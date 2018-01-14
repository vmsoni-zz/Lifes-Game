package lifesgame.tapstudios.ca.lifesgame.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lifesgame.tapstudios.ca.lifesgame.model.Rewards;

import static com.gun0912.tedpermission.TedPermission.TAG;

/**
 * Created by viditsoni on 2017-12-28.
 */

public class PurchaseHelper implements PurchasesUpdatedListener {
    private Context context;
    private DatabaseHelper databaseHelper;
    private BillingClient mBillingClient;
    private Boolean connectionFound;
    public PurchaseHelper(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
        connectionFound = false;
        mBillingClient = BillingClient.newBuilder((Activity) context).setListener(this).build();
        startServiceConnectionIfNeeded(null);
    }

    private void startServiceConnectionIfNeeded(final Runnable executeOnSuccess) {
        if (mBillingClient.isReady()) {
            if (executeOnSuccess != null) {
                executeOnSuccess.run();
            }
        } else {
            mBillingClient.startConnection(new BillingClientStateListener() {
                @Override
                public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponse) {
                    if (billingResponse == BillingClient.BillingResponse.OK) {
                        Log.i(TAG, "onBillingSetupFinished() response: " + billingResponse);
                        if (executeOnSuccess != null) {
                            executeOnSuccess.run();
                            connectionFound = true;
                        }
                    } else {
                        Log.w(TAG, "onBillingSetupFinished() error code: " + billingResponse);
                        connectionFound = false;
                    }
                }

                @Override
                public void onBillingServiceDisconnected() {
                    Log.w(TAG, "onBillingServiceDisconnected()");
                    connectionFound = false;
                }
            });
        }
    }

    public void purchase(Activity activity, BillingFlowParams flowParams) {
        mBillingClient.launchBillingFlow(activity, flowParams);
    }

    public Boolean purchaseReward(Rewards reward) {
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);
        Integer totalSilver = Integer.valueOf(databaseHelper.getValue(databaseHelper.SILVER_AMOUNT_TOTAL));
        if (totalSilver >= reward.getCost()) {
            databaseHelper.purchaseReward(reward.getId(),
                    currentTime,
                    totalSilver - reward.getCost(),
                    true,
                    reward.getUnlimitedConsumption());
            return true;
        }
        return false;
    }

    public List<Purchase> retrieveUserPurchases() {
        if (!connectionFound) {
            return null;
        }
        final List<Purchase> userPurchases = new ArrayList<>();
        mBillingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.INAPP,
                new PurchaseHistoryResponseListener() {
                    @Override
                    public void onPurchaseHistoryResponse(@BillingClient.BillingResponse int responseCode,
                                                          List<Purchase> purchasesList) {
                        if (responseCode == BillingClient.BillingResponse.OK
                                && purchasesList != null) {
                            userPurchases.addAll(purchasesList);
                        }
                    }
                });
        return userPurchases;
    }

    public List<SkuDetails> getPrices(List<String> skuList) {
        final List<SkuDetails> skuDetailsListValues = null;
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
        mBillingClient.querySkuDetailsAsync(params.build(),
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList) {
                        if (skuDetailsList != null) {
                            skuDetailsListValues.addAll(skuDetailsList);
                        }
                    }
                });
        return skuDetailsListValues;
    }

    @Override
    public void onPurchasesUpdated(@BillingClient.BillingResponse int responseCode,
                                   List<Purchase> purchases) {
        if (responseCode == BillingClient.BillingResponse.OK
                && purchases != null) {
            Toast.makeText(this.context, "Purchase successful", Toast.LENGTH_SHORT).show();
        } else if (responseCode == BillingClient.BillingResponse.USER_CANCELED) {
            Toast.makeText(this.context, "Purchase unsuccessful", Toast.LENGTH_SHORT).show();
        }
    }
}
