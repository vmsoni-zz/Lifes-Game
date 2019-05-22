package lifesgame.tapstudios.ca.lifesgame.fragment;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.DocumentsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.android.vending.billing.IInAppBillingService;
import com.github.orangegangsters.lollipin.lib.managers.AppLock;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import lifesgame.tapstudios.ca.lifesgame.AnalyticsApplication;
import lifesgame.tapstudios.ca.lifesgame.BillingManager;
import lifesgame.tapstudios.ca.lifesgame.MainViewController;
import lifesgame.tapstudios.ca.lifesgame.activity.CustomPinActivity;
import lifesgame.tapstudios.ca.lifesgame.activity.MainActivity;
import lifesgame.tapstudios.ca.lifesgame.R;
import lifesgame.tapstudios.ca.lifesgame.helper.CSVExportHelper;
import lifesgame.tapstudios.ca.lifesgame.helper.DatabaseExportHelper;
import lifesgame.tapstudios.ca.lifesgame.helper.DatabaseHelper;
import lifesgame.tapstudios.ca.lifesgame.helper.DatabaseImportHelper;
import lifesgame.tapstudios.ca.lifesgame.helper.PurchaseHelper;

public class MarketplaceFragment extends Fragment {
    private MainViewController mainViewController;
    private BillingManager billingManager;
    private DatabaseHelper databaseHelper;
    private View marketplaceView;
    private LinearLayout purchaseFullPackage;
    private LinearLayout purchasedFullPackage;
    private LinearLayout purchaseCSVExport;
    private LinearLayout purchaseDataBackup;
    private LinearLayout purchaseSupportDev;
    private LinearLayout exportDatabase;
    private LinearLayout importDatabase;
    private LinearLayout purchaseUnlockPin;
    private LinearLayout importExportLl;
    private LinearLayout enableAppPin;
    private LinearLayout disableAppPin;
    private LinearLayout enableDisableAppPinLl;
    private LinearLayout exportCSVLl;
    private Context context;
    private DatabaseExportHelper databaseExportHelper;
    private DatabaseImportHelper databaseImportHelper;
    private CSVExportHelper csvExportHelper;
    private Tracker tracker;

    private TextView fullPackageTv;
    private TextView dbImportExportTv;
    private TextView csvExportTv;
    private TextView appPinTv;
    private TextView supportDevTv;

    private static final int REQUEST_CODE_ENABLE = 11;
    private static final int REQUEST_CODE_DISABLE = 12;
    public static final int PURCHASE_CODE = 1001;

    public MarketplaceFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.context = getActivity();
        databaseHelper = new DatabaseHelper(getActivity());
        mainViewController = new MainViewController(this);
        billingManager = new BillingManager(getActivity(), mainViewController.getUpdateListener(), this);

        marketplaceView = inflater.inflate(R.layout.activity_marketplace, container, false);
        purchaseFullPackage = (LinearLayout) marketplaceView.findViewById(R.id.purchase_full_package);
        purchaseCSVExport = (LinearLayout) marketplaceView.findViewById(R.id.purchase_csv_export);
        purchaseDataBackup = (LinearLayout) marketplaceView.findViewById(R.id.purchase_cloud_backup);
        purchaseSupportDev = (LinearLayout) marketplaceView.findViewById(R.id.purchaset_support_dev);
        exportDatabase = (LinearLayout) marketplaceView.findViewById(R.id.export_backup);
        importDatabase = (LinearLayout) marketplaceView.findViewById(R.id.import_database);
        purchaseUnlockPin = (LinearLayout) marketplaceView.findViewById(R.id.purchase_unlock_pin);
        importExportLl = (LinearLayout) marketplaceView.findViewById(R.id.import_export_db);
        enableAppPin = (LinearLayout) marketplaceView.findViewById(R.id.enable_pin);
        disableAppPin = (LinearLayout) marketplaceView.findViewById(R.id.disable_pin);
        enableDisableAppPinLl = (LinearLayout) marketplaceView.findViewById(R.id.enable_disable_app_pin);
        exportCSVLl = (LinearLayout) marketplaceView.findViewById(R.id.export_csv_ll);
        purchasedFullPackage = (LinearLayout) marketplaceView.findViewById(R.id.purchased_full_package_ll);

