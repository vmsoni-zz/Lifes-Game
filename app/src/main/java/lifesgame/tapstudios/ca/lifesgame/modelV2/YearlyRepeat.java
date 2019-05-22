package lifesgame.tapstudios.ca.lifesgame.modelV2;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by viditsoni on 2019-03-06.
 */

@Entity
public class YearlyRepeat {

    @PrimaryKey(autoGenerate = true)
    public long yearlyRepeatId;

    @ForeignKey(entity = DailyTodo.class, parentColumns = "DailyId" , childColumns = "DailyId")
    public long dailyId;

    public int repeatInterval;
}
