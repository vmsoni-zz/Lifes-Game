package lifesgame.tapstudios.ca.lifesgame.fragment;

import android.app.Fragment;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.billingclient.api.Purchase;
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
import lifesgame.tapstudios.ca.lifesgame.activity.CustomPinActivity;
import lifesgame.tapstudios.ca.lifesgame.activity.MainActivity;
import lifesgame.tapstudios.ca.lifesgame.R;
import lifesgame.tapstudios.ca.lifesgame.helper.CSVExportHelper;
import lifesgame.tapstudios.ca.lifesgame.helper.DatabaseExportHelper;
import lifesgame.tapstudios.ca.lifesgame.helper.DatabaseHelper;
import lifesgame.tapstudios.ca.lifesgame.helper.DatabaseImportHelper;
import lifesgame.tapstudios.ca.lifesgame.helper.PurchaseHelper;

public class MarketplaceFragment extends Fragment {
    public DatabaseHelper databaseHelper;
    private View marketplaceView;
    private LinearLayout purchaseFullPackage;
    private LinearLayout purchaseCSVExport;
    private LinearLayout purchaseDataBackup;
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
    private PurchaseHelper purchaseHelper;
    private CSVExportHelper csvExportHelper;
    private IInAppBillingService mService;
    private ServiceConnection mServiceConn;
    private Tracker tracker;

    private static final int REQUEST_CODE_ENABLE = 11;
    private static final int REQUEST_CODE_DISABLE = 12;
    public static final int PURCHASE_CODE = 1001;


    public MarketplaceFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        databaseHelper = new DatabaseHelper(getContext());
        purchaseHelper = new PurchaseHelper(context);
        marketplaceView = inflater.inflate(R.layout.activity_marketplace, container, false);
        purchaseFullPackage = (LinearLayout) marketplaceView.findViewById(R.id.purchase_full_package);
        purchaseCSVExport = (LinearLayout) marketplaceView.findViewById(R.id.purchase_csv_export);
        purchaseDataBackup = (LinearLayout) marketplaceView.findViewById(R.id.purchase_cloud_backup);
        exportDatabase = (LinearLayout) marketplaceView.findViewById(R.id.export_backup);
        importDatabase = (LinearLayout) marketplaceView.findViewById(R.id.import_database);
        purchaseUnlockPin = (LinearLayout) marketplaceView.findViewById(R.id.purchase_unlock_pin);
        importExportLl = (LinearLayout) marketplaceView.findViewById(R.id.import_export_db);
        enableAppPin = (LinearLayout) marketplaceView.findViewById(R.id.enable_pin);
        disableAppPin = (LinearLayout) marketplaceView.findViewById(R.id.disable_pin);
        enableDisableAppPinLl = (LinearLayout) marketplaceView.findViewById(R.id.enable_disable_app_pin);
        exportCSVLl = (LinearLayout) marketplaceView.findViewById(R.id.export_csv_ll);
        setupListeners();

        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        tracker = application.getDefaultTracker();
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Category")
                .setAction("Marketplace")
                .build());

        tracker.setScreenName("Marketplace");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());

        mServiceConn = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
                mService = null;
            }

            @Override
            public void onServiceConnected(ComponentName name,
                                           IBinder service) {
                mService = IInAppBillingService.Stub.asInterface(service);
            }
        };

        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        context.bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);

        return marketplaceView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            context.unbindService(mServiceConn);
        }
    }

    private void setupListeners() {
        List<Purchase> userPurchases = purchaseHelper.retrieveUserPurchases();

        if (userPurchases == null || userPurchases.size() == 0) {
            setupAllPurchaseListeners();
            setupImportExport();
            setupAppLock();
            setupCSVExport();
        } else {
            for (Purchase purchase : userPurchases) {
                switch (purchase.getPackageName()) {
                    case "FullPackage":
                        setupImportExport();
                        setupCSVExport();
                        setupAppLock();
                        break;
                    case "ImportExport":
                        setupImportExport();
                        break;
                    case "CSVExport":
                        setupCSVExport();
                        break;
                    case "AppPin":
                        setupAppLock();
                        break;
                }
            }
        }
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

            }
        });

        purchaseCSVExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        purchaseDataBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        purchaseUnlockPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        Bundle buyIntentBundle = mService.getBuyIntent(3, context.getPackageName(),
                "", "inapp", "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
        PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
        try {
            startIntentSenderForResult(pendingIntent.getIntentSender(),
                    PURCHASE_CODE, new Intent(), Integer.valueOf(0), Integer.valueOf(0),
                    Integer.valueOf(0), null);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
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
}