        fullPackageTv = (TextView) marketplaceView.findViewById(R.id.full_package_cost);
        dbImportExportTv = (TextView) marketplaceView.findViewById(R.id.db_backup_cost);
        csvExportTv = (TextView) marketplaceView.findViewById(R.id.csv_export_cost);
        appPinTv = (TextView) marketplaceView.findViewById(R.id.unlock_pin_cost);
        supportDevTv = (TextView) marketplaceView.findViewById(R.id.support_dev_cost);

        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        tracker = application.getDefaultTracker();
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Category")
                .setAction("Marketplace")
                .build());

        tracker.setScreenName("Marketplace");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
        return marketplaceView;
    }

    public void onBillingManagerSetupFinished() {
        List<String> skuList = new ArrayList();
        skuList.add("full_package");
        skuList.add("csv_export");
        skuList.add("db_import_export");
        skuList.add("app_pin");
        skuList.add("support_dev");
        addSkuRows(null, skuList, BillingClient.SkuType.INAPP, null);
        billingManager.queryPurchases();
    }

    private List<SkuDetails> addSkuRows(final List<String> inList, List<String> skusList,
                                        final @BillingClient.SkuType String billingType, final Runnable executeWhenFinished) {

        final List<SkuDetails> finalSkuDetailsList = new ArrayList<>();
        billingManager.querySkuDetailsAsync(billingType, skusList,
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(@BillingClient.BillingResponse int responseCode,
                                                     List<SkuDetails> skuDetailsList) {

                        if (responseCode == BillingClient.BillingResponse.OK) {
                            finalSkuDetailsList.addAll(skuDetailsList);
                            setupPrices(finalSkuDetailsList);
                            // Handle any error responses.
                        } else if (skuDetailsList != null
                                && skuDetailsList.size() > 0) {

                            // Traverse through the list of SKUs inside SkuDetailsList.
                        }
                    }
                });
        return finalSkuDetailsList;
    }

    public void setupListeners(List<Purchase> userPurchases) {
        for (Purchase purchase : userPurchases) {
            switch (purchase.getSku()) {
                case "full_package":
                    setupFullPackagePurchased();
                    setupImportExport();
                    setupCSVExport();
                    setupAppLock();
                    break;
                case "db_import_export":
                    setupImportExport();
                    break;
                case "csv_export":
                    setupCSVExport();
                    break;
                case "app_pin":
                    setupAppLock();
                    break;
            }
        }
    }

    private void setupFullPackagePurchased() {
        purchaseFullPackage.setVisibility(View.GONE);
        purchasedFullPackage.setVisibility(View.VISIBLE);
    }

    private void setupImportExport() {
        purchaseDataBackup.setVisibility(View.GONE);
        importExportLl.setVisibility(View.VISIBLE);

        exportDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseExportHelper = new DatabaseExportHelper(context);
                databaseExportHelper.execute();
            }
        });

        importDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrieveAndImportDatabaseFromUser();
            }
        });
    }

    private void setupCSVExport() {
        exportCSVLl.setVisibility(View.VISIBLE);
        purchaseCSVExport.setVisibility(View.GONE);

        exportCSVLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                csvExportHelper = new CSVExportHelper(context);
                csvExportHelper.execute();
            }
        });
    }

    private void setupAppLock() {
        purchaseUnlockPin.setVisibility(View.GONE);
        enableDisableAppPinLl.setVisibility(View.VISIBLE);

        if (databaseHelper.passcodeSet()) {
            enableAppPin.setVisibility(View.GONE);
            disableAppPin.setVisibility(View.VISIBLE);
        } else {
            enableAppPin.setVisibility(View.VISIBLE);
            disableAppPin.setVisibility(View.GONE);
        }

        enableAppPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CustomPinActivity.class);
                intent.putExtra(AppLock.EXTRA_TYPE, AppLock.ENABLE_PINLOCK);
                startActivityForResult(intent, REQUEST_CODE_ENABLE);
            }
        });
        disableAppPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CustomPinActivity.class);
                intent.putExtra(AppLock.EXTRA_TYPE, AppLock.DISABLE_PINLOCK);
                startActivityForResult(intent, REQUEST_CODE_DISABLE);
                databaseHelper.updateValue("passcode_set", "false");
            }
        });
    }

    private void setupAllPurchaseListeners() {
        purchaseFullPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    purchaseItem("full_package");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        purchaseCSVExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    purchaseItem("csv_export");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        purchaseDataBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    purchaseItem("db_import_export");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        purchaseUnlockPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    purchaseItem("app_pin");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        purchaseSupportDev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    purchaseItem("support_dev");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void retrieveAndImportDatabaseFromUser() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(intent, 124);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PURCHASE_CODE) {
            int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
            String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
            String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");

            if (resultCode == 1) {
                try {
                    JSONObject jo = new JSONObject(purchaseData);
                    String sku = jo.getString("productId");
                    Toast.makeText(context, "Purchase successful!", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    Toast.makeText(context, "Purchase failed", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }

        if (requestCode == REQUEST_CODE_ENABLE) {
            if (resultCode == -1) {
                databaseHelper.updateValue("passcode_set", "true");
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        }
        if (requestCode == REQUEST_CODE_DISABLE) {
            if (resultCode == -1) {
                databaseHelper.updateValue("passcode_set", "false");
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        }

        if (requestCode == 124) {
            if (resultCode == -1) {
                final String importedDatabasePath = getPath(getActivity(), data.getData());
                if (importedDatabasePath != null && importedDatabasePath.endsWith(".db")) {
                    try {
                        PermissionListener permissionlistener = new PermissionListener() {
                            @Override
                            public void onPermissionGranted() {
                                SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openDatabase(importedDatabasePath, null, 1);
                                int versionNumber = sqLiteDatabase.getVersion();
                                databaseImportHelper = new DatabaseImportHelper(context, versionNumber, importedDatabasePath);
                                databaseImportHelper.execute();
                                sqLiteDatabase.close();
                            }

                            @Override
                            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                                Toast.makeText(context, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                            }
                        };

                        TedPermission.with(context)
                                .setPermissionListener(permissionlistener)
                                .setDeniedMessage("If you reject this permission, you can not use import your database.\n\nPlease turn on permissions at [Setting] > [Permission]")
                                .setPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                .check();
                    } catch (Exception e) {
                        Toast.makeText(context, "Import failed!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Import failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private String getPath(Context context, Uri uri) {
        boolean isKitKatOrAbove;
        if (Build.VERSION.SDK_INT >= 19) {
            isKitKatOrAbove = true;
        } else {
            isKitKatOrAbove = false;
        }
        if (isKitKatOrAbove && DocumentsContract.isDocumentUri(context, uri)) {
            String[] split;
            if (isExternalStorageDocument(uri)) {
                split = DocumentsContract.getDocumentId(uri).split(":");
                if ("primary".equalsIgnoreCase(split[0])) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
                return null;
            } else if (isDownloadsDocument(uri)) {
                return getDataColumn(context, ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(DocumentsContract.getDocumentId(uri)).longValue()), null, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        } else {
            if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
            return null;
        }
        return null;
    }


    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private void purchaseItem(String sku) throws RemoteException {
        BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                .setSku(sku)
                .setType(BillingClient.SkuType.INAPP)
                .build();
        billingManager.initiatePurchaseFlow(flowParams);
    }

    private String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, new String[]{"_data"}, selection, selectionArgs, null);
            if (cursor == null || !cursor.moveToFirst()) {
                if (cursor != null) {
                    cursor.close();
                }
                return null;
            }
            String string = cursor.getString(cursor.getColumnIndexOrThrow("_data"));
            return string;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void setupPrices(List<SkuDetails> skuDetailsList) {
        if (skuDetailsList == null || skuDetailsList.size() == 0) {
            setPriceNotAvailable();
            return;
        }
        setupAllPurchaseListeners();
        try {
            for (SkuDetails details : skuDetailsList) {
                String sku = details.getSku();
                switch (sku) {
                    case "full_package":
                        fullPackageTv.setText(details.getPrice());
                        break;
                    case "csv_export":
                        csvExportTv.setText(details.getPrice());
                        break;
                    case "db_import_export":
                        dbImportExportTv.setText(details.getPrice());
                        break;
                    case "app_pin":
                        appPinTv.setText(details.getPrice());
                        break;
                    case "support_dev":
                        supportDevTv.setText(details.getPrice());
                        break;
                }
            }
        } catch (Exception e) {

        }
    }

    private void setPriceNotAvailable() {
        fullPackageTv.setText("Not Available");
        purchaseFullPackage.setClickable(false);
        csvExportTv.setText("Not Available");
        purchaseCSVExport.setClickable(false);
        dbImportExportTv.setText("Not Available");
        purchaseDataBackup.setClickable(false);
        appPinTv.setText("Not Available");
        purchaseUnlockPin.setClickable(false);
        supportDevTv.setText("Not Available");
        purchaseSupportDev.setClickable(false);
    }
}
