package lifesgame.tapstudios.ca.lifesgame;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.text.SimpleDateFormat;
import java.util.Date;

import info.hoang8f.widget.FButton;
import lifesgame.tapstudios.ca.lifesgame.activity.MainActivity;
import lifesgame.tapstudios.ca.lifesgame.helper.DatabaseHelper;
import lifesgame.tapstudios.ca.lifesgame.model.Rewards;

public class DialogAddRewards extends AppCompatActivity {
    private TextView rewardTitle;
    private TextView rewardDescription;
    private TextInputLayout rewardTitleLayout;
    private DatabaseHelper databaseHelper;
    private CheckBox singleTime;
    private CheckBox unlimited;
    private CheckBox blueColor;
    private CheckBox redColor;
    private CheckBox greenColor;
    private CheckBox orangeColor;
    private String checkedColor;
    private Integer id = -1;
    private Tracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_add_rewards);
        rewardTitle = findViewById(R.id.user_reward_title);
        rewardDescription = findViewById(R.id.user_reward_description);
        rewardTitleLayout = findViewById(R.id.user_reward_title_layout);
        databaseHelper = new DatabaseHelper(this);
        checkedColor = "blue";
        addItem();
        deleteItem();
        silverSeekBar();
        setupCheckBoxes();

        id = getIntent().getIntExtra("ID", -1);

        if (id != -1) {
            editReward();
        }

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        tracker = application.getDefaultTracker();
        tracker.setScreenName("AddRewardDialog");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void silverSeekBar() {
        SeekBar silverSeekBar = (SeekBar) findViewById(R.id.silver_seek_bar);
        silverSeekBar.setMax(9999);
        silverSeekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    int progressValue;
                    final EditText silverEditText = (EditText) findViewById(R.id.silver_amount_edit_text);

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        progressValue = i;
                        silverEditText.setText(String.valueOf(progressValue));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        return;
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        silverEditText.setText(String.valueOf(progressValue));
                    }
                }
        );
    }

    private void setupCheckBoxes() {
        singleTime = (CheckBox) findViewById(R.id.singleTime);
        unlimited = (CheckBox) findViewById(R.id.unlimited);
        blueColor = (CheckBox) findViewById(R.id.color_blue);
        greenColor = (CheckBox) findViewById(R.id.color_green);
        redColor = (CheckBox) findViewById(R.id.color_red);
        orangeColor = (CheckBox) findViewById(R.id.color_orange);

        singleTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleTime.setChecked(true);
                unlimited.setChecked(false);
            }
        });

        unlimited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleTime.setChecked(false);
                unlimited.setChecked(true);
            }
        });

        blueColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blueColor.setChecked(true);
                redColor.setChecked(false);
                greenColor.setChecked(false);
                orangeColor.setChecked(false);
                checkedColor = "blue";
            }
        });

        redColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blueColor.setChecked(false);
                redColor.setChecked(true);
                greenColor.setChecked(false);
                orangeColor.setChecked(false);
                checkedColor = "red";
            }
        });

        greenColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blueColor.setChecked(false);
                redColor.setChecked(false);
                greenColor.setChecked(true);
                orangeColor.setChecked(false);
                checkedColor = "green";
            }
        });

        orangeColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blueColor.setChecked(false);
                redColor.setChecked(false);
                greenColor.setChecked(false);
                orangeColor.setChecked(true);
                checkedColor = "orange";
            }
        });
    }

    private void addItem() {
        FButton userAcceptTaskGoalBtn = (FButton) findViewById(R.id.btn_user_accept_reward);
        userAcceptTaskGoalBtn.setButtonColor(getResources().getColor(R.color.fbutton_color_emerald));
        userAcceptTaskGoalBtn.setShadowColor(getResources().getColor(R.color.fbutton_color_green_sea));
        userAcceptTaskGoalBtn.setShadowEnabled(true);
        userAcceptTaskGoalBtn.setShadowHeight(8);
        userAcceptTaskGoalBtn.setCornerRadius(15);
        userAcceptTaskGoalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText userRewardsCost = (EditText) findViewById(R.id.silver_amount_edit_text);

                if (rewardTitle.getText().toString().isEmpty()) {
                    rewardTitleLayout.setError("Title must not be blank!");
                }

                final CheckBox unlimited = (CheckBox) findViewById(R.id.unlimited);

                Boolean unlimitedConsumption = unlimited.isChecked();

                if (!rewardTitle.getText().toString().isEmpty()) {
                    Date date = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    if (id != -1) {
                        databaseHelper.updateReward(id,
                                rewardTitle.getText().toString(),
                                rewardDescription.getText().toString(),
                                Integer.valueOf(userRewardsCost.getText().toString()),
                                formatter.format(date),
                                unlimitedConsumption,
                                checkedColor);

                        tracker.send(new HitBuilders.EventBuilder()
                                .setCategory("Reward-Update")
                                .setAction(rewardTitle.getText().toString())
                                .setLabel(rewardTitle.getText().toString())
                                .build());
                    } else {
                        databaseHelper.addReward(rewardTitle.getText().toString(),
                                rewardDescription.getText().toString(),
                                Integer.valueOf(userRewardsCost.getText().toString()),
                                formatter.format(date),
                                unlimitedConsumption,
                                checkedColor);

                        tracker.send(new HitBuilders.EventBuilder()
                                .setCategory("Reward-Addition")
                                .setAction(rewardTitle.getText().toString())
                                .setLabel(rewardTitle.getText().toString())
                                .build());
                    }

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("FRAGMENT_NUMBER", 2);
                    startActivity(intent);
                }
            }
        });
    }

    private void deleteItem() {
        FButton userCancelTaskGoalBtn = (FButton) findViewById(R.id.btn_user_cancel_reward);
        userCancelTaskGoalBtn.setButtonColor(getResources().getColor(R.color.fbutton_color_alizarin));
        userCancelTaskGoalBtn.setShadowColor(getResources().getColor(R.color.fbutton_color_pomegranate));
        userCancelTaskGoalBtn.setShadowEnabled(true);
        userCancelTaskGoalBtn.setShadowHeight(8);
        userCancelTaskGoalBtn.setCornerRadius(15);
        userCancelTaskGoalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("FRAGMENT_NUMBER", 2);
                startActivity(intent);
            }
        });
    }

    public void editReward() {
        Rewards rewards = databaseHelper.getReward(id);
        rewardTitle.setText(rewards.getTitle());
        rewardDescription.setText(rewards.getDescription());
        if (rewards.getUnlimitedConsumption()) {
            unlimited.setChecked(true);
            singleTime.setChecked(false);
        } else {
            singleTime.setChecked(true);
            unlimited.setChecked(false);
        }

        blueColor.setChecked(false);
        greenColor.setChecked(false);
        orangeColor.setChecked(false);
        redColor.setChecked(false);

        switch (rewards.getStyleColor()) {
            case "blue":
                blueColor.setChecked(true);
                break;
            case "green":
                greenColor.setChecked(true);
                break;
            case "red":
                redColor.setChecked(true);
                break;
            case "orange":
                orangeColor.setChecked(true);
                break;
        }
        ((SeekBar) findViewById(R.id.silver_seek_bar)).setProgress(rewards.getCost());
    }
}
