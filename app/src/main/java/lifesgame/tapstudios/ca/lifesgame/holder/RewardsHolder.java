package lifesgame.tapstudios.ca.lifesgame.holder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import lifesgame.tapstudios.ca.lifesgame.R;
import lifesgame.tapstudios.ca.lifesgame.activity.DialogAddRewards;
import lifesgame.tapstudios.ca.lifesgame.adapter.RewardsAdapter;
import lifesgame.tapstudios.ca.lifesgame.helper.PurchaseHelper;
import lifesgame.tapstudios.ca.lifesgame.model.Rewards;

/**
 * Created by viditsoni on 2017-12-28.
 */

public class RewardsHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.editButton) ImageButton editBtn;
    @BindView(R.id.deleteButton) ImageButton deleteBtn;
    @BindView(R.id.reward_title) TextView title;
    @BindView(R.id.reward_description) TextView description;
    @BindView(R.id.reward_cost) TextView cost;
    @BindView(R.id.purchase_reward) LinearLayout lLPurchaseReward;
    @BindView(R.id.infinity_Ll) LinearLayout infinityLl;

    private Context context;
    private RewardsAdapter rewardsAdapter;
    private PurchaseHelper purchaseHelper;

    public RewardsHolder(Context context,
                         View itemView,
                         RewardsAdapter rewardsAdapter) {
        super(itemView);
        this.purchaseHelper = new PurchaseHelper(context);
        this.context = context;
        this.rewardsAdapter = rewardsAdapter;
        ButterKnife.bind(this,  itemView);
    }

    public void bindRewards(Rewards rewards, int position) {
        title.setText(rewards.getTitle());
        description.setText(rewards.getDescription());
        cost.setText(String.valueOf(rewards.getCost()));
        if (rewards.getUnlimitedConsumption()) {
            infinityLl.setVisibility(View.VISIBLE);
        }
        setupRewardsColors(rewards);
        this.initializeOnClickListeners(rewards, position);
    }

    public void initializeOnClickListeners(final Rewards reward, final int position) {
        lLPurchaseReward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (purchaseHelper.purchaseReward(reward)) {
                    if (!reward.getUnlimitedConsumption()) {
                        rewardsAdapter.removeItem(position);
                    }
                    Toast.makeText(context, "Redeemed Reward: " + reward.getTitle(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Not Enough Silver!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DialogAddRewards.class);
                intent.putExtra("ID", reward.getId());
                context.startActivity(intent);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rewardsAdapter.deleteRewardPermanent(position);
            }
        });
    }

    private void setupRewardsColors(Rewards rewards) {
        switch (rewards.getStyleColor()) {
            case "blue":
                title.setTextColor(context.getResources().getColor(R.color.color_blue_reward_title));
                description.setTextColor(context.getResources().getColor(R.color.color_blue_reward_description));
                break;
            case "green":
                title.setTextColor(context.getResources().getColor(R.color.color_green_reward_title));
                description.setTextColor(context.getResources().getColor(R.color.color_green_reward_description));
                break;
            case "red":
                title.setTextColor(context.getResources().getColor(R.color.color_red_reward_title));
                description.setTextColor(context.getResources().getColor(R.color.color_red_reward_description));
                break;
            case "orange":
                title.setTextColor(context.getResources().getColor(R.color.color_orange_reward_title));
                description.setTextColor(context.getResources().getColor(R.color.color_orange_reward_description));
                break;
        }
    }
}
