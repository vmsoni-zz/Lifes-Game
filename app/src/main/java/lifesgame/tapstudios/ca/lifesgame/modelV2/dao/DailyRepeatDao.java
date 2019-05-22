package lifesgame.tapstudios.ca.lifesgame.modelV2.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import lifesgame.tapstudios.ca.lifesgame.modelV2.DailyRepeat;
import lifesgame.tapstudios.ca.lifesgame.modelV2.HabitTodo;
import lifesgame.tapstudios.ca.lifesgame.modelV2.TodoTag;

/**
 * Created by viditsoni on 2019-03-06.
 */

@Dao
public interface DailyRepeatDao {

    @Insert
    public long insertDailyRepeat(DailyRepeat dailyRepeat);

    @Insert
    public void deleteDailyRepeat(DailyRepeat dailyRepeat);

    @Query("SELECT * FROM DailyRepeat WHERE DailyRepeatId = :dailyRepeatId")
    public DailyRepeat loadDailyRepeatById(int dailyRepeatId);

    @Query("SELECT * FROM DailyRepeat WHERE DailyId = :dailyId")
    public DailyRepeat loadDailyRepeatByDailyId(int dailyId);
}
