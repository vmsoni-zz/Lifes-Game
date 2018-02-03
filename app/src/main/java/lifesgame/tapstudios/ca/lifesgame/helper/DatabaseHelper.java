package lifesgame.tapstudios.ca.lifesgame.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.db.chart.model.BarSet;
import com.db.chart.model.LineSet;

import java.io.ByteArrayOutputStream;
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

import lifesgame.tapstudios.ca.lifesgame.TodoType;
import lifesgame.tapstudios.ca.lifesgame.model.GoalsAndTasks;
import lifesgame.tapstudios.ca.lifesgame.model.GraphData;
import lifesgame.tapstudios.ca.lifesgame.model.Rewards;
import lifesgame.tapstudios.ca.lifesgame.StatisticFilters;

/**
 * Created by Vidit Soni on 5/24/2017.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    public static final String DATABASE_NAME = "lifesGame";

    private static final String TABLE_TASKS_GOALS = "tasks_goals";
    private static final String TABLE_ID = "ID";
    private static final String TABLE_TASKS_GOALS_DATA = "data";
    private static final String TABLE_TASKS_GOALS_TITLE = "title";
    private static final String TABLE_TASKS_GOALS_CATEGORY = "category";
    private static final String TABLE_TASKS_GOALS_SILVER = "silver";
    private static final String TABLE_TASKS_GOALS_CREATION_DATE = "creation_date";
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

    private static final String TABLE_REWARDS = "rewards";
    private static final String TABLE_REWARDS_TITLE = "title";
    private static final String TABLE_REWARDS_DESCRIPTION = "description";
    private static final String TABLE_REWARDS_COST = "cost";
    private static final String TABLE_REWARDS_PURCHASED = "purchased";
    private static final String TABLE_REWARDS_CREATION_DATE = "creation_date";
    private static final String TABLE_REWARDS_PURCHASE_DATE = "purchase_date";
    private static final String TABLE_REWARDS_UNLIMITED = "unlimited_consumption";
    private static final String TABLE_REWARDS_STYLE_COLOR = "style_color";

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
    public static final String USER_PROFILE_PICTURE = "profile_picture";
    public static final String USER_PASSCODE_SET = "passcode_set";


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
                TABLE_TASKS_GOALS_CREATION_DATE + " DATETIME, " +
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

        String createTableRewards = "CREATE TABLE " + TABLE_REWARDS + " (" +
                TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TABLE_REWARDS_COST + " INTEGER, " +
                TABLE_REWARDS_TITLE + " TEXT, " +
                TABLE_REWARDS_DESCRIPTION + " TEXT, " +
                TABLE_REWARDS_CREATION_DATE + " DATETIME, " +
                TABLE_REWARDS_PURCHASE_DATE + " DATETIME, " +
                TABLE_REWARDS_UNLIMITED + " BOOL, " +
                TABLE_REWARDS_STYLE_COLOR + " TEXT, " +
                TABLE_REWARDS_PURCHASED + " BOOL NOT NULL DEFAULT '0')";

        db.execSQL(createTableKeyValues);
        db.execSQL(createTableTasksGoals);
        db.execSQL(createTableRewards);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_TASKS_GOALS);
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_KEY_VALUES);
        onCreate(db);
    }

    public synchronized Integer getDBVersion() throws SQLException {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.getVersion();
    }

    public void deleteTableKeyValyes() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_KEY_VALUES);
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
        contentValues.put(TABLE_VALUES, "100");
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
        contentValues.put(TABLE_KEY, USER_PASSCODE_SET);
        contentValues.put(TABLE_VALUES, "FALSE");
        db.insert(TABLE_KEY_VALUES, null, contentValues);
        contentValues.put(TABLE_KEY, USER_OTHER_LVL);
        contentValues.put(TABLE_VALUES, "0");
        db.insert(TABLE_KEY_VALUES, null, contentValues);
        contentValues.put(TABLE_KEY, USER_PROFILE_PICTURE);
        db.insert(TABLE_KEY_VALUES, null, contentValues);
        db.close();
        initializeDefaultRewards();
    }

    public void initializeDefaultRewards() {
        Date todayDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        addReward("End of day relaxation",
                "Take a nap, go out for dinner, have a glass of wine, your call!",
                100,
                formatter.format(todayDate),
                true,
                "blue");

        addReward("Treat yourself!",
                "Watch a TV show, play a video game, read a book, anything fun!",
                200,
                formatter.format(todayDate),
                true,
                "red");

        addReward("Yummy!",
                "Have a cheat day (pizza, chocolate, pop, you choose!)",
                400,
                formatter.format(todayDate),
                true,
                "green");

        addReward("You deserve it!",
                "Take a day off (go to the park, the beach, or just get away from home!)",
                5000,
                formatter.format(todayDate),
                true,
                "orange");
    }

    public String[] getGoalsAndTasksHeaders() {
        String[] tableTasksGoalsHeaders = {
                TABLE_ID,
                TABLE_TASKS_GOALS_SILVER,
                TABLE_TASKS_GOALS_TITLE,
                TABLE_TASKS_GOALS_CATEGORY,
                TABLE_TASKS_GOALS_CREATION_DATE,
                TABLE_TASKS_GOALS_COMPLETION_DATE,
                TABLE_TASKS_GOALS_DEADLINE_DATE,
                TABLE_TASKS_GOALS_DELETED,
                TABLE_TASKS_GOALS_COMPLETED,
                TABLE_TASKS_GOALS_HEALTH_EXERCISE,
                TABLE_TASKS_GOALS_WORK,
                TABLE_TASKS_GOALS_SCHOOL,
                TABLE_TASKS_GOALS_FAMILY_FRIENDS,
                TABLE_TASKS_GOALS_LEARNING,
                TABLE_TASKS_GOALS_OTHER,
                TABLE_TASKS_GOALS_DATA
        };
        return tableTasksGoalsHeaders;
    }

    public Long addData(String description, String category, String title, Long silver, Map<String, Boolean> improvementType, String deadlineDate, String creationDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_TASKS_GOALS_TITLE, title);
        contentValues.put(TABLE_TASKS_GOALS_DATA, description);
        contentValues.put(TABLE_TASKS_GOALS_CATEGORY, category);
        contentValues.put(TABLE_TASKS_GOALS_CREATION_DATE, creationDate);
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

    public void updateTodo(Long id, String description, String category, String title, Long silver, Map<String, Boolean> improvementType, String deadlineDate, String creationDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_TASKS_GOALS_TITLE, title);
        contentValues.put(TABLE_TASKS_GOALS_DATA, description);
        contentValues.put(TABLE_TASKS_GOALS_CATEGORY, category);
        contentValues.put(TABLE_TASKS_GOALS_CREATION_DATE, creationDate);
        contentValues.put(TABLE_TASKS_GOALS_DEADLINE_DATE, deadlineDate);
        contentValues.put(TABLE_TASKS_GOALS_SILVER, silver);
        contentValues.put(TABLE_TASKS_GOALS_HEALTH_EXERCISE, improvementType.get(TABLE_TASKS_GOALS_HEALTH_EXERCISE));
        contentValues.put(TABLE_TASKS_GOALS_WORK, improvementType.get(TABLE_TASKS_GOALS_WORK));
        contentValues.put(TABLE_TASKS_GOALS_SCHOOL, improvementType.get(TABLE_TASKS_GOALS_SCHOOL));
        contentValues.put(TABLE_TASKS_GOALS_FAMILY_FRIENDS, improvementType.get(TABLE_TASKS_GOALS_FAMILY_FRIENDS));
        contentValues.put(TABLE_TASKS_GOALS_LEARNING, improvementType.get(TABLE_TASKS_GOALS_LEARNING));
        contentValues.put(TABLE_TASKS_GOALS_OTHER, improvementType.get(TABLE_TASKS_GOALS_OTHER));
        Log.d(TAG, "addData: Adding " + description + " to " + TABLE_TASKS_GOALS + " under category " + category);
        db.update(TABLE_TASKS_GOALS, contentValues, TABLE_ID + "= " + id, null);
        db.close();
    }

    public Boolean passcodeSet() {
        String passcodeSet = getValue(USER_PASSCODE_SET);
        if (passcodeSet != null && passcodeSet.equals("true")) {
            return true;
        }
        return false;
    }

    public void updateReward(Integer id, String title, String description, Integer cost, String creationDate, Boolean unlimited, String colorStyle) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_REWARDS_TITLE, title);
        contentValues.put(TABLE_REWARDS_DESCRIPTION, description);
        contentValues.put(TABLE_REWARDS_CREATION_DATE, creationDate);
        contentValues.put(TABLE_REWARDS_COST, cost);
        contentValues.put(TABLE_REWARDS_UNLIMITED, unlimited);
        contentValues.put(TABLE_REWARDS_STYLE_COLOR, colorStyle);
        db.update(TABLE_REWARDS, contentValues, TABLE_ID + "= " + id, null);
        Log.d(TAG, "addData: Adding " + description + " to " + TABLE_REWARDS);
        db.close();
    }

    public Long addReward(String title, String description, Integer cost, String creationDate, Boolean unlimited, String colorStyle) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_REWARDS_TITLE, title);
        contentValues.put(TABLE_REWARDS_DESCRIPTION, description);
        contentValues.put(TABLE_REWARDS_CREATION_DATE, creationDate);
        contentValues.put(TABLE_REWARDS_COST, cost);
        contentValues.put(TABLE_REWARDS_UNLIMITED, unlimited);
        contentValues.put(TABLE_REWARDS_STYLE_COLOR, colorStyle);
        Log.d(TAG, "addData: Adding " + title + " to " + TABLE_REWARDS);
        Long result = db.insert(TABLE_REWARDS, null, contentValues);
        db.close();
        return result;
    }

    public Rewards getReward(Integer id) {
        Rewards rewards = null;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_REWARDS + " WHERE " + TABLE_ID + " = '" + id + "' ";
        try {
            Cursor c = db.rawQuery(query, null);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        rewards = new Rewards(c.getInt(c.getColumnIndex(TABLE_ID)),
                                c.getString(c.getColumnIndex(TABLE_REWARDS_TITLE)),
                                c.getString(c.getColumnIndex(TABLE_REWARDS_DESCRIPTION)),
                                c.getInt(c.getColumnIndex(TABLE_REWARDS_COST)),
                                c.getInt(c.getColumnIndex(TABLE_REWARDS_UNLIMITED)) > 0,
                                c.getString(c.getColumnIndex(TABLE_REWARDS_STYLE_COLOR))
                        );
                    } while (c.moveToNext());
                }
            }
        } catch (SQLiteException se) {
            Log.e(getClass().getSimpleName(), "Could not create or Open the database");
            db.close();
        }
        db.close();
        return rewards;
    }

    public GoalsAndTasks getTodo(Long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TASKS_GOALS + " WHERE " + TABLE_ID + " = '" + id + "' ";
        GoalsAndTasks goalsAndTasks = null;
        try {
            Cursor c = db.rawQuery(query, null);
            if (c != null) {
                if (c.moveToFirst()) {
                    Map<String, Boolean> improvementType = new HashMap<>();
                    improvementType.put(TABLE_TASKS_GOALS_HEALTH_EXERCISE, c.getInt(c.getColumnIndex(TABLE_TASKS_GOALS_HEALTH_EXERCISE)) > 0);
                    improvementType.put(TABLE_TASKS_GOALS_WORK, c.getInt(c.getColumnIndex(TABLE_TASKS_GOALS_WORK)) > 0);
                    improvementType.put(TABLE_TASKS_GOALS_SCHOOL, c.getInt(c.getColumnIndex(TABLE_TASKS_GOALS_SCHOOL)) > 0);
                    improvementType.put(TABLE_TASKS_GOALS_FAMILY_FRIENDS, c.getInt(c.getColumnIndex(TABLE_TASKS_GOALS_FAMILY_FRIENDS)) > 0);
                    improvementType.put(TABLE_TASKS_GOALS_LEARNING, c.getInt(c.getColumnIndex(TABLE_TASKS_GOALS_LEARNING)) > 0);
                    improvementType.put(TABLE_TASKS_GOALS_OTHER, c.getInt(c.getColumnIndex(TABLE_TASKS_GOALS_OTHER)) > 0);
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String deadlineDateString = c.getString(c.getColumnIndex(TABLE_TASKS_GOALS_DEADLINE_DATE));
                    String completionDateString = c.getString(c.getColumnIndex(TABLE_TASKS_GOALS_COMPLETION_DATE));
                    String creationDateString = c.getString(c.getColumnIndex(TABLE_TASKS_GOALS_CREATION_DATE));

                    Date creationDate = null;
                    if (creationDateString != null) {
                        try {
                            creationDate = formatter.parse(creationDateString);
                        } catch (ParseException e) {
                            Log.e(getClass().getSimpleName(), "Could not parse deadline date string");
                        }
                    }

                    Date deadlineDate = null;
                    if (deadlineDateString != null) {
                        try {
                            deadlineDate = formatter.parse(deadlineDateString);
                        } catch (ParseException e) {
                            Log.e(getClass().getSimpleName(), "Could not parse deadline date string");
                        }
                    }

                    Date completionDate = null;
                    if (completionDateString != null) {
                        try {
                            completionDate = formatter.parse(completionDateString);
                        } catch (ParseException e) {
                            Log.e(getClass().getSimpleName(), "Could not parse deadline date string");
                        }
                    }

                    goalsAndTasks = new GoalsAndTasks(
                            c.getString(c.getColumnIndex(TABLE_TASKS_GOALS_TITLE)),
                            c.getString(c.getColumnIndex(TABLE_TASKS_GOALS_DATA)),
                            c.getString(c.getColumnIndex(TABLE_TASKS_GOALS_CATEGORY)),
                            c.getLong(c.getColumnIndex(TABLE_ID)),
                            c.getInt(c.getColumnIndex(TABLE_TASKS_GOALS_SILVER)),
                            improvementType,
                            deadlineDate,
                            completionDate,
                            creationDate,
                            c.getInt(c.getColumnIndex(TABLE_TASKS_GOALS_COMPLETED)) > 0
                    );
                }
            }
        } catch (SQLiteException se) {
            Log.e(getClass().getSimpleName(), "Could not create or Open the database");
            db.close();
        }
        return goalsAndTasks;
    }

    public Integer resetExpiredGoalsAndTasks() {
        SQLiteDatabase db = this.getWritableDatabase();
        Calendar cal = Calendar.getInstance();
        Date today = new Date();
        DateFormat todayDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<GoalsAndTasks> expiredGoalsAndTasksArrayList = new ArrayList<GoalsAndTasks>();
        String query = "SELECT * FROM " + TABLE_TASKS_GOALS + " WHERE " + TABLE_TASKS_GOALS_DELETED + " = 0 "
                + "AND " + TABLE_TASKS_GOALS_CATEGORY + "<>'Daily' "
                + "ORDER BY " + TABLE_TASKS_GOALS_CREATION_DATE;
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
                        String completionDateString = c.getString(c.getColumnIndex(TABLE_TASKS_GOALS_COMPLETION_DATE));
                        String creationDateString = c.getString(c.getColumnIndex(TABLE_TASKS_GOALS_CREATION_DATE));
                        Date deadlineDate = null;
                        if (deadlineDateString != null) {
                            try {
                                deadlineDate = formatter.parse(deadlineDateString);
                            } catch (ParseException e) {
                                Log.e(getClass().getSimpleName(), "Could not parse deadline date string");
                            }
                        }

                        Date completionDate = null;
                        if (completionDateString != null) {
                            try {
                                completionDate = formatter.parse(completionDateString);
                            } catch (ParseException e) {
                                Log.e(getClass().getSimpleName(), "Could not parse deadline date string");
                            }
                        }

                        Date creationDate = null;
                        if (creationDateString != null) {
                            try {
                                creationDate = formatter.parse(creationDateString);
                            } catch (ParseException e) {
                                Log.e(getClass().getSimpleName(), "Could not parse deadline date string");
                            }
                        }

                        GoalsAndTasks goalsAndTasks = new GoalsAndTasks(
                                c.getString(c.getColumnIndex(TABLE_TASKS_GOALS_TITLE)),
                                c.getString(c.getColumnIndex(TABLE_TASKS_GOALS_DATA)),
                                c.getString(c.getColumnIndex(TABLE_TASKS_GOALS_CATEGORY)),
                                c.getLong(c.getColumnIndex(TABLE_ID)),
                                c.getInt(c.getColumnIndex(TABLE_TASKS_GOALS_SILVER)),
                                improvementType,
                                deadlineDate,
                                completionDate,
                                creationDate,
                                c.getInt(c.getColumnIndex(TABLE_TASKS_GOALS_COMPLETED)) > 0
                        );
                        expiredGoalsAndTasksArrayList.add(goalsAndTasks);
                    } while (c.moveToNext());
                }
            }
        } catch (SQLiteException se) {
            Log.e(getClass().getSimpleName(), "Could not create or Open the database");
            db.close();
        }
        Integer deletedCount = 0;
        for (GoalsAndTasks goalsAndTasks : expiredGoalsAndTasksArrayList) {
            Date todayDate = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = sdf.format(todayDate);
            if (goalsAndTasks.getCategory() == TodoType.DAILY) {
                if (goalsAndTasks.getCompletionDate() != null) {
                    if (getZeroTimeDate(todayDate).compareTo(getZeroTimeDate(goalsAndTasks.getCompletionDate())) > 0) {
                        deletedCount++;
                    }
                }
            }
            if (goalsAndTasks.getCategory() == TodoType.GOAL) {
                if (getZeroTimeDate(todayDate).compareTo(getZeroTimeDate(goalsAndTasks.getDeadlineDate())) > 0) {
                    deleteData(goalsAndTasks.getId(), false, currentTime, true);
                    deletedCount++;
                }
            } else if (getZeroTimeDate(todayDate).compareTo(getZeroTimeDate(goalsAndTasks.getCreationDate())) > 0) {
                deleteData(goalsAndTasks.getId(), false, currentTime, true);
                deletedCount++;
            }
        }
        db.close();
        return deletedCount;
    }

    private Date getZeroTimeDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        date = calendar.getTime();
        return date;
    }

    public ArrayList loadAllGoalsAndTask() {
        ArrayList<GoalsAndTasks> goalsAndTasksArrayList = new ArrayList<GoalsAndTasks>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TASKS_GOALS;
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
                        String completionDateString = c.getString(c.getColumnIndex(TABLE_TASKS_GOALS_COMPLETION_DATE));
                        String creationDateString = c.getString(c.getColumnIndex(TABLE_TASKS_GOALS_CREATION_DATE));

                        Date creationDate = null;
                        if (creationDateString != null) {
                            try {
                                creationDate = formatter.parse(creationDateString);
                            } catch (ParseException e) {
                                Log.e(getClass().getSimpleName(), "Could not parse deadline date string");
                            }
                        }


                        if (deadlineDateString != null) {
                            try {
                                deadlineDate = formatter.parse(deadlineDateString);
                            } catch (ParseException e) {
                                Log.e(getClass().getSimpleName(), "Could not parse deadline date string");
                            }
                        }

                        Date completionDate = null;
                        if (completionDateString != null) {
                            try {
                                completionDate = formatter.parse(completionDateString);
                            } catch (ParseException e) {
                                Log.e(getClass().getSimpleName(), "Could not parse deadline date string");
                            }
                        }

                        GoalsAndTasks goalsAndTasks = new GoalsAndTasks(
                                c.getString(c.getColumnIndex(TABLE_TASKS_GOALS_TITLE)),
                                c.getString(c.getColumnIndex(TABLE_TASKS_GOALS_DATA)),
                                c.getString(c.getColumnIndex(TABLE_TASKS_GOALS_CATEGORY)),
                                c.getLong(c.getColumnIndex(TABLE_ID)),
                                c.getInt(c.getColumnIndex(TABLE_TASKS_GOALS_SILVER)),
                                improvementType,
                                deadlineDate,
                                completionDate,
                                creationDate,
                                c.getInt(c.getColumnIndex(TABLE_TASKS_GOALS_COMPLETED)) > 0
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

    public ArrayList loadAllCompletedGoalsAndTask() {
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
                        String completionDateString = c.getString(c.getColumnIndex(TABLE_TASKS_GOALS_COMPLETION_DATE));
                        String creationDateString = c.getString(c.getColumnIndex(TABLE_TASKS_GOALS_CREATION_DATE));

                        Date creationDate = null;
                        if (creationDateString != null) {
                            try {
                                creationDate = formatter.parse(creationDateString);
                            } catch (ParseException e) {
                                Log.e(getClass().getSimpleName(), "Could not parse deadline date string");
                            }
                        }


                        if (deadlineDateString != null) {
                            try {
                                deadlineDate = formatter.parse(deadlineDateString);
                            } catch (ParseException e) {
                                Log.e(getClass().getSimpleName(), "Could not parse deadline date string");
                            }
                        }

                        Date completionDate = null;
                        if (completionDateString != null) {
                            try {
                                completionDate = formatter.parse(completionDateString);
                            } catch (ParseException e) {
                                Log.e(getClass().getSimpleName(), "Could not parse deadline date string");
                            }
                        }

                        GoalsAndTasks goalsAndTasks = new GoalsAndTasks(
                                c.getString(c.getColumnIndex(TABLE_TASKS_GOALS_TITLE)),
                                c.getString(c.getColumnIndex(TABLE_TASKS_GOALS_DATA)),
                                c.getString(c.getColumnIndex(TABLE_TASKS_GOALS_CATEGORY)),
                                c.getLong(c.getColumnIndex(TABLE_ID)),
                                c.getInt(c.getColumnIndex(TABLE_TASKS_GOALS_SILVER)),
                                improvementType,
                                deadlineDate,
                                completionDate,
                                creationDate,
                                c.getInt(c.getColumnIndex(TABLE_TASKS_GOALS_COMPLETED)) > 0
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

    public ArrayList loadAllRewards() {
        ArrayList<Rewards> rewardsArrayList = new ArrayList<Rewards>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_REWARDS + " WHERE " + TABLE_REWARDS_PURCHASED + " = 0 ";
        try {
            Cursor c = db.rawQuery(query, null);
            if (c != null) {
                if (c.moveToFirst()) {
                    do {
                        Rewards reward = new Rewards(
                                c.getInt(c.getColumnIndex(TABLE_ID)),
                                c.getString(c.getColumnIndex(TABLE_REWARDS_TITLE)),
                                c.getString(c.getColumnIndex(TABLE_REWARDS_DESCRIPTION)),
                                c.getInt(c.getColumnIndex(TABLE_REWARDS_COST)),
                                c.getInt(c.getColumnIndex(TABLE_REWARDS_UNLIMITED)) > 0,
                                c.getString(c.getColumnIndex(TABLE_REWARDS_STYLE_COLOR))
                        );
                        rewardsArrayList.add(reward);
                    } while (c.moveToNext());
                }
            }
        } catch (SQLiteException se) {
            Log.e(getClass().getSimpleName(), "Could not create or Open the database");
            db.close();
        }
        db.close();
        return rewardsArrayList;
    }

    public void purchaseReward(Integer id, String purchaseDate, Integer totalSilver, boolean purchased, boolean unlimited) {
        if (!unlimited) {
            SQLiteDatabase database = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(TABLE_REWARDS_PURCHASED, purchased);
            cv.put(TABLE_REWARDS_PURCHASE_DATE, purchaseDate);
            database.update(TABLE_REWARDS, cv, TABLE_ID + "= " + id, null);
            database.close();
        }
        updateValue(SILVER_AMOUNT_TOTAL, Integer.toString(totalSilver));
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

    public boolean deleteRewardPermanent(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_REWARDS, TABLE_ID + " = " + id, null);
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
        try {
            Cursor value = db.rawQuery(query, null);
            String values;
            if (value.moveToFirst()) {
                values = value.getString(2);
            } else {
                values = null;
            }
            db.close();
            return values;
        } catch (Exception e) {
            return "";
        }
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
        improvementTypesLevel.put("Health", getImprovementValue(USER_HEALTH_EXERCISE, dataRange, dayOfWeek));
        improvementTypesLevel.put("Work", getImprovementValue(USER_WORK, dataRange, dayOfWeek));
        improvementTypesLevel.put("School", getImprovementValue(USER_SCHOOL, dataRange, dayOfWeek));
        improvementTypesLevel.put("Family", getImprovementValue(USER_FAMILY_FRIENDS, dataRange, dayOfWeek));
        improvementTypesLevel.put("Learn", getImprovementValue(USER_LEARNING, dataRange, dayOfWeek));
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
                        "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"
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
                if (Integer.valueOf(namesOfDays[i]) % 6 == 0 || Integer.valueOf(namesOfDays[i]) == 23) {
                    if (Integer.valueOf(namesOfDays[i]) > 12) {
                        String key = Integer.valueOf(namesOfDays[i]) - 12 + "pm";
                        dataSetTotalSilver.addPoint(key, graphData.get(namesOfDays[i]).getSilverAmount());
                        dataSetCompletedTaskGoals.addPoint(key, graphData.get(namesOfDays[i]).getCompletedAmount());
                    } else {
                        String key;
                        if (Integer.valueOf(namesOfDays[i]) == 12) {
                            key = namesOfDays[i] + "pm";
                        }
                        else if(Integer.valueOf(namesOfDays[i]) == 0) {
                            key = "12am";
                        }
                        else {
                            key = namesOfDays[i] + "am";
                        }
                        dataSetTotalSilver.addPoint(key, graphData.get(namesOfDays[i]).getSilverAmount());
                        dataSetCompletedTaskGoals.addPoint(key, graphData.get(namesOfDays[i]).getCompletedAmount());
                    }
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

    public void updateDailyNotCompleted(Long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_TASKS_GOALS_COMPLETED, 0);
        db.update(TABLE_TASKS_GOALS, contentValues, TABLE_ID + " = " + "'" + id + "'", null);
    }

    public void insertProfilePicture(Bitmap profilePicture) {
        SQLiteDatabase db = this.getWritableDatabase();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        profilePicture.compress(Bitmap.CompressFormat.PNG, 50, stream);
        byte[] profilePictureByte = stream.toByteArray();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_VALUES, profilePictureByte);
        db.update(TABLE_KEY_VALUES, contentValues, TABLE_KEY + " = " + "'" + USER_PROFILE_PICTURE + "'", null);
    }

    public Bitmap getProfilePicture() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_KEY_VALUES + " WHERE " + TABLE_KEY + " = " + "'" + USER_PROFILE_PICTURE + "'";
        Cursor cur = db.rawQuery(query, null);
        byte[] imgByte = null;
        if (cur != null) {
            if (cur.moveToFirst()) {
                try {
                    imgByte = cur.getBlob(2);
                } catch (Exception e) {
                    return null;
                }
                cur.close();
                return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
            }
        }
        if (cur != null && !cur.isClosed()) {
            cur.close();
        }
        return null;
    }

    public void importDatabase(String databasePath) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(databasePath, null, 0);
        String query = "SELECT * FROM " + TABLE_TASKS_GOALS;
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
                        String deadlineDateString = c.getString(c.getColumnIndex(TABLE_TASKS_GOALS_DEADLINE_DATE));
                        String completionDateString = c.getString(c.getColumnIndex(TABLE_TASKS_GOALS_COMPLETION_DATE));
                        String creationDateString = c.getString(c.getColumnIndex(TABLE_TASKS_GOALS_CREATION_DATE));
                        addDataImport(
                                c.getString(c.getColumnIndex(TABLE_TASKS_GOALS_DATA)),
                                c.getString(c.getColumnIndex(TABLE_TASKS_GOALS_CATEGORY)),
                                c.getString(c.getColumnIndex(TABLE_TASKS_GOALS_TITLE)),
                                c.getInt(c.getColumnIndex(TABLE_TASKS_GOALS_SILVER)),
                                improvementType,
                                deadlineDateString,
                                completionDateString,
                                creationDateString,
                                c.getInt(c.getColumnIndex(TABLE_TASKS_GOALS_COMPLETED)) > 0,
                                c.getInt(c.getColumnIndex(TABLE_TASKS_GOALS_DELETED)) > 0
                        );

                    } while (c.moveToNext());
                }
            }
        } catch (SQLiteException se) {
            Log.e(getClass().getSimpleName(), "Could not create or Open the database");
            db.close();
        }

        try {
            String queryKeyValues = "SELECT * FROM " + TABLE_KEY_VALUES;
            Cursor cKeyValues = db.rawQuery(queryKeyValues, null);
            if (cKeyValues != null) {
                if (cKeyValues.moveToFirst()) {
                    do {
                        updateValue(
                                cKeyValues.getString(cKeyValues.getColumnIndex(TABLE_KEY)),
                                cKeyValues.getString(cKeyValues.getColumnIndex(TABLE_VALUES)));
                    } while (cKeyValues.moveToNext());
                }
            }
        } catch (SQLiteException se) {
            Log.e(getClass().getSimpleName(), "Could not create or Open the database");
            db.close();
        }
        db.close();
    }

    private void addDataImport(String description,
                               String category,
                               String title,
                               Integer silver,
                               Map<String, Boolean> improvementType,
                               String deadlineDate,
                               String creationDate,
                               String completionDate,
                               Boolean deleted,
                               Boolean completed) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TABLE_TASKS_GOALS_TITLE, title);
        contentValues.put(TABLE_TASKS_GOALS_DATA, description);
        contentValues.put(TABLE_TASKS_GOALS_CATEGORY, category);
        contentValues.put(TABLE_TASKS_GOALS_CREATION_DATE, creationDate);
        contentValues.put(TABLE_TASKS_GOALS_DEADLINE_DATE, deadlineDate);
        contentValues.put(TABLE_TASKS_GOALS_SILVER, silver);
        contentValues.put(TABLE_TASKS_GOALS_HEALTH_EXERCISE, improvementType.get(TABLE_TASKS_GOALS_HEALTH_EXERCISE));
        contentValues.put(TABLE_TASKS_GOALS_WORK, improvementType.get(TABLE_TASKS_GOALS_WORK));
        contentValues.put(TABLE_TASKS_GOALS_SCHOOL, improvementType.get(TABLE_TASKS_GOALS_SCHOOL));
        contentValues.put(TABLE_TASKS_GOALS_FAMILY_FRIENDS, improvementType.get(TABLE_TASKS_GOALS_FAMILY_FRIENDS));
        contentValues.put(TABLE_TASKS_GOALS_LEARNING, improvementType.get(TABLE_TASKS_GOALS_LEARNING));
        contentValues.put(TABLE_TASKS_GOALS_OTHER, improvementType.get(TABLE_TASKS_GOALS_OTHER));
        contentValues.put(TABLE_TASKS_GOALS_COMPLETED, completed);
        contentValues.put(TABLE_TASKS_GOALS_DELETED, deleted);
        contentValues.put(TABLE_TASKS_GOALS_COMPLETION_DATE, completionDate);
        Log.d(TAG, "addData: Adding " + description + " to " + TABLE_TASKS_GOALS + " under category " + category);
        Long result = db.insert(TABLE_TASKS_GOALS, null, contentValues);
        db.close();
    }

    public boolean existingKey(String key) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "Select * from " + TABLE_KEY_VALUES + " where " + TABLE_KEY + " = " + "'" + key + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
}
