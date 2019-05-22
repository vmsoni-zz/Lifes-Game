package lifesgame.tapstudios.ca.lifesgame.modelV2.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import lifesgame.tapstudios.ca.lifesgame.modelV2.DailyTodo;
import lifesgame.tapstudios.ca.lifesgame.modelV2.HabitTodo;

/**
 * Created by viditsoni on 2019-02-01.
 */

@Dao
public interface DailyDao {

    @Insert
    public long insertDailyTodo(DailyTodo dailyTodo);

    @Delete
    public void deleteDailyTodo(DailyTodo dailyTodo);

    @Query("SELECT * FROM DailyTodo WHERE DailyId = :dailyId")
    public LiveData<DailyTodo> loadDailyTodoById(long dailyId);

    @Query("SELECT * FROM DailyTodo")
    public LiveData<List<DailyTodo>> loadAllDailyTodo();

}
