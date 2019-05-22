package lifesgame.tapstudios.ca.lifesgame.modelV2;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by viditsoni on 2019-03-06.
 */

@Entity
public class Reward {

    @PrimaryKey(autoGenerate = true)
    public long rewardId;

    public String title;
    public String description;
    public long cost;
    public long redeemCount;
    public boolean repeatingReward;
}
