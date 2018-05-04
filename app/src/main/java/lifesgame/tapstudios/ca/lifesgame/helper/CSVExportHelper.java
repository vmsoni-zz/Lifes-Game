package lifesgame.tapstudios.ca.lifesgame.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v13.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.opencsv.CSVWriter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import lifesgame.tapstudios.ca.lifesgame.model.GoalsAndTasks;

/**
 * Created by viditsoni on 2018-01-01.
 */

public class CSVExportHelper extends AsyncTask<String, String, Uri> {
    private final Context context;
    private final DatabaseHelper databaseHelper;
    private static final String TABLE_TASKS_GOALS_HEALTH_EXERCISE = "health_exercise";
    private static final String TABLE_TASKS_GOALS_WORK = "work";
    private static final String TABLE_TASKS_GOALS_SCHOOL = "school";
    private static final String TABLE_TASKS_GOALS_FAMILY_FRIENDS = "family_friends";
    private static final String TABLE_TASKS_GOALS_LEARNING = "learning";
    private static final String TABLE_TASKS_GOALS_OTHER = "other";
    private static final int REQUEST = 112;
    private final ProgressDialog dialog;


    public CSVExportHelper(Context context) {
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
    protected Uri doInBackground(final String... args) {
        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        final File file = new File(exportDir, "LifesGameData.csv");
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                try {
                    file.createNewFile();
                    CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                    List<GoalsAndTasks> goalsAndTasksList = databaseHelper.loadAllGoalsAndTask();

                    //Headers
                    csvWrite.writeNext(databaseHelper.getGoalsAndTasksHeaders());

                    if (goalsAndTasksList.size() > 0) {
                        for (int i = 0; i < goalsAndTasksList.size(); i++) {
                            String arrStr[] = {
                                    goalsAndTasksList.get(i).getId().toString(),
                                    goalsAndTasksList.get(i).getSilver().toString(),
                                    goalsAndTasksList.get(i).getTitle(),
                                    goalsAndTasksList.get(i).getCategory().getTodoTypeString(),
                                    goalsAndTasksList.get(i).getStartDateString(),
                                    goalsAndTasksList.get(i).getCreationDateString(),
                                    goalsAndTasksList.get(i).getCompletionDateString(),
                                    goalsAndTasksList.get(i).getDeadlineDateString(),
                                    goalsAndTasksList.get(i).getCompleted().toString(),
                                    goalsAndTasksList.get(i).getImprovementTypeMap().get(TABLE_TASKS_GOALS_HEALTH_EXERCISE).toString(),
                                    goalsAndTasksList.get(i).getImprovementTypeMap().get(TABLE_TASKS_GOALS_WORK).toString(),
                                    goalsAndTasksList.get(i).getImprovementTypeMap().get(TABLE_TASKS_GOALS_SCHOOL).toString(),
                                    goalsAndTasksList.get(i).getImprovementTypeMap().get(TABLE_TASKS_GOALS_FAMILY_FRIENDS).toString(),
                                    goalsAndTasksList.get(i).getImprovementTypeMap().get(TABLE_TASKS_GOALS_LEARNING).toString(),
                                    goalsAndTasksList.get(i).getImprovementTypeMap().get(TABLE_TASKS_GOALS_LEARNING).toString(),
                                    goalsAndTasksList.get(i).getImprovementTypeMap().get(TABLE_TASKS_GOALS_OTHER).toString(),
                                    goalsAndTasksList.get(i).getDescription(),
                                    String.valueOf(goalsAndTasksList.get(i).getCompletedCount()),
                                    String.valueOf(goalsAndTasksList.get(i).getFailedCount())
                            };
                            csvWrite.writeNext(arrStr);
                        }
                    }

                    csvWrite.close();
                    Uri csvURI = Uri.fromFile(file);
                    CSVExportResult(csvURI);
                } catch (IOException e) {
                    Log.e("MainActivity", e.getMessage(), e);
                    CSVExportResult(null);
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
    protected void onPostExecute(final Uri csvUri) {
    }

    private void CSVExportResult(final Uri csvUri) {
        if (this.dialog.isShowing()) {
            this.dialog.dismiss();
        }
        if (csvUri != null) {
            Toast.makeText(context, "Export successful!", Toast.LENGTH_SHORT).show();
            sendEmail(csvUri);
        } else {
            Toast.makeText(context, "Export failed!", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendEmail(Uri csvUri) {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "LifesGame CSV Data");
        sendIntent.putExtra(Intent.EXTRA_STREAM, csvUri);
        sendIntent.setType("text/html");
        context.startActivity(sendIntent);
    }
}
