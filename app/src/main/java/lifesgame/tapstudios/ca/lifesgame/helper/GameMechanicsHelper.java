package lifesgame.tapstudios.ca.lifesgame.helper;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

import cn.pedant.SweetAlert.SweetAlertDialog;
import lifesgame.tapstudios.ca.lifesgame.activity.ProfilePicker;

/**
 * Created by Vidit Soni on 6/3/2017.
 */
public class GameMechanicsHelper {
    private TextView charHealth;
    private TextView charXp;
    private TextView charLevel;
    private TextView silverAmountTextView;
    private int maxHealth;
    private int maxXp;
    private int currentXp;
    private int currentLvl;
    private int currentHealth;
    private Long totalSilver;
    private DatabaseHelper databaseHelper;
    private RoundCornerProgressBar healthBar;
    private RoundCornerProgressBar xpBar;
    private int healthExercise;
    private int work;
    private int school;
    private int familyFriends;
    private int learning;
    private int other;
    private int maxHealthExercise;
    private int maxWork;
    private int maxSchool;
    private int maxFamilyFriends;
    private int maxLearning;
    private int maxOther;
    private int lvlHealthExercise;
    private int lvlWork;
    private int lvlSchool;
    private int lvlFamilyFriends;
    private int lvlLearning;
    private int lvlOther;
    private Context context;

    public GameMechanicsHelper(TextView charHealth,
                               TextView charXp,
                               TextView charLevel,
                               TextView silverAmountTextView,
                               DatabaseHelper databaseHelper,
                               RoundCornerProgressBar healthBar,
                               RoundCornerProgressBar xpBar,
                               Context context) {
        this.charHealth = charHealth;
        this.charXp = charXp;
        this.charLevel = charLevel;
        this.silverAmountTextView = silverAmountTextView;
        this.databaseHelper = databaseHelper;
        this.maxHealth = 100;
        this.currentHealth = 100;
        this.currentXp = 0;
        this.currentLvl = 1;
        this.maxXp = 1000;
        this.totalSilver = 0L;
        this.healthBar = healthBar;
        this.xpBar = xpBar;
        this.healthExercise = 0;
        this.work = 0;
        this.school = 0;
        this.familyFriends = 0;
        this.learning = 0;
        this.other = 0;
        this.maxHealthExercise = 0;
        this.maxWork = 0;
        this.maxSchool = 0;
        this.maxFamilyFriends = 0;
        this.maxLearning = 0;
        this.maxOther = 0;
        lvlHealthExercise = 0;
        lvlWork = 0;
        lvlSchool = 0;
        lvlFamilyFriends = 0;
        lvlLearning = 0;
        lvlOther = 0;
        this.context = context;
    }

