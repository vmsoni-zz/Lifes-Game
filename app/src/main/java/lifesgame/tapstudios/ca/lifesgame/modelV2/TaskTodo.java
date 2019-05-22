package lifesgame.tapstudios.ca.lifesgame.modelV2;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;

import lifesgame.tapstudios.ca.lifesgame.modelV2.converter.DateTypeConverter;
import lifesgame.tapstudios.ca.lifesgame.modelV2.converter.TodoTypeConverter;
import lifesgame.tapstudios.ca.lifesgame.modelV2.generic.Todo;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by viditsoni on 2019-02-01.
 */
@Entity
@TypeConverters({DateTypeConverter.class, TodoTypeConverter.class})
public class TaskTodo implements Todo {

    @PrimaryKey(autoGenerate = true)
    public long taskId;

    public String title;
    public String description;
    public long silver;

    //Assign a foreign key
    @ForeignKey(onDelete = CASCADE, entity = TodoTag.class, parentColumns = "TagId" , childColumns = "TagId")
    public long tagId;

    @ForeignKey(onDelete = CASCADE, entity = TodoTypes.class, parentColumns = "TodoType" , childColumns = "TodoType")
    public TodoTypes.TodoType todoType;

    public boolean sublist;
    //TODO: Make typeconverters for date
    public Date dueDate;
    public Date reminderDateTime;
    public boolean actionTaken = false;

    public TaskTodo() {}

    @Ignore
    public TaskTodo(String title,
                    String description,
                    long silver,
                    long tagId,
                    boolean sublist,
                    Date dueDate,
                    Date reminderDateTime,
                    TodoTypes.TodoType todoType) {
        this.title = title;
        this.description = description;
        this.silver = silver;
        this.tagId = tagId;
        this.sublist = sublist;
        this.dueDate = dueDate;
        this.reminderDateTime = reminderDateTime;
        this.todoType = todoType;
    }

    @Override
    public long getId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public long getSilver() {
        return silver;
    }

    public void setSilver(long silver) {
        this.silver = silver;
    }

    @Override
    public long getTagId() {
        return tagId;
    }

    public void setTagId(long tagId) {
        this.tagId = tagId;
    }

    public boolean isSublist() {
        return sublist;
    }

    public void setSublist(boolean sublist) {
        this.sublist = sublist;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getReminderDateTime() {
        return reminderDateTime;
    }

    public void setReminderDateTime(Date reminderDateTime) {
        this.reminderDateTime = reminderDateTime;
    }

    @Override
    public TodoTypes.TodoType getTodoType() {
        return todoType;
    }

    public boolean isActionTaken() {
        return actionTaken;
    }

    public void setActionTaken(boolean actionTaken) {
        this.actionTaken = actionTaken;
    }

}
