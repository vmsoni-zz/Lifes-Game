package lifesgame.tapstudios.ca.lifesgame;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.Purchase;

import java.util.List;

/**
 * Created by viditsoni on 2018-01-28.
 */

public interface BillingUpdatesListener {
    void onBillingClientSetupFinished();
    void onConsumeFinished(String token, @BillingClient.BillingResponse int result);
    void onPurchasesUpdated(List<Purchase> purchases);
}