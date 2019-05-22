package lifesgame.tapstudios.ca.lifesgame.modelV2.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import lifesgame.tapstudios.ca.lifesgame.modelV2.DailyStatistic;
import lifesgame.tapstudios.ca.lifesgame.modelV2.TaskStatistic;

/**
 * Created by viditsoni on 2019-03-06.
 */

@Dao
public interface DailyStatisticDao {

    @Insert
    public long insertDailyStatistic(DailyStatistic dailyStatistic);

    @Insert
    public void deleteDailyStatistic(DailyStatistic dailyStatistic);

    @Query("SELECT * FROM DailyStatistic WHERE DailyStatisticId = :dailyStatisticId")
    public DailyStatistic loadDailyStatisticById(int dailyStatisticId);

    @Query("SELECT * FROM DailyStatistic WHERE DailyId = :dailyId")
    public DailyStatistic loadDailyStatisticByDailyId(int dailyId);
}
