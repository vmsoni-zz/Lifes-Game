package lifesgame.tapstudios.ca.lifesgame.modelV2.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import lifesgame.tapstudios.ca.lifesgame.modelV2.JoinModel.TaskTodoAndTag;
import lifesgame.tapstudios.ca.lifesgame.modelV2.TaskTodo;

/**
 * Created by viditsoni on 2019-02-01.
 */

@Dao
public interface TaskDao {

    @Insert
    public long insertTaskTodo(TaskTodo taskTodo);

    @Delete
    public void deleteTaskTodo(TaskTodo taskTodo);

    @Query("SELECT * FROM TaskTodo WHERE TaskId = :taskId")
    public LiveData<TaskTodo> loadTaskTodoById(long taskId);

    @Query("SELECT * FROM TaskTodo INNER JOIN TodoTag on (TagId = tag_TagId) WHERE NOT ActionTaken")
    public LiveData<List<TaskTodoAndTag>> loadAllTaskTodo();
}
