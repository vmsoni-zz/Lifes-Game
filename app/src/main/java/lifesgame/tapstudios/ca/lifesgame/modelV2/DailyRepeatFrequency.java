package lifesgame.tapstudios.ca.lifesgame.modelV2;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import lifesgame.tapstudios.ca.lifesgame.modelV2.converter.DailyRepeatFrequencyConverter;

/**
 * Created by viditsoni on 2019-03-06.
 */

@Entity
@TypeConverters(DailyRepeatFrequencyConverter.class)
public class DailyRepeatFrequency {

    @PrimaryKey
    @NonNull
    public RepeatFrequency repeatFrequency;

    public enum RepeatFrequency {
        DAILY,
        WEEKLY,
        MONTHLY,
        YEARLY
    }
}
