package lifesgame.tapstudios.ca.lifesgame.modelV2;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;

import org.joda.time.DateTime;

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
public class DailyTodo implements Todo {

    @PrimaryKey(autoGenerate = true)
    public long dailyId;

    public String title;
    public String description;
    public long silver;

    //Assign a foreign key
    @ForeignKey(onDelete = CASCADE, entity = Class.class, parentColumns = "TagId" , childColumns = "TagId")
    public long tagId;

    @ForeignKey(onDelete = CASCADE, entity = TodoTypes.class, parentColumns = "TodoType" , childColumns = "TodoType")
    public TodoTypes.TodoType todoType;

    //TODO Make enum
    public String repeatFrequency;

    public Date reminderDateTime;

    public boolean ActionTaken;

    public DailyTodo() {}

    @Ignore
    public DailyTodo(long dailyId, String title, String description, long silver, long tagId, TodoTypes.TodoType todoType, String repeatFrequency, Date reminderDateTime, boolean actionTaken) {
        this.dailyId = dailyId;
        this.title = title;
        this.description = description;
        this.silver = silver;
        this.tagId = tagId;
        this.todoType = todoType;
        this.repeatFrequency = repeatFrequency;
        this.reminderDateTime = reminderDateTime;
        ActionTaken = actionTaken;
    }

    public long getId() {
        return dailyId;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public long getSilver() {
        return silver;
    }

    @Override
    public long getTagId() {
        return tagId;
    }

    @Override
    public TodoTypes.TodoType getTodoType() {
        return todoType;
    }

    public String getRepeatFrequency() {
        return repeatFrequency;
    }

    public Date getReminderDateTime() {
        return reminderDateTime;
    }

    public boolean isActionTaken() {
        return ActionTaken;
    }
}
