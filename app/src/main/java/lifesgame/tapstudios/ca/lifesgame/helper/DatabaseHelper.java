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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lifesgame.tapstudios.ca.lifesgame.GoalsAndTasks;
import lifesgame.tapstudios.ca.lifesgame.GraphData;

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
                        if(deadlineDateString != null) {
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
        }

        for (GoalsAndTasks goalsAndTasks : expiredGoalsAndTasksArrayList) {
            Date dt = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = sdf.format(dt);
            if(!goalsAndTasks.getCategory().equals("Goal")) {
                deleteData(goalsAndTasks.getId(), false, currentTime, true);
            }
        }
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
                        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                        String deadlineDateString = c.getString(c.getColumnIndex(TABLE_TASKS_GOALS_DEADLINE_DATE));
                        Date deadlineDate = null;
                        if(deadlineDateString != null) {
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
        }
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
        if (value.moveToFirst()) {
            values = value.getString(2);
        } else {
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

    public BarSet getImprovementTypesXP() {
        Map<String, Float> improvementTypesLevel = new HashMap<>();
        improvementTypesLevel.put("Health and Exercise", Float.valueOf(getValue(USER_HEALTH_EXERCISE)));
        improvementTypesLevel.put("Work", Float.valueOf(getValue(USER_WORK)));
        improvementTypesLevel.put("School", Float.valueOf(getValue(USER_SCHOOL)));
        improvementTypesLevel.put("Family and Friends", Float.valueOf(getValue(USER_FAMILY_FRIENDS)));
        improvementTypesLevel.put("Learning", Float.valueOf(getValue(USER_LEARNING)));
        improvementTypesLevel.put("Other", Float.valueOf(getValue(USER_OTHER)));
        BarSet barSetImprovementTypesLevel = new BarSet();
        for (String type : improvementTypesLevel.keySet()) {
            barSetImprovementTypesLevel.addBar(type, improvementTypesLevel.get(type));
        }
        return barSetImprovementTypesLevel;
    }

    public List<LineSet> getCompletedGoalTasks() {
        SQLiteDatabase db = this.getWritableDatabase();
        List<LineSet> graphDataLineSet = new ArrayList<>();
        Map<String, GraphData> graphData = new HashMap<>();
        String[] namesOfDays = new String[]{
                "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"
        };
        for (int i = 0; i < namesOfDays.length; i++) {
            graphData.put(namesOfDays[i], new GraphData());
        }
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
                        graphData.get(dayOfWeek).addSilver(c.getFloat(c.getColumnIndex(TABLE_TASKS_GOALS_SILVER)));
                        graphData.get(dayOfWeek).addCompletedAmount(1F);
                    } while (c.moveToNext());
                }
            }
        } catch (Exception e) {
            Log.println(Log.ERROR, "Error: ", "issue is " + e);
        }
        LineSet dataSetCompletedTaskGoals = new LineSet();
        LineSet dataSetTotalSilver = new LineSet();
        int i = 0;
        for (String date : graphData.keySet()) {
            dataSetTotalSilver.addPoint(namesOfDays[i], graphData.get(namesOfDays[i]).getSilverAmount());
            dataSetCompletedTaskGoals.addPoint(namesOfDays[i], graphData.get(namesOfDays[i]).getCompletedAmount());
            i++;
        }
        graphDataLineSet.addAll(Arrays.asList(dataSetTotalSilver, dataSetCompletedTaskGoals));
        return graphDataLineSet;
    }

    public int getCompletedToDoPercentage() {
        SQLiteDatabase db = this.getWritableDatabase();
        String queryComp = "SELECT COUNT (*) FROM " + TABLE_TASKS_GOALS + " WHERE " + TABLE_TASKS_GOALS_DELETED + " = 1 AND " + TABLE_TASKS_GOALS_COMPLETED + " = 1";
        String queryDel = "SELECT COUNT (*) FROM " + TABLE_TASKS_GOALS + " WHERE " + TABLE_TASKS_GOALS_DELETED + " = 1 ";
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
            Log.w("Error: ", e);
        }
        if (countTotal > 0 && countComp > 0) {
            return (int) Math.round((countComp / countTotal) * 100);
        }
        return 0;
    }

    public int getTotalDeletedCount() {
        SQLiteDatabase db = this.getWritableDatabase();
        String queryDel = "SELECT COUNT (*) FROM " + TABLE_TASKS_GOALS + " WHERE " + TABLE_TASKS_GOALS_DELETED + " = 1 ";
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
            Log.w("Error: ", e);
        }
        return countTotal;
    }
}
