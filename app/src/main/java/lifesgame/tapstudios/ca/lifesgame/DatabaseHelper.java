package lifesgame.tapstudios.ca.lifesgame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteTableLockedException;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Vidit Soni on 5/24/2017.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "lifesGame";

    private static final String TABLE_TASKS_GOALS = "tasks_goals";
    private static final String TABLE_ID = "ID";
    private static final String TABLE_TASKS_GOALS_DATA = "data";
    private static final String TABLE_TASKS_GOALS_CATEGORY = "category";

    private static final String TABLE_KEY_VALUES =  "key_values";
    private static final String TABLE_KEY = "key";
    private static final String TABLE_VALUES = "value";

    public static final String CHAR_HEALTH = "character_health";
    public static final String CHAR_MAX_HEALTH = "character_max_health";
    public static final String CHAR_XP = "character_xp";
    public static final String CHAR_MAX_XP = "character_max_xp";
    public static final String CHAR_LVL = "character_level";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db){
        String createTableTasksGoals = "CREATE TABLE " + TABLE_TASKS_GOALS + " (" + TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TABLE_TASKS_GOALS_CATEGORY + " TEXT, " + TABLE_TASKS_GOALS_DATA + " TEXT )";
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
    }

    public boolean addData(String item, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_TASKS_GOALS_DATA, item);
        contentValues.put(TABLE_TASKS_GOALS_CATEGORY, item);
        Log.d(TAG, "addData: Adding " + item + " to " + TABLE_TASKS_GOALS + " under category " + category);

        long result = db.insert(TABLE_TASKS_GOALS, null, contentValues);
        if(result == -1) {
            return false;
        }
        else{
            return true;
        }
    }

    public ArrayList loadAllGoalsAndTask() {
        ArrayList<String> goalsAndTasksArrayList = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            Cursor c = db.rawQuery("SELECT "+ TABLE_TASKS_GOALS_DATA + " FROM " + TABLE_TASKS_GOALS, null);
            if (c != null ) {
                if  (c.moveToFirst()) {
                    do {
                        String goalAndTaskString = c.getString(c.getColumnIndex(TABLE_TASKS_GOALS_DATA));
                        goalsAndTasksArrayList.add(goalAndTaskString);
                    }while (c.moveToNext());
                }
            }
        } catch (SQLiteException se ) {
            Log.e(getClass().getSimpleName(), "Could not create or Open the database");
        }
        return goalsAndTasksArrayList;
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TASKS_GOALS;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void deleteData(String data) {
        //Open the database
        SQLiteDatabase database = this.getWritableDatabase();
        //Execute sql query to remove from database
        //NOTE: When removing by String in SQL, value must be enclosed with ''
        database.execSQL("DELETE FROM " + TABLE_TASKS_GOALS + " WHERE " + TABLE_TASKS_GOALS_DATA + "= " + "'" + data + "'");

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
}
