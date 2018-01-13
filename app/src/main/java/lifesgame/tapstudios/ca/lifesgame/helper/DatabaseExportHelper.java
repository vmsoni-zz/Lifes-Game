package lifesgame.tapstudios.ca.lifesgame.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v13.app.ActivityCompat;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import lifesgame.tapstudios.ca.lifesgame.activity.MainActivity;

/**
 * Created by viditsoni on 2018-01-03.
 */

public class DatabaseExportHelper extends AsyncTask<String, String, String> {
    private final Context context;
    private final DatabaseHelper databaseHelper;
    private static final int REQUEST = 112;
    private final ProgressDialog dialog;

    public DatabaseExportHelper(Context context) {
        this.context = context;
        this.databaseHelper = new DatabaseHelper(context);
        dialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        this.dialog.setMessage("Exporting database...");
        this.dialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                try {
                    File storageDirectory = Environment.getExternalStorageDirectory();
                    if (!storageDirectory.canWrite()) {
                        exportResult(null);
                    }
                    File currentDatabase = context.getDatabasePath(DatabaseHelper.DATABASE_NAME);
                    File backupDirectory = new File(storageDirectory, "LifesGame");
                    if (!backupDirectory.exists()) {
                        backupDirectory.mkdir();
                    }
                    String backupPath = "/LifesGame/LifesGame - " + DateTimeFormat.forPattern("YYYY-MM-dd HH_mm_ss").print(new DateTime()) + ".db";
                    File backupDatabase = new File(storageDirectory, backupPath);
                    FileChannel source = new FileInputStream(currentDatabase).getChannel();
                    FileChannel destination = new FileOutputStream(backupDatabase).getChannel();
                    destination.transferFrom(source, 0, source.size());
                    source.close();
                    destination.close();
                    String fileLocation = storageDirectory + backupPath;
                    MediaScannerConnection.scanFile(context, new String[]{backupDatabase.toString()}, null, null);
                    exportResult(fileLocation);
                } catch (Exception e) {
                    exportResult(null);
                }
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(context, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(context)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject this permission, you cannot export your database.\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
        return null;
    }

    @Override
    protected void onPostExecute(final String exportDBPath) {
    }

    private void exportResult(final String exportDBPath) {
        if (this.dialog.isShowing()) {
            this.dialog.dismiss();
        }
        if (exportDBPath != null) {
            Toast.makeText(context, "Export successful!", Toast.LENGTH_SHORT).show();
            shareData(exportDBPath);
        } else {
            Toast.makeText(context, "Export failed!", Toast.LENGTH_SHORT).show();
        }
    }

    public void shareData(String exportDBPath) {
        Intent shareIntent = new Intent();
        shareIntent.setAction("android.intent.action.SEND");
        shareIntent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File(exportDBPath)));
        shareIntent.setType("application/x-sqlite3");
        ((Activity) context).startActivity(Intent.createChooser(shareIntent, "Send database to…"));
    }
}
