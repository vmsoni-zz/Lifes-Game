package lifesgame.tapstudios.ca.lifesgame.modelV2.JoinModel;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

import lifesgame.tapstudios.ca.lifesgame.modelV2.JoinModel.generic.TodoAndTag;
import lifesgame.tapstudios.ca.lifesgame.modelV2.TaskSublistItem;
import lifesgame.tapstudios.ca.lifesgame.modelV2.TaskTodo;
import lifesgame.tapstudios.ca.lifesgame.modelV2.TodoTag;


/**
 * Created by viditsoni on 2019-05-03.
 */

public class TaskTodoAndTag implements TodoAndTag{
    @Embedded
    public TaskTodo taskTodo;

    @Embedded
    public TodoTag tag;

    @Relation(parentColumn = "taskId", entityColumn = "sublistItem_ParentId", entity = TaskSublistItem.class)
    public List<TaskSublistItem> sublistItemList;

    @Override
    public TaskTodo getTodo() {
        return taskTodo;
    }

    @Override
    public TodoTag getTodoTag() {
        return tag;
    }
}
