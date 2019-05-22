package lifesgame.tapstudios.ca.lifesgame.modelV2.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import lifesgame.tapstudios.ca.lifesgame.modelV2.TaskStatistic;
import lifesgame.tapstudios.ca.lifesgame.modelV2.WeeklyRepeat;

/**
 * Created by viditsoni on 2019-03-06.
 */

@Dao
public interface TaskStatisticDao {

    @Insert
    public long insertTaskStatistic(TaskStatistic taskStatistic);

    @Insert
    public void deleteTaskStatistic(TaskStatistic taskStatistic);

    @Query("SELECT * FROM TaskStatistic WHERE TaskStatisticId = :taskStatisticId")
    public TaskStatistic loadTaskStatisticById(int taskStatisticId);

    @Query("SELECT * FROM TaskStatistic WHERE TaskId = :taskId")
    public TaskStatistic loadTaskStatisticByTaskId(int taskId);
}
