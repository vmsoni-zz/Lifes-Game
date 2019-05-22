package lifesgame.tapstudios.ca.lifesgame.modelV2.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import lifesgame.tapstudios.ca.lifesgame.modelV2.MonthlyRepeat;
import lifesgame.tapstudios.ca.lifesgame.modelV2.TodoTag;
import lifesgame.tapstudios.ca.lifesgame.modelV2.YearlyRepeat;

/**
 * Created by viditsoni on 2019-03-06.
 */

@Dao
public interface YearlyRepeatDao {

    @Insert
    public long insertYearlyRepeat(YearlyRepeat yearlyRepeat);

    @Insert
    public void deleteYearlyRepeat(YearlyRepeat yearlyRepeat);

    @Query("SELECT * FROM YearlyRepeat WHERE YearlyRepeatId = :yearlyRepeatId")
    public YearlyRepeat loadYearlyRepeatById(int yearlyRepeatId);

    @Query("SELECT * FROM YearlyRepeat WHERE DailyId = :dailyId")
    public YearlyRepeat loadYearlyRepeatByDailyId(int dailyId);
}
