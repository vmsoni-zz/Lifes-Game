package lifesgame.tapstudios.ca.lifesgame.modelV2.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import lifesgame.tapstudios.ca.lifesgame.modelV2.HabitTodo;
import lifesgame.tapstudios.ca.lifesgame.modelV2.TodoTag;

/**
 * Created by viditsoni on 2019-03-06.
 */

@Dao
public interface TagDao {

    @Insert
    public long insertTodoTag(TodoTag todoTag);

    @Insert
    public void deleteTodoTag(HabitTodo habitTodo);

    @Query("SELECT * FROM TodoTag WHERE tag_TagId = :tagId")
    public TodoTag loadTodoTagById(long tagId);
}
