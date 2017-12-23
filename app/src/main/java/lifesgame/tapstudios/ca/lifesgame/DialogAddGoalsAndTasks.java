package lifesgame.tapstudios.ca.lifesgame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import info.hoang8f.widget.FButton;

public class DialogAddGoalsAndTasks extends AppCompatActivity {
    private SelectedDate mSelectedDate;
    private EditText endDateEt;
    private LinearLayout endDateLl;
    private EditText userTaskGoalTitle;
    private EditText userTaskGoalDescription;
    Spinner improvementCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_add_goals_and_tasks);
        improvementCategory = (Spinner) findViewById(R.id.spinner1);
        endDateEt = (EditText) findViewById(R.id.endDateTv);
        endDateLl = (LinearLayout) findViewById(R.id.endDateHolder);
        userTaskGoalDescription = (EditText) findViewById(R.id.textDescription);
        userTaskGoalTitle = (EditText) findViewById(R.id.textTitle);

        endDateEt.setInputType(InputType.TYPE_NULL);
        endDateLl.setVisibility(View.GONE);

        final String[] improvementCategories = new String[]{"Task", "Goal", "Quest", "Epic"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, improvementCategories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        improvementCategory.setAdapter(spinnerAdapter);
        addItem();
        deleteItem();
        categoryTitleEditListener();
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

        improvementCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 1) {
                    endDateLl.setVisibility(View.VISIBLE);
                } else {
                    endDateLl.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
        FButton userAcceptTaskGoalBtn = (FButton) findViewById(R.id.btn_user_accept_goal_task);
        userAcceptTaskGoalBtn.setButtonColor(getResources().getColor(R.color.fbutton_color_emerald));
        userAcceptTaskGoalBtn.setShadowColor(getResources().getColor(R.color.fbutton_color_green_sea));
        userAcceptTaskGoalBtn.setShadowEnabled(true);
        userAcceptTaskGoalBtn.setShadowHeight(8);
        userAcceptTaskGoalBtn.setCornerRadius(15);
        userAcceptTaskGoalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    intent.putExtra("DATA_CATEGORY", improvementCategory.getSelectedItem().toString());
                    intent.putExtra("DATA_TITLE", userTaskGoalTitle.getText().toString());
                    intent.putExtra("DATA_SILVER", Long.valueOf(userTaskGoalSilver.getText().toString()));
                    intent.putExtra("DATA_IMPROVEMENT_TYPE", improvementType);
                    if (improvementCategory.getSelectedItem().toString().equals("Goal")) {
                        Calendar calendarDeadline = mSelectedDate.getEndDate();
                        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                        intent.putExtra("DATA_ENDDATE", formatter.format(calendarDeadline.getTime()));
                    }
                    startActivity(intent);
                }
            }
        });
    }

    private void deleteItem() {
        FButton userCancelTaskGoalBtn = (FButton) findViewById(R.id.btn_user_cancel_goal_task);
        userCancelTaskGoalBtn.setButtonColor(getResources().getColor(R.color.fbutton_color_alizarin));
        userCancelTaskGoalBtn.setShadowColor(getResources().getColor(R.color.fbutton_color_pomegranate));
        userCancelTaskGoalBtn.setShadowEnabled(true);
        userCancelTaskGoalBtn.setShadowHeight(8);
        userCancelTaskGoalBtn.setCornerRadius(15);
        userCancelTaskGoalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void categoryTitleEditListener() {
        userTaskGoalTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE)) {
                    // hide virtual keyboard
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return false;

            }
        });

        userTaskGoalDescription.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE)) {
                    // hide virtual keyboard
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return false;

            }
        });
    }
}