    public void setUpGameTextViews() {
        try {
            currentHealth = Integer.parseInt(databaseHelper.getValue(databaseHelper.CHAR_HEALTH));
            maxHealth = Integer.parseInt(databaseHelper.getValue(databaseHelper.CHAR_MAX_HEALTH));
            currentXp = Integer.parseInt(databaseHelper.getValue(databaseHelper.CHAR_XP));
            maxXp = Integer.parseInt(databaseHelper.getValue(databaseHelper.CHAR_MAX_XP));
            currentLvl = Integer.parseInt(databaseHelper.getValue(databaseHelper.CHAR_LVL));
            totalSilver = Long.valueOf(databaseHelper.getValue(databaseHelper.SILVER_AMOUNT_TOTAL));

            healthExercise = Integer.parseInt(databaseHelper.getValue(databaseHelper.USER_HEALTH_EXERCISE));
            work = Integer.parseInt(databaseHelper.getValue(databaseHelper.USER_WORK));
            school = Integer.parseInt(databaseHelper.getValue(databaseHelper.USER_SCHOOL));
            familyFriends = Integer.parseInt(databaseHelper.getValue(databaseHelper.USER_FAMILY_FRIENDS));
            learning = Integer.parseInt(databaseHelper.getValue(databaseHelper.USER_LEARNING));
            other = Integer.parseInt(databaseHelper.getValue(databaseHelper.USER_OTHER));
            maxHealthExercise = Integer.parseInt(databaseHelper.getValue(databaseHelper.USER_MAX_HEALTH_EXERCISE));
            maxWork = Integer.parseInt(databaseHelper.getValue(databaseHelper.USER_MAX_WORK));
            maxSchool = Integer.parseInt(databaseHelper.getValue(databaseHelper.USER_MAX_SCHOOL));
            maxFamilyFriends = Integer.parseInt(databaseHelper.getValue(databaseHelper.USER_MAX_FAMILY_FRIENDS));
            maxLearning = Integer.parseInt(databaseHelper.getValue(databaseHelper.USER_MAX_LEARNING));
            maxOther = Integer.parseInt(databaseHelper.getValue(databaseHelper.USER_MAX_OTHER));

            //setup progress bars for health and xp
            xpBar.setMax(maxXp);
            xpBar.setProgress(currentXp);
            healthBar.setMax(maxHealth);
            healthBar.setProgress(currentHealth);
        } catch (Exception e) {
            databaseHelper.initiateKeys();
            xpBar.setMax(maxXp);
            xpBar.setProgress(currentXp);
            healthBar.setMax(maxHealth);
            healthBar.setProgress(currentHealth);
            Log.d("Error", "Error asserted: " + e);
        }
        charHealth.setText(Integer.toString(currentHealth) + "/" + Integer.toString(maxHealth));
        charXp.setText(Integer.toString(currentXp) + "/" + Integer.toString(maxXp));
        charLevel.setText(Integer.toString(currentLvl));
        silverAmountTextView.setText(Long.toString(totalSilver));
    }

    public void updateGameTextViews() {
        charHealth.setText(Integer.toString(currentHealth) + "/" + Integer.toString(maxHealth));
        charXp.setText(Integer.toString(currentXp) + "/" + Integer.toString(maxXp));
        charLevel.setText(Integer.toString(currentLvl));
    }

    public void addSilver(Integer addSilver) {
        totalSilver += addSilver;
        databaseHelper.updateValue(databaseHelper.SILVER_AMOUNT_TOTAL, Long.toString(totalSilver));
        silverAmountTextView.setText(Long.toString(totalSilver));
    }

