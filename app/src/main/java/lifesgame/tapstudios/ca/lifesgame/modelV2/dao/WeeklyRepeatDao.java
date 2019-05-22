package lifesgame.tapstudios.ca.lifesgame.modelV2.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import lifesgame.tapstudios.ca.lifesgame.modelV2.MonthlyRepeat;
import lifesgame.tapstudios.ca.lifesgame.modelV2.TodoTag;
import lifesgame.tapstudios.ca.lifesgame.modelV2.WeeklyRepeat;

/**
 * Created by viditsoni on 2019-03-06.
 */

@Dao
public interface WeeklyRepeatDao {

    @Insert
    public long insertWeeklyRepeat(WeeklyRepeat weeklyRepeat);

    @Insert
    public void deleteMonthlyRepeat(WeeklyRepeat weeklyRepeat);

    @Query("SELECT * FROM WeeklyRepeat WHERE WeeklyRepeatId = :weeklyRepeatId")
    public WeeklyRepeat loadWeeklyRepeatById(int weeklyRepeatId);

    @Query("SELECT * FROM WeeklyRepeat WHERE DailyId = :dailyId")
    public WeeklyRepeat loadWeeklyRepeatByDailyId(int dailyId);
}
