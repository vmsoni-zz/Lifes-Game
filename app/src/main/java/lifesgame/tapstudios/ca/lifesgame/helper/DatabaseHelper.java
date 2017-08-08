package lifesgame.tapstudios.ca.lifesgame.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteTableLockedException;
import android.util.Log;

import com.db.chart.model.LineSet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lifesgame.tapstudios.ca.lifesgame.GoalsAndTasks;

/**
 * Created by Vidit Soni on 5/24/2017.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "lifesGame";

    private static final String TABLE_TASKS_GOALS = "tasks_goals";
    private static final String TABLE_ID = "ID";
    private static final String TABLE_TASKS_GOALS_DATA = "data";
    private static final String TABLE_TASKS_GOALS_TITLE = "title";
    private static final String TABLE_TASKS_GOALS_CATEGORY = "category";
    private static final String TABLE_TASKS_GOALS_SILVER = "silver";
    private static final String TABLE_TASKS_GOALS_COMPLETION_DATE = "completion_date";
    private static final String TABLE_TASKS_GOALS_DELETED = "deleted";
    private static final String TABLE_TASKS_GOALS_COMPLETED = "completed";


    private static final String TABLE_KEY_VALUES =  "key_values";
    private static final String TABLE_KEY = "key";
    private static final String TABLE_VALUES = "value";

    public static final String CHAR_HEALTH = "character_health";
    public static final String CHAR_MAX_HEALTH = "character_max_health";
    public static final String CHAR_XP = "character_xp";
    public static final String CHAR_MAX_XP = "character_max_xp";
    public static final String CHAR_LVL = "character_level";
    public static final String SILVER_AMOUNT_TOTAL = "total_silver";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db){
        String createTableTasksGoals = "CREATE TABLE " + TABLE_TASKS_GOALS + " (" +
                TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TABLE_TASKS_GOALS_SILVER + " INTEGER, " +
                TABLE_TASKS_GOALS_TITLE + " TEXT, " +
                TABLE_TASKS_GOALS_CATEGORY + " TEXT, " +
                TABLE_TASKS_GOALS_COMPLETION_DATE + " DATETIME, " +
                TABLE_TASKS_GOALS_DELETED + " BOOL NOT NULL DEFAULT '0', " +
                TABLE_TASKS_GOALS_COMPLETED + " BOOL, " +
                TABLE_TASKS_GOALS_DATA + " TEXT )";
        String createTableKeyValues = "CREATE TABLE " + TABLE_KEY_VALUES + " (" + TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TABLE_KEY + " TEXT," +  TABLE_VALUES + " TEXT)";

        db.execSQL(createTableKeyValues);
        db.execSQL(createTableTasksGoals);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1){
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_TASKS_GOALS);
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_KEY_VALUES);
        onCreate(db);
    }

    public void initiateKeys() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_KEY, CHAR_HEALTH);
        contentValues.put(TABLE_VALUES, "100");
        db.insert(TABLE_KEY_VALUES, null, contentValues);
        contentValues.put(TABLE_KEY, CHAR_MAX_HEALTH);
        contentValues.put(TABLE_VALUES, "100");
        db.insert(TABLE_KEY_VALUES, null, contentValues);
        contentValues.put(TABLE_KEY, CHAR_XP);
        contentValues.put(TABLE_VALUES, "0");
        db.insert(TABLE_KEY_VALUES, null, contentValues);
        contentValues.put(TABLE_KEY, CHAR_MAX_XP);
        contentValues.put(TABLE_VALUES, "1000");
        db.insert(TABLE_KEY_VALUES, null, contentValues);
        contentValues.put(TABLE_KEY, CHAR_LVL);
        contentValues.put(TABLE_VALUES, "1");
        db.insert(TABLE_KEY_VALUES, null, contentValues);
        contentValues.put(TABLE_KEY, SILVER_AMOUNT_TOTAL);
        contentValues.put(TABLE_VALUES, "0");
        db.insert(TABLE_KEY_VALUES, null, contentValues);
    }

    public Long addData(String description, String category, String title, Long silver) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_TASKS_GOALS_TITLE, title);
        contentValues.put(TABLE_TASKS_GOALS_DATA, description);
        contentValues.put(TABLE_TASKS_GOALS_CATEGORY, category);
        contentValues.put(TABLE_TASKS_GOALS_SILVER, silver);
        Log.d(TAG, "addData: Adding " + description + " to " + TABLE_TASKS_GOALS + " under category " + category);

        Long result = db.insert(TABLE_TASKS_GOALS, null, contentValues);
        return result;
    }

    public ArrayList loadAllGoalsAndTask() {
        ArrayList<GoalsAndTasks> goalsAndTasksArrayList = new ArrayList<GoalsAndTasks>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TASKS_GOALS  + " WHERE " + TABLE_TASKS_GOALS_DELETED + " = 0 ";
        try {
            Cursor c = db.rawQuery(query, null);
            if (c != null ) {
                if  (c.moveToFirst()) {
                    do {
                        GoalsAndTasks goalsAndTasks = new GoalsAndTasks(
                                c.getString(c.getColumnIndex(TABLE_TASKS_GOALS_TITLE)),
                                c.getString(c.getColumnIndex(TABLE_TASKS_GOALS_DATA)),
                                c.getString(c.getColumnIndex(TABLE_TASKS_GOALS_CATEGORY)),
                                c.getLong(c.getColumnIndex(TABLE_ID)),
                                c.getLong(c.getColumnIndex(TABLE_TASKS_GOALS_SILVER))
                        );
                        goalsAndTasksArrayList.add(goalsAndTasks);
                    }while (c.moveToNext());
                }
            }
        } catch (SQLiteException se ) {
            Log.e(getClass().getSimpleName(), "Could not create or Open the database");
        }
        return goalsAndTasksArrayList;
    }

    public void deleteData(Long id, boolean completed, String completionDate) {
        //Open the database
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TABLE_TASKS_GOALS_COMPLETED, completed);
        cv.put(TABLE_TASKS_GOALS_COMPLETION_DATE, completionDate);
        cv.put(TABLE_TASKS_GOALS_DELETED, true);
        //Execute sql query to remove from database
        //NOTE: When removing by String in SQL, value must be enclosed with ''
        database.update(TABLE_TASKS_GOALS, cv, TABLE_ID + "= " + id, null);
        //Close the database
        database.close();
    }

    public void addKey(String key) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_KEY_VALUES, key);
        db.insert(TABLE_KEY_VALUES, null, contentValues);
    }

    public String getValue(String key) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_KEY_VALUES + " WHERE " + TABLE_KEY + " = " + "'" + key + "'";
        Cursor value = db.rawQuery(query, null);
        String values;
        if(value.moveToFirst()){
            values = value.getString(2);
        }
        else{
            values = null;
        }
        return values;
    }

    public void updateValue(String key, String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_VALUES, value);
        db.update(TABLE_KEY_VALUES, contentValues, TABLE_KEY + " = " + "'" + key + "'", null);
    }

    public void deleteValue(String key) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL("DELETE FROM " + TABLE_KEY_VALUES + " WHERE " + TABLE_KEY + "= " + "'" + key + "'");
    }

    public LineSet getCompletedGoalTasks() {
        SQLiteDatabase db = this.getWritableDatabase();
        Map<String, Float> goalTasksDay = new HashMap<>();
        String[] namesOfDays = new String[] {
            "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"
        };
        goalTasksDay.put("Sun", 0F);
        goalTasksDay.put("Mon", 0F);
        goalTasksDay.put("Tue", 0F);
        goalTasksDay.put("Wed", 0F);
        goalTasksDay.put("Thu", 0F);
        goalTasksDay.put("Fri", 0F);
        goalTasksDay.put("Sat", 0F);

        String query = "SELECT * FROM " + TABLE_TASKS_GOALS + " WHERE DATE(" + TABLE_TASKS_GOALS_COMPLETION_DATE + ") >= DATE('now', 'weekday 0', '-7 days') ORDER BY " + TABLE_TASKS_GOALS_COMPLETION_DATE;
        DateFormat dateFormat = new SimpleDateFormat("E");
        try {
            Cursor c = db.rawQuery(query, null);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        String dateTime = c.getString(c.getColumnIndexOrThrow(TABLE_TASKS_GOALS_COMPLETION_DATE));
                        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTime);
                        String dayOfWeek = dateFormat.format(date).toString();
                        if(goalTasksDay.get(dayOfWeek) != null) {
                            goalTasksDay.put(dayOfWeek, goalTasksDay.get(dayOfWeek) + c.getLong(c.getColumnIndex(TABLE_TASKS_GOALS_SILVER)));
                        }
                        else {
                            goalTasksDay.put(dayOfWeek, c.getFloat(c.getColumnIndex(TABLE_TASKS_GOALS_SILVER)));
                        }
                    } while (c.moveToNext());
                }
            }
        }
        catch (Exception e)
        {
            Log.println(Log.ERROR, "Error: ", "issue is " + e);
        }
        LineSet dataSet = new LineSet();
        int i = 0;
        for(String date : goalTasksDay.keySet()) {
            dataSet.addPoint(namesOfDays[i], goalTasksDay.get(namesOfDays[i]));
            i++;
        }
        return dataSet;
    }
}