    public void removeHealth() {
        currentHealth -= 10;
        charHealth.setText(Integer.toString(currentHealth) + "/" + Integer.toString(maxHealth));
        healthBar.setProgress(currentHealth);
        if (currentHealth <= 0) {
            new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Uh Oh...")
                    .setContentText("You have lost all your health, time to restart")
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            databaseHelper.deleteTableKeyValyes();
                            databaseHelper.initiateKeys();
                            Intent intent = new Intent(context, ProfilePicker.class);
                            context.startActivity(intent);
                        }
                    })
                    .show();
        } else {
            databaseHelper.updateValue(databaseHelper.CHAR_HEALTH, Integer.toString(currentHealth));
        }
    }

    public void addHealth() {
        currentHealth += 10;
        databaseHelper.updateValue(databaseHelper.CHAR_HEALTH, Integer.toString(currentHealth));
        charHealth.setText(Integer.toString(currentHealth) + "/" + Integer.toString(maxHealth));
        healthBar.setProgress(currentHealth);
    }

    public Boolean addXp(Integer amount) {
        Boolean levelUp = false;
        currentXp += amount;
        xpBar.setProgress(currentXp);
        if (currentXp >= maxXp) {
            currentXp -= maxXp;
            maxXp += 50;
            xpBar.setMax(maxXp);
            xpBar.setProgress(currentXp);
            maxHealth += 100;
            currentHealth = maxHealth;
            healthBar.setMax(maxHealth);
            healthBar.setProgress(currentHealth);
            addLevel();
            updateGameTextViews();
            databaseHelper.updateValue(databaseHelper.CHAR_MAX_HEALTH, Integer.toString(maxHealth));
            databaseHelper.updateValue(databaseHelper.CHAR_MAX_XP, Integer.toString(maxXp));
            databaseHelper.updateValue(databaseHelper.CHAR_HEALTH, Integer.toString(currentHealth));
            levelUp = true;
        }
        databaseHelper.updateValue(databaseHelper.CHAR_XP, Integer.toString(currentXp));
        charXp.setText(Integer.toString(currentXp) + "/" + Integer.toString(maxXp));
        return levelUp;
    }

    public void addLevel() {
        currentLvl += 1;
        databaseHelper.updateValue(databaseHelper.CHAR_LVL, Integer.toString(currentLvl));
        charLevel.setText(Integer.toString(currentLvl));
    }

    public void addImprovementType(String type) {
        switch (type) {
            case "health_exercise":
                healthExercise += 50;
                if (healthExercise == maxHealthExercise) {
                    maxHealthExercise *= 2;
                    healthExercise = 0;
                    lvlHealthExercise += 1;
                    databaseHelper.updateValue(databaseHelper.USER_HEALTH_EXERCISE_LVL, Integer.toString(lvlHealthExercise));
                    databaseHelper.updateValue(databaseHelper.USER_MAX_HEALTH_EXERCISE, Integer.toString(maxHealthExercise));
                }
                databaseHelper.updateValue(databaseHelper.USER_HEALTH_EXERCISE, Integer.toString(healthExercise));
                break;
            case "work":
                work += 50;
                if (work == maxWork) {
                    maxWork *= 2;
                    work = 0;
                    lvlWork += 1;
                    databaseHelper.updateValue(databaseHelper.USER_WORK_LVL, Integer.toString(lvlWork));
                    databaseHelper.updateValue(databaseHelper.USER_MAX_WORK, Integer.toString(maxWork));
                }
                databaseHelper.updateValue(databaseHelper.USER_WORK, Integer.toString(work));
                break;
            case "school":
                school += 50;
                if (school == maxSchool) {
                    maxSchool *= 2;
                    school = 0;
                    lvlSchool += 1;
                    databaseHelper.updateValue(databaseHelper.USER_SCHOOL_LVL, Integer.toString(lvlSchool));
                    databaseHelper.updateValue(databaseHelper.USER_MAX_SCHOOL, Integer.toString(maxSchool));
                }
                databaseHelper.updateValue(databaseHelper.USER_SCHOOL, Integer.toString(school));
                break;
            case "family_friends":
                familyFriends += 50;
                if (familyFriends == maxFamilyFriends) {
                    maxFamilyFriends *= 2;
                    familyFriends = 0;
                    lvlFamilyFriends += 1;
                    databaseHelper.updateValue(databaseHelper.USER_FAMILY_FRIENDS_LVL, Integer.toString(lvlFamilyFriends));
                    databaseHelper.updateValue(databaseHelper.USER_MAX_FAMILY_FRIENDS, Integer.toString(maxFamilyFriends));
                }
                databaseHelper.updateValue(databaseHelper.USER_FAMILY_FRIENDS, Integer.toString(familyFriends));
                break;
            case "learning":
                learning += 50;
                if (learning == maxLearning) {
                    maxLearning *= 2;
                    learning = 0;
                    lvlLearning += 1;
                    databaseHelper.updateValue(databaseHelper.USER_LEARNING_LVL, Integer.toString(lvlLearning));
                    databaseHelper.updateValue(databaseHelper.USER_MAX_LEARNING, Integer.toString(maxLearning));
                }
                databaseHelper.updateValue(databaseHelper.USER_LEARNING, Integer.toString(learning));
                break;
            case "other":
                other += 50;
                if (other == maxOther) {
                    maxOther *= 2;
                    other = 0;
                    lvlOther += 1;
                    databaseHelper.updateValue(databaseHelper.USER_OTHER_LVL, Integer.toString(lvlOther));
                    databaseHelper.updateValue(databaseHelper.USER_MAX_OTHER, Integer.toString(maxOther));
                }
                databaseHelper.updateValue(databaseHelper.USER_OTHER, Integer.toString(other));
                break;
        }
    }
}
