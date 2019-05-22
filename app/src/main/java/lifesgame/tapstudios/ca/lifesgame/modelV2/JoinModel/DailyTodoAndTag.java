package lifesgame.tapstudios.ca.lifesgame.modelV2.JoinModel;

import android.arch.persistence.room.Embedded;

import lifesgame.tapstudios.ca.lifesgame.modelV2.DailyTodo;
import lifesgame.tapstudios.ca.lifesgame.modelV2.JoinModel.generic.TodoAndTag;
import lifesgame.tapstudios.ca.lifesgame.modelV2.TaskTodo;
import lifesgame.tapstudios.ca.lifesgame.modelV2.TodoTag;


/**
 * Created by viditsoni on 2019-05-03.
 */

public class DailyTodoAndTag implements TodoAndTag {
    @Embedded
    public TaskTodo taskTodo;

    @Embedded(prefix = "tag_")
    public TodoTag tag;

    @Override
    public DailyTodo getTodo() {
        return getTodo();
    }

    @Override
    public TodoTag getTodoTag() {
        return tag;
    }
}
