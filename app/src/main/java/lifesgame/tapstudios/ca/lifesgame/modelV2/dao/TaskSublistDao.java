package lifesgame.tapstudios.ca.lifesgame.modelV2.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import lifesgame.tapstudios.ca.lifesgame.modelV2.TaskSublistItem;

/**
 * Created by viditsoni on 2019-03-06.
 */

@Dao
public interface TaskSublistDao {

    @Insert
    public List<Long> insertTaskSublist(List<TaskSublistItem> taskSublist);

    @Insert
    public void deleteTaskSublist(List<TaskSublistItem> taskSublist);

    @Query("SELECT * FROM TaskSublistItem WHERE sublistItem_SublistId = :sublistId")
    public TaskSublistItem loadTaskSublistById(int sublistId);

    @Query("SELECT * FROM TaskSublistItem WHERE sublistItem_ParentId = :parentId")
    public TaskSublistItem loadTaskSublistByTaskId(int parentId);
}
