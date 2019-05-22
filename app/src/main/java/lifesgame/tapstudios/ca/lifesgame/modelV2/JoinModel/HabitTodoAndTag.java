package lifesgame.tapstudios.ca.lifesgame.modelV2.JoinModel;

import android.arch.persistence.room.Embedded;

import lifesgame.tapstudios.ca.lifesgame.modelV2.HabitTodo;
import lifesgame.tapstudios.ca.lifesgame.modelV2.JoinModel.generic.TodoAndTag;
import lifesgame.tapstudios.ca.lifesgame.modelV2.TaskTodo;
import lifesgame.tapstudios.ca.lifesgame.modelV2.TodoTag;


/**
 * Created by viditsoni on 2019-05-03.
 */

public class HabitTodoAndTag implements TodoAndTag{
    @Embedded
    public HabitTodo habitTodo;

    @Embedded(prefix = "tag_")
    public TodoTag tag;

    @Override
    public HabitTodo getTodo() {
        return getTodo();
    }

    @Override
    public TodoTag getTodoTag() {
        return tag;
    }
}
