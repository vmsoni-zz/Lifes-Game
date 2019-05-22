package lifesgame.tapstudios.ca.lifesgame.modelV2.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import lifesgame.tapstudios.ca.lifesgame.modelV2.HabitTodo;
import lifesgame.tapstudios.ca.lifesgame.modelV2.TaskTodo;

/**
 * Created by viditsoni on 2019-02-01.
 */

@Dao
public interface HabitDao {

    @Insert
    public long insertHabitTodo(HabitTodo habitTodo);

    @Delete
    public void deleteHabitTodo(HabitTodo habitTodo);

    @Query("SELECT * FROM HabitTodo WHERE HabitId = :habitId")
    public LiveData<HabitTodo> loadHabitTodoById(long habitId);

    @Query("SELECT * FROM HabitTodo")
    public LiveData<List<HabitTodo>> loadAllHabitTodo();
}
