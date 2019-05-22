package lifesgame.tapstudios.ca.lifesgame;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import lifesgame.tapstudios.ca.lifesgame.modelV2.DailyRepeat;
import lifesgame.tapstudios.ca.lifesgame.modelV2.DailyRepeatFrequency;
import lifesgame.tapstudios.ca.lifesgame.modelV2.DailyStatistic;
import lifesgame.tapstudios.ca.lifesgame.modelV2.DailyTodo;
import lifesgame.tapstudios.ca.lifesgame.modelV2.HabitStatistic;
import lifesgame.tapstudios.ca.lifesgame.modelV2.HabitTodo;
import lifesgame.tapstudios.ca.lifesgame.modelV2.MonthlyRepeat;
import lifesgame.tapstudios.ca.lifesgame.modelV2.Reward;
import lifesgame.tapstudios.ca.lifesgame.modelV2.TaskStatistic;
import lifesgame.tapstudios.ca.lifesgame.modelV2.TaskSublistItem;
import lifesgame.tapstudios.ca.lifesgame.modelV2.TaskTodo;
import lifesgame.tapstudios.ca.lifesgame.modelV2.TodoTag;
import lifesgame.tapstudios.ca.lifesgame.modelV2.WeeklyRepeat;
import lifesgame.tapstudios.ca.lifesgame.modelV2.YearlyRepeat;
import lifesgame.tapstudios.ca.lifesgame.modelV2.dao.DailyDao;
import lifesgame.tapstudios.ca.lifesgame.modelV2.dao.DailyRepeatDao;
import lifesgame.tapstudios.ca.lifesgame.modelV2.dao.DailyRepeatFrequencyDao;
import lifesgame.tapstudios.ca.lifesgame.modelV2.dao.DailyStatisticDao;
import lifesgame.tapstudios.ca.lifesgame.modelV2.dao.HabitDao;
import lifesgame.tapstudios.ca.lifesgame.modelV2.dao.HabitStatisticDao;
import lifesgame.tapstudios.ca.lifesgame.modelV2.dao.MonthlyRepeatDao;
import lifesgame.tapstudios.ca.lifesgame.modelV2.dao.RewardDao;
import lifesgame.tapstudios.ca.lifesgame.modelV2.dao.TagDao;
import lifesgame.tapstudios.ca.lifesgame.modelV2.dao.TaskDao;
import lifesgame.tapstudios.ca.lifesgame.modelV2.dao.TaskStatisticDao;
import lifesgame.tapstudios.ca.lifesgame.modelV2.dao.TaskSublistDao;
import lifesgame.tapstudios.ca.lifesgame.modelV2.dao.WeeklyRepeatDao;
import lifesgame.tapstudios.ca.lifesgame.modelV2.dao.YearlyRepeatDao;

/**
 * Created by viditsoni on 2019-03-06.
 */

@Database(entities = {
        DailyRepeat.class,
        DailyRepeatFrequency.class,
        DailyStatistic.class,
        DailyTodo.class,
        HabitStatistic.class,
        HabitTodo.class,
        MonthlyRepeat.class,
        Reward.class,
        TaskStatistic.class,
        TaskSublistItem.class,
        TaskTodo.class,
        TodoTag.class,
        WeeklyRepeat.class,
        YearlyRepeat.class
}, version = 1, exportSchema = false)
public abstract class LifesGameDatabase extends RoomDatabase {
    public abstract DailyDao dailyDao();
    public abstract DailyRepeatDao dailyRepeatDao();
    public abstract DailyRepeatFrequencyDao dailyRepeatFrequencyDao();
    public abstract DailyStatisticDao dailyStatisticDao();
    public abstract HabitDao habitDao();
    public abstract HabitStatisticDao habitStatisticDao();
    public abstract MonthlyRepeatDao monthlyRepeatDao();
    public abstract RewardDao rewardDao();
    public abstract TagDao tagDao();
    public abstract TaskDao taskDao();
    public abstract TaskStatisticDao taskStatisticDao();
    public abstract TaskSublistDao taskSublistDao();
    public abstract WeeklyRepeatDao weeklyRepeatDao();
    public abstract YearlyRepeatDao yearlyRepeatDao();

    private static volatile LifesGameDatabase DB_INSTANCE;

    public static LifesGameDatabase getAppDatabase(Context context) {
        if (DB_INSTANCE == null) {
            synchronized (LifesGameDatabase.class) {
                if (DB_INSTANCE == null) {
                    DB_INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            LifesGameDatabase.class, "lifes_game_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return DB_INSTANCE;
    }

    public static void destroyInstance() {
        DB_INSTANCE = null;
    }
}
