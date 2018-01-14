package lifesgame.tapstudios.ca.lifesgame;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import lifesgame.tapstudios.ca.lifesgame.helper.PurchaseHelper;
import lifesgame.tapstudios.ca.lifesgame.model.Rewards;

/**
 * Created by viditsoni on 2017-12-28.
 */

public class RewardsHolder extends RecyclerView.ViewHolder {
    private final ImageButton editBtn;
    private final ImageButton deleteBtn;
    private Rewards rewards;
    private Context context;
    private RewardsAdapter rewardsAdapter;
    private PurchaseHelper purchaseHelper;

    private TextView title;
    private TextView description;
    private TextView cost;
    private LinearLayout lLPurchaseReward;
    private LinearLayout infinityLl;

    public RewardsHolder(Context context,
                         View itemView,
                         RewardsAdapter rewardsAdapter) {
        super(itemView);
        this.purchaseHelper = new PurchaseHelper(context);
        this.context = context;
        this.rewardsAdapter = rewardsAdapter;
        this.title = itemView.findViewById(R.id.reward_title);
        this.description = itemView.findViewById(R.id.reward_description);
        this.cost = itemView.findViewById(R.id.reward_cost);
        this.lLPurchaseReward = itemView.findViewById(R.id.purchase_reward);
        this.editBtn = (ImageButton) itemView.findViewById(R.id.editButton);
        this.deleteBtn = (ImageButton) itemView.findViewById(R.id.deleteButton);
        this.infinityLl = (LinearLayout) itemView.findViewById(R.id.infinity_Ll);
    }

    public void bindRewards(Rewards rewards, int position) {
        title.setText(rewards.getTitle());
        description.setText(rewards.getDescription());
        cost.setText(String.valueOf(rewards.getCost()));
        if (rewards.getUnlimitedConsumption()) {
            infinityLl.setVisibility(View.VISIBLE);
        }
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
}
