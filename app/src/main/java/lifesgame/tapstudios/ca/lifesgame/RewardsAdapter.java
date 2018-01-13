package lifesgame.tapstudios.ca.lifesgame;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import lifesgame.tapstudios.ca.lifesgame.helper.DatabaseHelper;
import lifesgame.tapstudios.ca.lifesgame.model.Rewards;

/**
 * Created by viditsoni on 2017-12-28.
 */

public class RewardsAdapter extends RecyclerView.Adapter<RewardsHolder> {
    public List<Rewards> rewardsList;
    public LayoutInflater inflater;
    public DatabaseHelper databaseHelper;
    private int resource;
    private Context context;

    public RewardsAdapter(Context context,
                                int resource,
                                DatabaseHelper databaseHelper,
                                List<Rewards> rewardsList) {
        this.context = context;
        this.resource = resource;
        this.databaseHelper = databaseHelper;
        this.rewardsList = rewardsList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RewardsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new RewardsHolder(context, view, this);
    }

    @Override
    public void onBindViewHolder(RewardsHolder holder, int position) {
        Rewards rewards = this.rewardsList.get(position);
        holder.bindRewards(rewards, position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return rewardsList.size();
    }

    public void removeItem(int position) {
        rewardsList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, rewardsList.size());
    }

    public void deleteRewardPermanent(int position) {
        databaseHelper.deleteRewardPermanent(rewardsList.get(position).getId());
        rewardsList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, rewardsList.size());
    }
}
