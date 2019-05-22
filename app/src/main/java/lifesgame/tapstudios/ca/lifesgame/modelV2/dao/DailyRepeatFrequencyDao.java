package lifesgame.tapstudios.ca.lifesgame.modelV2.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import lifesgame.tapstudios.ca.lifesgame.modelV2.DailyRepeatFrequency;
import lifesgame.tapstudios.ca.lifesgame.modelV2.TaskTodo;

/**
 * Created by viditsoni on 2019-03-06.
 */

@Dao
public interface DailyRepeatFrequencyDao {


    @Query("SELECT * FROM DailyRepeatFrequency")
    public DailyRepeatFrequency loadAllDailyRepeatFrequency();
}
