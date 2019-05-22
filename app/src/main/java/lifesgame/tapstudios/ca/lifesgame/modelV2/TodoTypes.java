package lifesgame.tapstudios.ca.lifesgame.modelV2;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import lifesgame.tapstudios.ca.lifesgame.modelV2.converter.TodoTypeConverter;

/**
 * Created by viditsoni on 2019-02-20.
 */

@Entity
@TypeConverters(TodoTypeConverter.class)
public class TodoTypes {

    @PrimaryKey
    @NonNull
    public TodoType todoType;

    public enum TodoType {
        TASK,
        HABIT,
        DAILY
    }
}
