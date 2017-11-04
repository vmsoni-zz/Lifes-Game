package lifesgame.tapstudios.ca.lifesgame;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import java.text.DateFormat;
import java.util.HashMap;

public class DialogAddGoalsAndTasks extends AppCompatActivity {
    private SelectedDate mSelectedDate;
    private EditText endDateEt;
    private LinearLayout endDateLl;
    private int mHour, mMinute;
    private RelativeLayout rlDateTimeRecurrenceInfo;
    private String mRecurrenceOption, mRecurrenceRule;
    private MainActivity mainActivity;
    BetterSpinner improvementCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_add_goals_and_tasks);
        improvementCategory = (BetterSpinner) findViewById(R.id.spinner1);
        endDateEt = (EditText) findViewById(R.id.endDateTv);
        endDateLl = (LinearLayout) findViewById(R.id.endDateHolder);

        endDateEt.setInputType(InputType.TYPE_NULL);
        endDateLl.setVisibility(View.GONE);

        final String[] improvementCategories = new String[]{"Task", "Goal", "Quest", "Epic"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, improvementCategories);
        improvementCategory.setAdapter(spinnerAdapter);
        mainActivity = new MainActivity();
        addItem();
        silverSeekBar();
        goalSelection();
    }

    DatePicker.Callback mFragmentCallback = new DatePicker.Callback() {
        @Override
        public void onCancelled() {
        }

        @Override
        public void onDateTimeRecurrenceSet(SelectedDate selectedDate,
                                            int hourOfDay, int minute,
                                            SublimeRecurrencePicker.RecurrenceOption recurrenceOption,
                                            String recurrenceRule) {
            mSelectedDate = selectedDate;
            mHour = hourOfDay;
            mMinute = minute;
            mRecurrenceOption = recurrenceOption != null ? recurrenceOption.name() : "n/a";
            mRecurrenceRule = recurrenceRule != null ? recurrenceRule : "n/a";
            updateInfoView();
        }
    };

    private void goalSelection() {
        //Onclick Listener for the end date text view
        endDateEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicker pickerFrag = new DatePicker();
                pickerFrag.setCallback(mFragmentCallback);
                SublimeOptions options = new SublimeOptions();
                options.setPickerToShow(SublimeOptions.Picker.DATE_PICKER);
                options.setDisplayOptions(SublimeOptions.ACTIVATE_DATE_PICKER);
                options.setCanPickDateRange(false);

                Bundle bundle = new Bundle();
                bundle.putParcelable("SUBLIME_OPTIONS", options);
                pickerFrag.setArguments(bundle);

                pickerFrag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                pickerFrag.show(getSupportFragmentManager(), "SUBLIME_PICKER");
            }
        });

        //Onclick Goal category within ToDo spinner
        improvementCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    endDateLl.setVisibility(View.VISIBLE);
                } else {
                    endDateLl.setVisibility(View.GONE);
                }
                improvementCategory.onItemClick(parent, view, position, id);
            }
        });
    }

    private void updateInfoView() {
        if (mSelectedDate != null) {
            if (mSelectedDate.getType() == SelectedDate.Type.SINGLE) {
                endDateEt.setText(applyBoldStyle("END: ")
                        .append(DateFormat.getDateInstance().format(mSelectedDate.getEndDate().getTime())));
            }
        }
    }

    private SpannableStringBuilder applyBoldStyle(String text) {
        SpannableStringBuilder ss = new SpannableStringBuilder(text);
        ss.setSpan(new StyleSpan(Typeface.BOLD), 0, text.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    private void silverSeekBar() {
        SeekBar silverSeekBar = (SeekBar) findViewById(R.id.silver_seek_bar);
        silverSeekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    int progressValue;
                    final TextView silverTextView = (TextView) findViewById(R.id.silver_amount_text_view);

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        progressValue = i;
                        silverTextView.setText(String.valueOf(progressValue));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        return;
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        silverTextView.setText(String.valueOf(progressValue));
                    }
                }
        );
    }

    private void addItem() {
        Button userAcceptTaskGoalBtn = (Button) findViewById(R.id.btn_user_accept_goal_task);
        userAcceptTaskGoalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText userTaskGoalDescription = (EditText) findViewById(R.id.textDescription);
                final EditText userTaskGoalTitle = (EditText) findViewById(R.id.textTitle);
                final TextView userTaskGoalSilver = (TextView) findViewById(R.id.silver_amount_text_view);
                final CheckBox userHealthExercise = (CheckBox) findViewById(R.id.healthExercise);
                final CheckBox userWork = (CheckBox) findViewById(R.id.work);
                final CheckBox userSchool = (CheckBox) findViewById(R.id.school);
                final CheckBox userFamilyFriends = (CheckBox) findViewById(R.id.familyFriends);
                final CheckBox userLearning = (CheckBox) findViewById(R.id.learning);
                final CheckBox userOther = (CheckBox) findViewById(R.id.other);

                HashMap<String, Boolean> improvementType = new HashMap<String, Boolean>();
                improvementType.put("health_exercise", userHealthExercise.isChecked());
                improvementType.put("work", userWork.isChecked());
                improvementType.put("school", userSchool.isChecked());
                improvementType.put("family_friends", userFamilyFriends.isChecked());
                improvementType.put("learning", userLearning.isChecked());
                improvementType.put("other", userOther.isChecked());

                if (!userTaskGoalDescription.getText().toString().isEmpty() && !userTaskGoalTitle.getText().toString().isEmpty()) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("DATA_DESCRIPTION", userTaskGoalDescription.getText().toString());
                    intent.putExtra("DATA_CATEGORY", improvementCategory.getText().toString());
                    intent.putExtra("DATA_TITLE", userTaskGoalTitle.getText().toString());
                    intent.putExtra("DATA_SILVER", Long.valueOf(userTaskGoalSilver.getText().toString()));
                    intent.putExtra("DATA_IMPROVEMENT_TYPE", improvementType);
                    if (endDateLl.getVisibility() == View.VISIBLE) {
                        intent.putExtra("DATA_ENDDATE", mSelectedDate.getEndDate());
                    }
                    startActivity(intent);
                }
            }
        });
    }
}


