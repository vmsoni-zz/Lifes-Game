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

public class PurchaseHelper {
    private Context context;
    private DatabaseHelper databaseHelper;
    public PurchaseHelper(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
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

}
