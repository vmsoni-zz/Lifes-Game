package lifesgame.tapstudios.ca.lifesgame.modelV2.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import lifesgame.tapstudios.ca.lifesgame.modelV2.DailyRepeat;
import lifesgame.tapstudios.ca.lifesgame.modelV2.MonthlyRepeat;
import lifesgame.tapstudios.ca.lifesgame.modelV2.TodoTag;

/**
 * Created by viditsoni on 2019-03-06.
 */

@Dao
public interface MonthlyRepeatDao {

    @Insert
    public long insertMonthlyRepeat(MonthlyRepeat monthlyRepeat);

    @Insert
    public void deleteMonthlyRepeat(MonthlyRepeat monthlyRepeat);

    @Query("SELECT * FROM MonthlyRepeat WHERE MonthlyRepeatId = :monthlyRepeatId")
    public MonthlyRepeat loadMonthlyRepeatById(int monthlyRepeatId);

    @Query("SELECT * FROM MonthlyRepeat WHERE DailyId = :dailyId")
    public MonthlyRepeat loadMonthlyRepeatByDailyId(int dailyId);
}
