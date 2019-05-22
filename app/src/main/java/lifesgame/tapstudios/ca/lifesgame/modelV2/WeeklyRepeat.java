package lifesgame.tapstudios.ca.lifesgame.modelV2;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by viditsoni on 2019-03-06.
 */

@Entity
public class WeeklyRepeat {

    @PrimaryKey(autoGenerate = true)
    public long weeklyRepeatId;

    @ForeignKey(onDelete = CASCADE, entity = DailyTodo.class, parentColumns = "DailyId" , childColumns = "DailyId")
    public long dailyId;

    public boolean monday;
    public boolean tuesday;
    public boolean wednesday;
    public boolean thursday;
    public boolean friday;
    public boolean saturday;
    public boolean sunday;
}
