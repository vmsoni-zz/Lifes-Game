package lifesgame.tapstudios.ca.lifesgame.helper;

import android.content.Context;
import android.support.annotation.Nullable;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lifesgame.tapstudios.ca.lifesgame.model.Rewards;

/**
 * Created by viditsoni on 2017-12-28.
 */

public class PurchaseHelper {
    private Context context;
    private DatabaseHelper databaseHelper;
    private BillingClient mBillingClient;
    private Boolean connectionFound;
    private PurchasesUpdatedListener purchasesUpdatedListener;

    public PurchaseHelper(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
        connectionFound = false;
        purchasesUpdatedListener = new PurchasesUpdatedListener() {
            @Override
            public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
            }
        };
        mBillingClient = BillingClient.newBuilder(context).setListener(purchasesUpdatedListener).build();
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponseCode) {
                if (billingResponseCode == BillingClient.BillingResponse.OK) {
                    connectionFound = true;
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                connectionFound = false;
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });
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
}
