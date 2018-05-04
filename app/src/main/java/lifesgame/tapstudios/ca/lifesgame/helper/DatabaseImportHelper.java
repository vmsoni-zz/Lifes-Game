package lifesgame.tapstudios.ca.lifesgame.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v13.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import lifesgame.tapstudios.ca.lifesgame.activity.MainActivity;

/**
 * Created by viditsoni on 2018-01-03.
 */

public class DatabaseImportHelper extends AsyncTask<String ,String, Boolean> {
    private Context context;
    private DatabaseHelper databaseHelper;
    private static final int REQUEST = 112;
    private final Integer importVersion;
    private final String importedDatabasePath;
    private final ProgressDialog dialog;

    public DatabaseImportHelper(Context context, Integer importVersion, String importedDatabasePath) {
        this.context = context;
        this.databaseHelper = new DatabaseHelper(context);
        this.importVersion = importVersion;
        this.importedDatabasePath = importedDatabasePath;
        dialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        this.dialog.setMessage("Importing database...");
        this.dialog.show();
    }

    public Boolean setupPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (!hasPermissions(context, PERMISSIONS)) {
                ActivityCompat.requestPermissions((Activity) context, PERMISSIONS, REQUEST);
            } else {
                //do here
            }
        } else {
            //do here
        }
        return true;
    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        if(setupPermissions()) {
            try {
                if (importVersion > databaseHelper.getDBVersion()) {
                    throw new Exception("Unable to downgrade the database");
                }
                databaseHelper.importDatabase(importedDatabasePath);
                return true;
            } catch (Exception e) {
                Toast.makeText(context, "Database version is outdated", Toast.LENGTH_SHORT).show();
                Log.e("Exception", e.getMessage());
            }
        }
        return false;
    }

    @Override
    protected void onPostExecute(final Boolean imported) {
        if (this.dialog.isShowing()){
            this.dialog.dismiss();
        }
        if (imported){
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
            Toast.makeText(context, "Import successful!", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Import failed!", Toast.LENGTH_SHORT).show();
        }
    }
}
