package lifesgame.tapstudios.ca.lifesgame.modelV2.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import lifesgame.tapstudios.ca.lifesgame.modelV2.DailyStatistic;
import lifesgame.tapstudios.ca.lifesgame.modelV2.HabitStatistic;
import lifesgame.tapstudios.ca.lifesgame.modelV2.TaskStatistic;

/**
 * Created by viditsoni on 2019-03-06.
 */

@Dao
public interface HabitStatisticDao {

    @Insert
    public long insertHabitStatistic(HabitStatistic habitStatistic);

    @Insert
    public void deleteHabitStatistic(HabitStatistic habitStatistic);

    @Query("SELECT * FROM HabitStatistic WHERE HabitStatisticId = :habitStatisticId")
    public HabitStatistic loadHabitStatisticById(int habitStatisticId);

    @Query("SELECT * FROM HabitStatistic WHERE HabitId = :habitId")
    public HabitStatistic loadHabitStatisticByDailyId(int habitId);
}
