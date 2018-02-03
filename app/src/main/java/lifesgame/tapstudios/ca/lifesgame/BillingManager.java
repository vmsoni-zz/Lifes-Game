package lifesgame.tapstudios.ca.lifesgame;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lifesgame.tapstudios.ca.lifesgame.fragment.MarketplaceFragment;
import lifesgame.tapstudios.ca.lifesgame.model.Purchases;

/**
 * Created by viditsoni on 2018-01-28.
 */

public class BillingManager implements PurchasesUpdatedListener {
    private BillingClient mBillingClient;
    private Boolean mIsServiceConnected;
    private Integer mBillingClientResponseCode;
    private BillingUpdatesListener mBillingUpdatesListener;
    private Activity mActivity;
    private MarketplaceFragment marketplaceFragment;
    private static final String BASE_64_ENCODED_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAo0GUGUaHjz+8UKBf3g5vvsahVqVFxgFBRdYbUpwViH1jvu2pDvyeChZlml57atJF7x8W+026RMbqrVlLMgnWOyVQgu+kICOW/iZCYY/K0LlA7z5bknHYAHSDGePEl8TD0BKwx4H38bH76pFbw3+qINWt3+mfGxDfFHTzzsJPJSpGt41eukQDukCOx8yBTIe7C76dGL5BJeHCPHWJdswYNpcdSiIoGqPbstP5PtypVnkC/wYOhlo36FUhy6BP64lLdv30nH+cQKTI/YML00E+Kojy7JI+Vi55mNhELPP4eVxOj2rOIgHAqjWK11gRjce7oY6MfmjVu3ZUNBLYpE/PGQIDAQAB";

    public BillingManager(Activity activity, final BillingUpdatesListener updatesListener, final MarketplaceFragment marketplaceFragment) {
        mActivity = activity;
        mBillingUpdatesListener = updatesListener;
        mBillingClient = BillingClient.newBuilder(mActivity).setListener(this).build();
        mIsServiceConnected = false;
        this.marketplaceFragment = marketplaceFragment;

        // Start the setup asynchronously.
        // The specified listener is called once setup completes.
        // New purchases are reported through the onPurchasesUpdated() callback
        // of the class specified using the setListener() method above.
        startServiceConnection(new Runnable() {
            @Override
            public void run() {
                // Notify the listener that the billing client is ready.
                mBillingUpdatesListener.onBillingClientSetupFinished();
                //marketplaceFragment.onBillingManagerSetupFinished();
                // IAB is fully setup. Now get an inventory of stuff the user owns.
            }
        });
    }

    public void startServiceConnection(final Runnable executeOnSuccess) {
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponseCode) {
                if (billingResponseCode == BillingClient.BillingResponse.OK) {
                    mIsServiceConnected = true;
                    if (executeOnSuccess != null) {
                        executeOnSuccess.run();
                    }
                }
                mBillingClientResponseCode = billingResponseCode;
            }

            @Override
            public void onBillingServiceDisconnected() {
                mIsServiceConnected = false;
            }
        });
    }

    public void querySkuDetailsAsync(final @BillingClient.SkuType String billingType, final List<String> skuList, final SkuDetailsResponseListener listener) {
        // Create a runnable from the request to use inside the connection retry policy.
        Runnable queryRequest = new Runnable() {
            @Override
            public void run() {
                // Create the SkuDetailParams object
                SkuDetailsParams skuDetailsParams = SkuDetailsParams.newBuilder().setSkusList(skuList).setType(billingType).build();
                // Run the query asynchronously.
                mBillingClient.querySkuDetailsAsync(skuDetailsParams, listener);
            }
        };
        executeServiceRequest(queryRequest);
    }

    private void executeServiceRequest(Runnable runnable) {
        if (mIsServiceConnected) {
            runnable.run();
        } else {
            // If the billing service disconnects, try to reconnect once.
            startServiceConnection(runnable);
        }
    }

    @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
        if (responseCode == BillingClient.BillingResponse.OK) {
            for (Purchase purchase : purchases) {
                if(!handlePurchase(purchase)) {
                    marketplaceFragment.setupListeners(new ArrayList<Purchase>());
                }
            }
            marketplaceFragment.setupListeners(purchases);
        } else if (responseCode == BillingClient.BillingResponse.USER_CANCELED) {
            Toast.makeText(this.mActivity, "Purchase Cancelled", Toast.LENGTH_SHORT).show();
            marketplaceFragment.setupListeners(new ArrayList<Purchase>());
        } else {
            Toast.makeText(this.mActivity, "An Error Occurred", Toast.LENGTH_SHORT).show();
            marketplaceFragment.setupListeners(new ArrayList<Purchase>());
        }
    }


    public void queryPurchases() {
        Runnable queryToExecute = new Runnable() {
            @Override
            public void run() {
                Purchase.PurchasesResult purchasesResult = mBillingClient.queryPurchases(BillingClient.SkuType.INAPP);
                if (purchasesResult.getResponseCode() == BillingClient.BillingResponse.OK) {
                    // Skip subscription purchases query as they are not supported.
                } else {
                    // Handle any other error response codes.
                }
                onQueryPurchasesFinished(purchasesResult);
            }
        };
        executeServiceRequest(queryToExecute);
    }

    /**
     * Handle a result from querying of purchases and report an updated list to the listener
     */
    private void onQueryPurchasesFinished(Purchase.PurchasesResult result) {
        final List<Purchase> purchaseResults = new ArrayList<>();
        // Have we been disposed of in the meantime? If so, or bad result code, then quit
        if (mBillingClient == null || result.getResponseCode() != BillingClient.BillingResponse.OK) {
            return;
        }

        // Update the UI and purchases inventory with new list of purchases
        onPurchasesUpdated(BillingClient.BillingResponse.OK, result.getPurchasesList());
    }

    public void initiatePurchaseFlow(final BillingFlowParams flowParams) {
        Runnable purchaseFlowRequest = new Runnable() {
            @Override
            public void run() {
                mBillingClient.launchBillingFlow(mActivity, flowParams);
            }
        };
        executeServiceRequest(purchaseFlowRequest);
    }

    private Boolean handlePurchase(Purchase purchase) {
        if (!verifyValidSignature(purchase.getOriginalJson(), purchase.getSignature())) {
            return false;
        }

        return true;
        //mPurchases.add(purchase);
    }

    private boolean verifyValidSignature(String signedData, String signature) {
        // Some sanity checks to see if the developer (that's you!) really followed the
        // instructions to run this sample (don't put these checks on your app!)
        try {
            return Security.verifyPurchase(BASE_64_ENCODED_PUBLIC_KEY, signedData, signature);
        } catch (IOException e) {
            return false;
        }
    }
}
