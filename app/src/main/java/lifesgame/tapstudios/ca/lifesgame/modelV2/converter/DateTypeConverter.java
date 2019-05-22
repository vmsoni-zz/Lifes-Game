package lifesgame.tapstudios.ca.lifesgame.modelV2.converter;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by viditsoni on 2019-03-08.
 */

public class DateTypeConverter {

    @TypeConverter
    public static Date toDate(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long toLong(Date value) {
        return value == null ? null : value.getTime();
    }
}
