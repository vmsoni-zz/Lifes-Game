package lifesgame.tapstudios.ca.lifesgame.modelV2.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import lifesgame.tapstudios.ca.lifesgame.modelV2.Reward;

/**
 * Created by viditsoni on 2019-03-06.
 */

@Dao
public interface RewardDao {

    @Insert
    public long insertReward(Reward reward);

    @Insert
    public void deleteReward(Reward reward);

    @Query("SELECT * FROM Reward WHERE RewardId = :rewardId")
    public Reward loadRewardById(int rewardId);
}
