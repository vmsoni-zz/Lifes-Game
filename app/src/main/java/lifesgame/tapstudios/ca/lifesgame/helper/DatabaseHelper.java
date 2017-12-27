package lifesgame.tapstudios.ca.lifesgame.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.db.chart.model.BarSet;
import com.db.chart.model.LineSet;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lifesgame.tapstudios.ca.lifesgame.GoalsAndTasks;
import lifesgame.tapstudios.ca.lifesgame.GraphData;
import lifesgame.tapstudios.ca.lifesgame.StatisticFilters;

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
    private static final String TABLE_TASKS_GOALS_DEADLINE_DATE = "deadline_date";
    private static final String TABLE_TASKS_GOALS_DELETED = "deleted";
    private static final String TABLE_TASKS_GOALS_COMPLETED = "completed";
    private static final String TABLE_TASKS_GOALS_HEALTH_EXERCISE = "health_exercise";
    private static final String TABLE_TASKS_GOALS_WORK = "work";
    private static final String TABLE_TASKS_GOALS_SCHOOL = "school";
    private static final String TABLE_TASKS_GOALS_FAMILY_FRIENDS = "family_friends";
    private static final String TABLE_TASKS_GOALS_LEARNING = "learning";
    private static final String TABLE_TASKS_GOALS_OTHER = "other";
    private static final String TABLE_KEY_VALUES = "key_values";
    private static final String TABLE_KEY = "key";
    private static final String TABLE_VALUES = "value";

    public static final String CHAR_HEALTH = "character_health";
    public static final String CHAR_MAX_HEALTH = "character_max_health";
    public static final String CHAR_XP = "character_xp";
    public static final String CHAR_MAX_XP = "character_max_xp";
    public static final String CHAR_LVL = "character_level";
    public static final String SILVER_AMOUNT_TOTAL = "total_silver";
    public static final String USER_HEALTH_EXERCISE = "health_exercise";
    public static final String USER_WORK = "work";
    public static final String USER_SCHOOL = "school";
    public static final String USER_FAMILY_FRIENDS = "family_friends";
    public static final String USER_LEARNING = "learning";
    public static final String USER_OTHER = "other";
    public static final String USER_MAX_HEALTH_EXERCISE = "max_health_exercise";
    public static final String USER_MAX_WORK = "max_work";
    public static final String USER_MAX_SCHOOL = "max_school";
    public static final String USER_MAX_FAMILY_FRIENDS = "max_family_friends";
    public static final String USER_MAX_LEARNING = "max_learning";
    public static final String USER_MAX_OTHER = "max_other";
    public static final String USER_HEALTH_EXERCISE_LVL = "health_exercise_lvl";
    public static final String USER_WORK_LVL = "work_lvl";
    public static final String USER_SCHOOL_LVL = "school_lvl";
    public static final String USER_FAMILY_FRIENDS_LVL = "family_friends_lvl";
    public static final String USER_LEARNING_LVL = "learning_lvl";
    public static final String USER_OTHER_LVL = "other_lvl";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableTasksGoals = "CREATE TABLE " + TABLE_TASKS_GOALS + " (" +
                TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TABLE_TASKS_GOALS_SILVER + " INTEGER, " +
                TABLE_TASKS_GOALS_TITLE + " TEXT, " +
                TABLE_TASKS_GOALS_CATEGORY + " TEXT, " +
                TABLE_TASKS_GOALS_COMPLETION_DATE + " DATETIME, " +
                TABLE_TASKS_GOALS_DEADLINE_DATE + " DATETIME, " +
                TABLE_TASKS_GOALS_DELETED + " BOOL NOT NULL DEFAULT '0', " +
                TABLE_TASKS_GOALS_COMPLETED + " BOOL, " +
                TABLE_TASKS_GOALS_HEALTH_EXERCISE + " BOOL, " +
                TABLE_TASKS_GOALS_WORK + " BOOL, " +
                TABLE_TASKS_GOALS_SCHOOL + " BOOL, " +
                TABLE_TASKS_GOALS_FAMILY_FRIENDS + " BOOL, " +
                TABLE_TASKS_GOALS_LEARNING + " BOOL, " +
                TABLE_TASKS_GOALS_OTHER + " BOOL, " +
                TABLE_TASKS_GOALS_DATA + " TEXT )";
        String createTableKeyValues = "CREATE TABLE " + TABLE_KEY_VALUES + " (" + TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TABLE_KEY + " TEXT," + TABLE_VALUES + " TEXT)";

        db.execSQL(createTableKeyValues);
        db.execSQL(createTableTasksGoals);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
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
        contentValues.put(TABLE_KEY, USER_HEALTH_EXERCISE);
        contentValues.put(TABLE_VALUES, "0");
        db.insert(TABLE_KEY_VALUES, null, contentValues);
        contentValues.put(TABLE_KEY, USER_MAX_HEALTH_EXERCISE);
        contentValues.put(TABLE_VALUES, "1000");
        db.insert(TABLE_KEY_VALUES, null, contentValues);
        contentValues.put(TABLE_KEY, USER_HEALTH_EXERCISE_LVL);
        contentValues.put(TABLE_VALUES, "0");
        db.insert(TABLE_KEY_VALUES, null, contentValues);
        contentValues.put(TABLE_KEY, USER_WORK);
        contentValues.put(TABLE_VALUES, "0");
        db.insert(TABLE_KEY_VALUES, null, contentValues);
        contentValues.put(TABLE_KEY, USER_MAX_WORK);
        contentValues.put(TABLE_VALUES, "1000");
        db.insert(TABLE_KEY_VALUES, null, contentValues);
        contentValues.put(TABLE_KEY, USER_WORK_LVL);
        contentValues.put(TABLE_VALUES, "0");
        db.insert(TABLE_KEY_VALUES, null, contentValues);
        contentValues.put(TABLE_KEY, USER_SCHOOL);
        contentValues.put(TABLE_VALUES, "0");
        db.insert(TABLE_KEY_VALUES, null, contentValues);
        contentValues.put(TABLE_KEY, USER_MAX_SCHOOL);
        contentValues.put(TABLE_VALUES, "1000");
        db.insert(TABLE_KEY_VALUES, null, contentValues);
        contentValues.put(TABLE_KEY, USER_SCHOOL_LVL);
        contentValues.put(TABLE_VALUES, "0");
        db.insert(TABLE_KEY_VALUES, null, contentValues);
        contentValues.put(TABLE_KEY, USER_FAMILY_FRIENDS);
        contentValues.put(TABLE_VALUES, "0");
        db.insert(TABLE_KEY_VALUES, null, contentValues);
        contentValues.put(TABLE_KEY, USER_MAX_FAMILY_FRIENDS);
        contentValues.put(TABLE_VALUES, "1000");
        db.insert(TABLE_KEY_VALUES, null, contentValues);
        contentValues.put(TABLE_KEY, USER_FAMILY_FRIENDS_LVL);
        contentValues.put(TABLE_VALUES, "0");
        db.insert(TABLE_KEY_VALUES, null, contentValues);
        contentValues.put(TABLE_KEY, USER_LEARNING);
        contentValues.put(TABLE_VALUES, "0");
        db.insert(TABLE_KEY_VALUES, null, contentValues);
        contentValues.put(TABLE_KEY, USER_MAX_LEARNING);
        contentValues.put(TABLE_VALUES, "1000");
        db.insert(TABLE_KEY_VALUES, null, contentValues);
        contentValues.put(TABLE_KEY, USER_LEARNING_LVL);
        contentValues.put(TABLE_VALUES, "0");
        db.insert(TABLE_KEY_VALUES, null, contentValues);
        contentValues.put(TABLE_KEY, USER_OTHER);
        contentValues.put(TABLE_VALUES, "0");
        db.insert(TABLE_KEY_VALUES, null, contentValues);
        contentValues.put(TABLE_KEY, USER_MAX_OTHER);
        contentValues.put(TABLE_VALUES, "1000");
        db.insert(TABLE_KEY_VALUES, null, contentValues);
        contentValues.put(TABLE_KEY, USER_OTHER_LVL);
        contentValues.put(TABLE_VALUES, "0");
        db.insert(TABLE_KEY_VALUES, null, contentValues);
        db.close();
    }

    public Long addData(String description, String category, String title, Long silver, Map<String, Boolean> improvementType, String deadlineDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_TASKS_GOALS_TITLE, title);
        contentValues.put(TABLE_TASKS_GOALS_DATA, description);
        contentValues.put(TABLE_TASKS_GOALS_CATEGORY, category);
        contentValues.put(TABLE_TASKS_GOALS_DEADLINE_DATE, deadlineDate);
        contentValues.put(TABLE_TASKS_GOALS_SILVER, silver);
        contentValues.put(TABLE_TASKS_GOALS_HEALTH_EXERCISE, improvementType.get(TABLE_TASKS_GOALS_HEALTH_EXERCISE));
        contentValues.put(TABLE_TASKS_GOALS_WORK, improvementType.get(TABLE_TASKS_GOALS_WORK));
        contentValues.put(TABLE_TASKS_GOALS_SCHOOL, improvementType.get(TABLE_TASKS_GOALS_SCHOOL));
        contentValues.put(TABLE_TASKS_GOALS_FAMILY_FRIENDS, improvementType.get(TABLE_TASKS_GOALS_FAMILY_FRIENDS));
        contentValues.put(TABLE_TASKS_GOALS_LEARNING, improvementType.get(TABLE_TASKS_GOALS_LEARNING));
        contentValues.put(TABLE_TASKS_GOALS_OTHER, improvementType.get(TABLE_TASKS_GOALS_OTHER));
        Log.d(TAG, "addData: Adding " + description + " to " + TABLE_TASKS_GOALS + " under category " + category);
        Long result = db.insert(TABLE_TASKS_GOALS, null, contentValues);
        db.close();
        return result;
    }

    public void resetExpiredGoalsAndTasks() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<GoalsAndTasks> expiredGoalsAndTasksArrayList = new ArrayList<GoalsAndTasks>();

        String query = "SELECT * FROM " + TABLE_TASKS_GOALS + " WHERE " + TABLE_TASKS_GOALS_DELETED + " = 0 "
                + "AND " + TABLE_TASKS_GOALS_CATEGORY + "<>'Goal'"
                + " AND DATE(" + TABLE_TASKS_GOALS_COMPLETION_DATE + ") = DATE(NOW() - INTERVAL 1 DAY) ORDER BY " + TABLE_TASKS_GOALS_COMPLETION_DATE;
        try {
            Cursor c = db.rawQuery(query, null);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        Map<String, Boolean> improvementType = new HashMap<>();
                        improvementType.put(TABLE_TASKS_GOALS_HEALTH_EXERCISE, c.getInt(c.getColumnIndex(TABLE_TASKS_GOALS_HEALTH_EXERCISE)) > 0);
                        improvementType.put(TABLE_TASKS_GOALS_WORK, c.getInt(c.getColumnIndex(TABLE_TASKS_GOALS_WORK)) > 0);
                        improvementType.put(TABLE_TASKS_GOALS_SCHOOL, c.getInt(c.getColumnIndex(TABLE_TASKS_GOALS_SCHOOL)) > 0);
                        improvementType.put(TABLE_TASKS_GOALS_FAMILY_FRIENDS, c.getInt(c.getColumnIndex(TABLE_TASKS_GOALS_FAMILY_FRIENDS)) > 0);
                        improvementType.put(TABLE_TASKS_GOALS_LEARNING, c.getInt(c.getColumnIndex(TABLE_TASKS_GOALS_LEARNING)) > 0);
                        improvementType.put(TABLE_TASKS_GOALS_OTHER, c.getInt(c.getColumnIndex(TABLE_TASKS_GOALS_OTHER)) > 0);
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String deadlineDateString = c.getString(c.getColumnIndex(TABLE_TASKS_GOALS_DEADLINE_DATE));
                        Date deadlineDate = null;
                        if (deadlineDateString != null) {
                            try {
                                deadlineDate = formatter.parse(deadlineDateString);
                            } catch (ParseException e) {
                                Log.e(getClass().getSimpleName(), "Could not parse deadline date string");
                            }
                        }
                        GoalsAndTasks goalsAndTasks = new GoalsAndTasks(
                                c.getString(c.getColumnIndex(TABLE_TASKS_GOALS_TITLE)),
                                c.getString(c.getColumnIndex(TABLE_TASKS_GOALS_DATA)),
                                c.getString(c.getColumnIndex(TABLE_TASKS_GOALS_CATEGORY)),
                                c.getLong(c.getColumnIndex(TABLE_ID)),
                                c.getLong(c.getColumnIndex(TABLE_TASKS_GOALS_SILVER)),
                                improvementType,
                                deadlineDate
                        );
                        expiredGoalsAndTasksArrayList.add(goalsAndTasks);
                    } while (c.moveToNext());
                }
            }
        } catch (SQLiteException se) {
            Log.e(getClass().getSimpleName(), "Could not create or Open the database");
            db.close();
        }

        for (GoalsAndTasks goalsAndTasks : expiredGoalsAndTasksArrayList) {
            Date dt = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = sdf.format(dt);
            if (!goalsAndTasks.getCategory().equals("Goal")) {
                deleteData(goalsAndTasks.getId(), false, currentTime, true);
            }
        }
        db.close();
    }

    public ArrayList loadAllGoalsAndTask() {
        ArrayList<GoalsAndTasks> goalsAndTasksArrayList = new ArrayList<GoalsAndTasks>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TASKS_GOALS + " WHERE " + TABLE_TASKS_GOALS_DELETED + " = 0 ";
        try {
            Cursor c = db.rawQuery(query, null);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        Map<String, Boolean> improvementType = new HashMap<>();
                        improvementType.put(TABLE_TASKS_GOALS_HEALTH_EXERCISE, c.getInt(c.getColumnIndex(TABLE_TASKS_GOALS_HEALTH_EXERCISE)) > 0);
                        improvementType.put(TABLE_TASKS_GOALS_WORK, c.getInt(c.getColumnIndex(TABLE_TASKS_GOALS_WORK)) > 0);
                        improvementType.put(TABLE_TASKS_GOALS_SCHOOL, c.getInt(c.getColumnIndex(TABLE_TASKS_GOALS_SCHOOL)) > 0);
                        improvementType.put(TABLE_TASKS_GOALS_FAMILY_FRIENDS, c.getInt(c.getColumnIndex(TABLE_TASKS_GOALS_FAMILY_FRIENDS)) > 0);
                        improvementType.put(TABLE_TASKS_GOALS_LEARNING, c.getInt(c.getColumnIndex(TABLE_TASKS_GOALS_LEARNING)) > 0);
                        improvementType.put(TABLE_TASKS_GOALS_OTHER, c.getInt(c.getColumnIndex(TABLE_TASKS_GOALS_OTHER)) > 0);
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String deadlineDateString = c.getString(c.getColumnIndex(TABLE_TASKS_GOALS_DEADLINE_DATE));
                        Date deadlineDate = null;
                        if (deadlineDateString != null) {
                            try {
                                deadlineDate = formatter.parse(deadlineDateString);
                            } catch (ParseException e) {
                                Log.e(getClass().getSimpleName(), "Could not parse deadline date string");
                            }
                        }
                        GoalsAndTasks goalsAndTasks = new GoalsAndTasks(
                                c.getString(c.getColumnIndex(TABLE_TASKS_GOALS_TITLE)),
                                c.getString(c.getColumnIndex(TABLE_TASKS_GOALS_DATA)),
                                c.getString(c.getColumnIndex(TABLE_TASKS_GOALS_CATEGORY)),
                                c.getLong(c.getColumnIndex(TABLE_ID)),
                                c.getLong(c.getColumnIndex(TABLE_TASKS_GOALS_SILVER)),
                                improvementType,
                                deadlineDate
                        );
                        goalsAndTasksArrayList.add(goalsAndTasks);
                    } while (c.moveToNext());
                }
            }
        } catch (SQLiteException se) {
            Log.e(getClass().getSimpleName(), "Could not create or Open the database");
            db.close();
        }
        db.close();
        return goalsAndTasksArrayList;
    }

    public void deleteData(Long id, boolean completed, String completionDate, boolean deleted) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TABLE_TASKS_GOALS_COMPLETED, completed);
        cv.put(TABLE_TASKS_GOALS_COMPLETION_DATE, completionDate);
        cv.put(TABLE_TASKS_GOALS_DELETED, deleted);
        database.update(TABLE_TASKS_GOALS, cv, TABLE_ID + "= " + id, null);
        database.close();
    }

    public boolean deleteDataPermanent(Long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS_GOALS, TABLE_ID + " = " + id, null);
        db.close();
        return true;
    }

    public void addKey(String key) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_KEY_VALUES, key);
        db.insert(TABLE_KEY_VALUES, null, contentValues);
        db.close();
    }

    public String getValue(String key) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_KEY_VALUES + " WHERE " + TABLE_KEY + " = " + "'" + key + "'";
        Cursor value = db.rawQuery(query, null);
        String values;
        if (value.moveToFirst()) {
            values = value.getString(2);
        } else {
            values = null;
        }
        db.close();
        return values;
    }

    public float getImprovementValue(String key, StatisticFilters dataRange, Integer dayOfWeek) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query;
        switch (dataRange) {
            case DAILY:
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DAY_OF_WEEK, dayOfWeek);
                DateFormat todayDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                query = "SELECT COUNT (*) FROM " + TABLE_TASKS_GOALS + " WHERE " + TABLE_TASKS_GOALS_DELETED + " = 1 " +
                        "AND " + TABLE_TASKS_GOALS_COMPLETED + " = 1 " +
                        "AND " + key + "= 1 " +
                        "AND DATE(" + TABLE_TASKS_GOALS_COMPLETION_DATE + ") = DATE('" + todayDateFormat.format(cal.getTime()) + "') ";
                break;
            case WEEKLY:
                query = "SELECT COUNT (*) FROM " + TABLE_TASKS_GOALS +
                        " WHERE " + TABLE_TASKS_GOALS_DELETED + " = 1 " +
                        "AND " + TABLE_TASKS_GOALS_COMPLETED + " = 1 " +
                        "AND " + key + "= 1 " +
                        "AND DATE(" + TABLE_TASKS_GOALS_COMPLETION_DATE + ") >= DATE('now', 'weekday 0', '-7 days') ";
                break;
            case MONTHLY:
                String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
                query = "SELECT COUNT (*) FROM " + TABLE_TASKS_GOALS +
                        " WHERE " + TABLE_TASKS_GOALS_DELETED + " = 1 " +
                        "AND " + TABLE_TASKS_GOALS_COMPLETED + " = 1 " +
                        "AND " + key + "= 1 " +
                        "AND strftime('%Y', " + TABLE_TASKS_GOALS_COMPLETION_DATE + ") = '" + year + "' ";
                break;
            default:
                query = "SELECT COUNT (*) FROM " + TABLE_TASKS_GOALS +
                        " WHERE " + TABLE_TASKS_GOALS_DELETED + " = 1 " +
                        "AND " + TABLE_TASKS_GOALS_COMPLETED + " = 1 " +
                        "AND " + key + "= 1 " +
                        "AND DATE(" + TABLE_TASKS_GOALS_COMPLETION_DATE + ") >= DATE('now', 'weekday 0', '-7 days') ";
                break;
        }

        float improvementCount = 0;
        try {
            Cursor c = db.rawQuery(query, null);
            if (null != c) {
                if (c.getCount() > 0) {
                    c.moveToFirst();
                    improvementCount = c.getFloat(0);
                }
            }
            c.close();
        } catch (Exception e) {
            db.close();
            Log.w("Error: ", e);
        }
        return improvementCount;
    }

    public void updateValue(String key, String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_VALUES, value);
        db.update(TABLE_KEY_VALUES, contentValues, TABLE_KEY + " = " + "'" + key + "'", null);
    }

    public BarSet getImprovementTypesXP(StatisticFilters dataRange, Integer dayOfWeek) {
        Map<String, Float> improvementTypesLevel = new HashMap<>();
        improvementTypesLevel.put("health", getImprovementValue(USER_HEALTH_EXERCISE, dataRange, dayOfWeek));
        improvementTypesLevel.put("Work", getImprovementValue(USER_WORK, dataRange, dayOfWeek));
        improvementTypesLevel.put("School", getImprovementValue(USER_SCHOOL, dataRange, dayOfWeek));
        improvementTypesLevel.put("Family", getImprovementValue(USER_FAMILY_FRIENDS, dataRange, dayOfWeek));
        improvementTypesLevel.put("Learning", getImprovementValue(USER_LEARNING, dataRange, dayOfWeek));
        improvementTypesLevel.put("Other", getImprovementValue(USER_OTHER, dataRange, dayOfWeek));
        BarSet barSetImprovementTypesLevel = new BarSet();
        for (String type : improvementTypesLevel.keySet()) {
            barSetImprovementTypesLevel.addBar(type, improvementTypesLevel.get(type));
        }
        return barSetImprovementTypesLevel;
    }

    public List<LineSet> getCompletedGoalTasks(StatisticFilters dataRange, Integer dayOfWeek) {
        SQLiteDatabase db = this.getWritableDatabase();
        List<LineSet> graphDataLineSet = new ArrayList<>();
        Map<String, GraphData> graphData = new HashMap<>();
        String namesOfDays[];
        String query = null;
        String keyValue = null;
        DateFormat dateFormat = null;
        switch (dataRange) {
            case DAILY:
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DAY_OF_WEEK, dayOfWeek);
                DateFormat todayDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                namesOfDays = new String[]{
                        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24"
                };
                query = "SELECT " + TABLE_TASKS_GOALS_COMPLETION_DATE + ", " + TABLE_TASKS_GOALS_SILVER + " FROM " + TABLE_TASKS_GOALS +
                        " WHERE DATE(" + TABLE_TASKS_GOALS_COMPLETION_DATE + ") = DATE('" + todayDateFormat.format(cal.getTime()) + "') " +
                        "AND " + TABLE_TASKS_GOALS_COMPLETED + " = 1 " +
                        "ORDER BY " + TABLE_TASKS_GOALS_COMPLETION_DATE;
                break;
            case WEEKLY:
                namesOfDays = new String[]{
                        "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"
                };
                query = "SELECT " + TABLE_TASKS_GOALS_COMPLETION_DATE + ", " + TABLE_TASKS_GOALS_SILVER + " FROM " + TABLE_TASKS_GOALS +
                        " WHERE DATE(" + TABLE_TASKS_GOALS_COMPLETION_DATE + ") >= DATE('now', 'weekday 0', '-7 days') " +
                        "AND " + TABLE_TASKS_GOALS_COMPLETED + " = 1 " +
                        "ORDER BY " + TABLE_TASKS_GOALS_COMPLETION_DATE;
                break;
            case MONTHLY:
                String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
                namesOfDays = new String[]{
                        "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
                };
                query = "SELECT " + TABLE_TASKS_GOALS_COMPLETION_DATE + " ," + TABLE_TASKS_GOALS_SILVER + " FROM " + TABLE_TASKS_GOALS +
                        " WHERE strftime('%Y', " + TABLE_TASKS_GOALS_COMPLETION_DATE + ") = '" + year + "' " +
                        "AND " + TABLE_TASKS_GOALS_COMPLETED + " = 1 " +
                        "ORDER BY " + TABLE_TASKS_GOALS_COMPLETION_DATE;
                break;
            default:
                namesOfDays = new String[]{
                        "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"
                };
                query = "SELECT " + TABLE_TASKS_GOALS_COMPLETION_DATE + " ," + TABLE_TASKS_GOALS_SILVER + " FROM " + TABLE_TASKS_GOALS +
                        " WHERE DATE(" + TABLE_TASKS_GOALS_COMPLETION_DATE + ") >= DATE('now', 'weekday 0', '-7 days') " +
                        "AND " + TABLE_TASKS_GOALS_COMPLETED + " = 1 " +
                        "ORDER BY " + TABLE_TASKS_GOALS_COMPLETION_DATE;
                break;
        }

        for (int i = 0; i < namesOfDays.length; i++) {
            graphData.put(namesOfDays[i], new GraphData());
        }
        try {
            Cursor c = db.rawQuery(query, null);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        String dateTime = c.getString(c.getColumnIndexOrThrow(TABLE_TASKS_GOALS_COMPLETION_DATE));
                        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTime);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        switch (dataRange) {
                            case DAILY:
                                int hours = calendar.get(Calendar.HOUR_OF_DAY);
                                keyValue = String.valueOf(hours);
                                break;
                            case WEEKLY:
                                dateFormat = new SimpleDateFormat("E");
                                keyValue = dateFormat.format(date);
                                break;
                            case MONTHLY:
                                dateFormat = new SimpleDateFormat("MMM");
                                keyValue = dateFormat.format(date);
                                break;
                        }
                        graphData.get(keyValue).addSilver(c.getFloat(c.getColumnIndex(TABLE_TASKS_GOALS_SILVER)));
                        graphData.get(keyValue).addCompletedAmount(1F);
                    } while (c.moveToNext());
                }
            }
        } catch (Exception e) {
            db.close();
            Log.println(Log.ERROR, "Error: ", "issue is " + e);

        }
        LineSet dataSetCompletedTaskGoals = new LineSet();
        LineSet dataSetTotalSilver = new LineSet();
        int i = 0;
        if (dataRange == StatisticFilters.DAILY) {
            for (String date : graphData.keySet()) {
                if (Integer.valueOf(namesOfDays[i]) % 6 == 0 || Integer.valueOf(namesOfDays[i]) == 1) {
                    dataSetTotalSilver.addPoint(namesOfDays[i], graphData.get(namesOfDays[i]).getSilverAmount());
                    dataSetCompletedTaskGoals.addPoint(namesOfDays[i], graphData.get(namesOfDays[i]).getCompletedAmount());
                } else {
                    dataSetTotalSilver.addPoint("", graphData.get(namesOfDays[i]).getSilverAmount());
                    dataSetCompletedTaskGoals.addPoint("", graphData.get(namesOfDays[i]).getCompletedAmount());
                }
                i++;
            }
        } else {
            for (String date : graphData.keySet()) {
                dataSetTotalSilver.addPoint(namesOfDays[i], graphData.get(namesOfDays[i]).getSilverAmount());
                dataSetCompletedTaskGoals.addPoint(namesOfDays[i], graphData.get(namesOfDays[i]).getCompletedAmount());
                i++;
            }
        }
        graphDataLineSet.addAll(Arrays.asList(dataSetTotalSilver, dataSetCompletedTaskGoals));
        db.close();
        return graphDataLineSet;
    }

    public int getCompletedToDoPercentage(StatisticFilters dataRange, Integer dayOfWeek) {
        SQLiteDatabase db = this.getWritableDatabase();
        String queryComp;
        String queryDel;
        switch (dataRange) {
            case DAILY:
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DAY_OF_WEEK, dayOfWeek);
                DateFormat todayDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                queryComp = "SELECT COUNT (*) FROM " + TABLE_TASKS_GOALS + " WHERE " + TABLE_TASKS_GOALS_DELETED + " = 1 " +
                        "AND " + TABLE_TASKS_GOALS_COMPLETED + " = 1 " +
                        "AND DATE(" + TABLE_TASKS_GOALS_COMPLETION_DATE + ") = DATE('" + todayDateFormat.format(cal.getTime()) + "') ";
                queryDel = "SELECT COUNT (*) FROM " + TABLE_TASKS_GOALS +
                        " WHERE " + TABLE_TASKS_GOALS_DELETED + " = 1 " +
                        "AND DATE(" + TABLE_TASKS_GOALS_COMPLETION_DATE + ") = DATE('" + todayDateFormat.format(cal.getTime()) + "') ";
                break;
            case WEEKLY:
                queryComp = "SELECT COUNT (*) FROM " + TABLE_TASKS_GOALS +
                        " WHERE " + TABLE_TASKS_GOALS_DELETED + " = 1 " +
                        "AND " + TABLE_TASKS_GOALS_COMPLETED + " = 1 " +
                        "AND DATE(" + TABLE_TASKS_GOALS_COMPLETION_DATE + ") >= DATE('now', 'weekday 0', '-7 days') ";

                queryDel = "SELECT COUNT (*) FROM " + TABLE_TASKS_GOALS +
                        " WHERE " + TABLE_TASKS_GOALS_DELETED + " = 1 " +
                        " AND DATE(" + TABLE_TASKS_GOALS_COMPLETION_DATE + ") >= DATE('now', 'weekday 0', '-7 days') ";
                break;
            case MONTHLY:
                String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
                queryComp = "SELECT COUNT (*) FROM " + TABLE_TASKS_GOALS +
                        " WHERE " + TABLE_TASKS_GOALS_DELETED + " = 1 " +
                        "AND " + TABLE_TASKS_GOALS_COMPLETED + " = 1 " +
                        "AND strftime('%Y', " + TABLE_TASKS_GOALS_COMPLETION_DATE + ") = '" + year + "' ";
                queryDel = "SELECT COUNT (*) FROM " + TABLE_TASKS_GOALS +
                        " WHERE " + TABLE_TASKS_GOALS_DELETED + " = 1 " +
                        "AND strftime('%Y', " + TABLE_TASKS_GOALS_COMPLETION_DATE + ") = '" + year + "' ";
                break;
            default:
                queryComp = "SELECT COUNT (*) FROM " + TABLE_TASKS_GOALS +
                        " WHERE " + TABLE_TASKS_GOALS_DELETED + " = 1 " +
                        "AND " + TABLE_TASKS_GOALS_COMPLETED + " = 1 " +
                        "AND DATE(" + TABLE_TASKS_GOALS_COMPLETION_DATE + ") >= DATE('now', 'weekday 0', '-7 days') ";

                queryDel = "SELECT COUNT (*) FROM " + TABLE_TASKS_GOALS +
                        " WHERE " + TABLE_TASKS_GOALS_DELETED + " = 1 " +
                        " AND DATE(" + TABLE_TASKS_GOALS_COMPLETION_DATE + ") >= DATE('now', 'weekday 0', '-7 days') ";
                break;
        }

        double countTotal = 0;
        double countComp = 0;
        try {
            Cursor c = db.rawQuery(queryComp, null);
            if (null != c) {
                if (c.getCount() > 0) {
                    c.moveToFirst();
                    countComp = c.getDouble(0);
                }
            }
            c = db.rawQuery(queryDel, null);
            if (null != c) {
                if (c.getCount() > 0) {
                    c.moveToFirst();
                    countTotal = c.getDouble(0);
                }
            }
            c.close();
        } catch (Exception e) {
            db.close();
            Log.w("Error: ", e);
        }
        if (countTotal > 0 && countComp > 0) {
            return (int) Math.round((countComp / countTotal) * 100);
        }
        db.close();
        return 0;
    }

    public int getTotalDeletedCount(StatisticFilters dataRange) {
        SQLiteDatabase db = this.getWritableDatabase();
        DateFormat todayDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String queryDel = "SELECT COUNT (*) FROM " + TABLE_TASKS_GOALS +
                " WHERE " + TABLE_TASKS_GOALS_DELETED + " = 1 " +
                "AND DATE(" + TABLE_TASKS_GOALS_COMPLETION_DATE + ") = DATE('" + todayDateFormat.format(date) + "') ";
        int countTotal = 0;
        try {
            Cursor c = db.rawQuery(queryDel, null);
            if (null != c) {
                if (c.getCount() > 0) {
                    c.moveToFirst();
                    countTotal = c.getInt(0);
                }
            }
            c.close();
        } catch (Exception e) {
            db.close();
            Log.w("Error: ", e);
        }
        db.close();
        return countTotal;
    }
}
