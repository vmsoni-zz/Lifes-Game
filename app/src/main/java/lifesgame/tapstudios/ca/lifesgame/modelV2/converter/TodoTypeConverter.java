package lifesgame.tapstudios.ca.lifesgame.modelV2.converter;

import android.arch.persistence.room.TypeConverter;

import lifesgame.tapstudios.ca.lifesgame.modelV2.TodoTypes;

import static lifesgame.tapstudios.ca.lifesgame.modelV2.TodoTypes.TodoType.DAILY;
import static lifesgame.tapstudios.ca.lifesgame.modelV2.TodoTypes.TodoType.HABIT;
import static lifesgame.tapstudios.ca.lifesgame.modelV2.TodoTypes.TodoType.TASK;


/**
 * Created by viditsoni on 2019-02-20.
 */

public class TodoTypeConverter {
    @TypeConverter
    public static TodoTypes.TodoType todoTypeStringConverter(String todoType) {
        switch(todoType) {
            case "TASK":
                return TASK;
            case "DAILY":
                return DAILY;
            case "HABIT":
                return HABIT;
            default:
                throw new IllegalArgumentException("Could not recognize todoType string");
        }
    }

    @TypeConverter
    public static String todoTypeEnumConverter(TodoTypes.TodoType todoType) {
        switch(todoType) {
            case TASK:
                return "TASK";
            case DAILY:
                return "DAILY";
            case HABIT:
                return "HABIT";
            default:
                throw new IllegalArgumentException("Could not recognize todoType enum");
        }
    }
}
