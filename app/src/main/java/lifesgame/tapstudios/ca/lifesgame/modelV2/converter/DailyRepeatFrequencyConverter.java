package lifesgame.tapstudios.ca.lifesgame.modelV2.converter;

import android.arch.persistence.room.TypeConverter;

import lifesgame.tapstudios.ca.lifesgame.modelV2.DailyRepeatFrequency;

import static lifesgame.tapstudios.ca.lifesgame.modelV2.DailyRepeatFrequency.RepeatFrequency.DAILY;
import static lifesgame.tapstudios.ca.lifesgame.modelV2.DailyRepeatFrequency.RepeatFrequency.MONTHLY;
import static lifesgame.tapstudios.ca.lifesgame.modelV2.DailyRepeatFrequency.RepeatFrequency.WEEKLY;
import static lifesgame.tapstudios.ca.lifesgame.modelV2.DailyRepeatFrequency.RepeatFrequency.YEARLY;

/**
 * Created by viditsoni on 2019-03-06.
 */

public class DailyRepeatFrequencyConverter {

    @TypeConverter
    public static DailyRepeatFrequency.RepeatFrequency repeatFrequencyStringConverter(String repeatFrequency) {
        switch(repeatFrequency) {
            case "DAILY":
                return DAILY;
            case "WEEKLY":
                return WEEKLY;
            case "MONTHLY":
                return MONTHLY;
            case "YEARLY":
                return YEARLY;
            default:
                throw new IllegalArgumentException("Could not recognize RepeatFrequency string");
        }
    }

    @TypeConverter
    public static String repeatFrequencyEnumConverter(DailyRepeatFrequency.RepeatFrequency repeatFrequency) {
        switch(repeatFrequency) {
            case DAILY:
                return "Daily";
            case WEEKLY:
                return "WEEKLY";
            case MONTHLY:
                return "MONTHLY";
            case YEARLY:
                return "YEARLY";
            default:
                throw new IllegalArgumentException("Could not recognize RepeatFrequency enum");
        }
    }
}
