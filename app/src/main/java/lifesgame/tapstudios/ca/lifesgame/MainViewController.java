package lifesgame.tapstudios.ca.lifesgame;

import android.app.Activity;

import com.android.billingclient.api.Purchase;

import java.util.List;

import lifesgame.tapstudios.ca.lifesgame.fragment.MarketplaceFragment;

/**
 * Created by viditsoni on 2018-01-28.
 */

public class MainViewController {
    public MarketplaceFragment marketplaceFragment;
    private UpdateListener updateListener;

    public MainViewController(MarketplaceFragment marketplaceFragment) {
        this.marketplaceFragment = marketplaceFragment;
        updateListener = new UpdateListener();
    }

    public UpdateListener getUpdateListener() {
        return updateListener;
    }


    private class UpdateListener implements BillingUpdatesListener {
        UpdateListener() {}

        @Override
        public void onBillingClientSetupFinished() {
            marketplaceFragment.onBillingManagerSetupFinished();
        }

        @Override
        public void onConsumeFinished(String token, int result) {

        }

        @Override
        public void onPurchasesUpdated(List<Purchase> purchases) {

        }
    }
}