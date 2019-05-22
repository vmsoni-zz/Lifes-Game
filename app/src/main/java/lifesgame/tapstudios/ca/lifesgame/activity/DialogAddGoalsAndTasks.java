package lifesgame.tapstudios.ca.lifesgame.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.hoang8f.widget.FButton;
import lifesgame.tapstudios.ca.lifesgame.AnalyticsApplication;
import lifesgame.tapstudios.ca.lifesgame.DatePicker;
import lifesgame.tapstudios.ca.lifesgame.modelV2.TaskTodo;
import lifesgame.tapstudios.ca.lifesgame.modelV2.TodoTag;
import lifesgame.tapstudios.ca.lifesgame.modelV2.TodoTypes;
import lifesgame.tapstudios.ca.lifesgame.modelV2.generic.Todo;
import lifesgame.tapstudios.ca.lifesgame.repository.TodoRepository;
import lifesgame.tapstudios.ca.lifesgame.service.JobService;
import lifesgame.tapstudios.ca.lifesgame.R;
import lifesgame.tapstudios.ca.lifesgame.helper.DatabaseHelper;
import lifesgame.tapstudios.ca.lifesgame.model.GoalsAndTasks;
import lifesgame.tapstudios.ca.lifesgame.model.NotificationDate;
import lifesgame.tapstudios.ca.lifesgame.model.TodoType;
import lifesgame.tapstudios.ca.lifesgame.utility.DateUtils;

public class DialogAddGoalsAndTasks extends AppCompatActivity {
    private static final String TABLE_TASKS_GOALS_HEALTH_EXERCISE = "health_exercise";
    private static final String TABLE_TASKS_GOALS_WORK = "work";
    private static final String TABLE_TASKS_GOALS_SCHOOL = "school";
    private static final String TABLE_TASKS_GOALS_FAMILY_FRIENDS = "family_friends";
    private static final String TABLE_TASKS_GOALS_LEARNING = "learning";
    private static final String TABLE_TASKS_GOALS_OTHER = "other";
    private static final String taskDescription = "* Tasks expire at 11:59pm on their start date *";
    private static final String goalDescription = "* Goals expire at 11:59pm on their deadline date *";
    private static final String dailyDescription = "* Dailies reset 11:59pm everyday *";

    private SelectedDate mSelectedEndDate;
    private SelectedDate mSelectedStartDate;
    private SelectedDate mSelectedNotificationDateTime;
    private NotificationDate notificationDate;
    private TodoRepository todoRepository;

    @BindView(R.id.silver_seek_bar)
    SeekBar silverSeekBar;
    @BindView(R.id.endDateTv)
    EditText endDateEt;
    @BindView(R.id.startDateTv)
    EditText startDateEt;
    @BindView(R.id.notificationDateTimeTv)
    EditText notificationDateTimeEt;
    @BindView(R.id.silver_amount_edit_text)
    EditText silverEditText;
    @BindView(R.id.endDateHolder)
    LinearLayout endDateLl;
    @BindView(R.id.startDateHolder)
    LinearLayout startDateLl;
    @BindView(R.id.notificationDateTimeHolder)
    LinearLayout notificationDateTimeLl;
    @BindView(R.id.textTitle)
    TextInputEditText userTaskGoalTitle;
    @BindView(R.id.textTitleLayout)
    TextInputLayout userTaskGoalTitleLayout;
    @BindView(R.id.endDateLayout)
    TextInputLayout endDateLayout;
    @BindView(R.id.startDateLayout)
    TextInputLayout startDateLayout;
    @BindView(R.id.notificationDateTimeLayout)
    TextInputLayout notificationDateTimeLayout;
    @BindView(R.id.textDescription)
    TextInputEditText userTaskGoalDescription;
    @BindView(R.id.todoTypeDescriptionTV)
    TextView todoTypeDescription;
    @BindView(R.id.spinner1)
    Spinner improvementCategory;
    @BindView(R.id.btn_user_accept_goal_task)
    FButton userAcceptTaskGoalBtn;
    @BindView(R.id.healthExercise)
    CheckBox userHealthExercise;
    @BindView(R.id.work)
    CheckBox userWork;
    @BindView(R.id.school)
    CheckBox userSchool;
    @BindView(R.id.familyFriends)
    CheckBox userFamilyFriends;
    @BindView(R.id.learning)
    CheckBox userLearning;
    @BindView(R.id.other)
    CheckBox userOther;

    private DatabaseHelper databaseHelper;
    private JobService jobService;
    private Long id;
    private Tracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_add_goals_and_tasks);
        databaseHelper = new DatabaseHelper(this);
        jobService = new JobService(this);
        ButterKnife.bind(this);

        endDateEt.setInputType(InputType.TYPE_NULL);
        startDateEt.setInputType(InputType.TYPE_NULL);
        notificationDateTimeEt.setInputType(InputType.TYPE_NULL);
        endDateLl.setVisibility(View.GONE);
        startDateLl.setVisibility(View.GONE);

        final String[] improvementCategories = new String[]{"Task", "Goal", "Daily"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, improvementCategories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        improvementCategory.setAdapter(spinnerAdapter);
        setupAddItemButton();
        setupDeleteItemButton();
        categoryTitleEditListener();
        setupSilverSeekBarListener();
        setupTodoSelectionListener();

        todoRepository = ViewModelProviders.of(this).get(TodoRepository.class);

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        tracker = application.getDefaultTracker();
        tracker.setScreenName("AddToDoDialog");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());

        id = getIntent().getLongExtra("ID", -1L);

        mSelectedStartDate = new SelectedDate(Calendar.getInstance());
        notificationDate = null;
        updateStartDateInfoView();
        silverEditTextListener();
        if (id != -1L) {
            editTodo();
        }
    }

    DatePicker.Callback mStartdateFragmentCallback = new DatePicker.Callback() {
        @Override
        public void onCancelled() {
        }

        @Override
        public void onDateTimeRecurrenceSet(SelectedDate selectedDate,
                                            int hourOfDay, int minute,
                                            SublimeRecurrencePicker.RecurrenceOption recurrenceOption,
                                            String recurrenceRule) {
            mSelectedStartDate = selectedDate;
            updateStartDateInfoView();
        }
    };


    DatePicker.Callback mEndateFragmentCallback = new DatePicker.Callback() {
        @Override
        public void onCancelled() {
        }

        @Override
        public void onDateTimeRecurrenceSet(SelectedDate selectedDate,
                                            int hourOfDay, int minute,
                                            SublimeRecurrencePicker.RecurrenceOption recurrenceOption,
                                            String recurrenceRule) {
            mSelectedEndDate = selectedDate;
            updateEndDateInfoView();
        }
    };

    DatePicker.Callback mNotificationDateTimeFragmentCallback = new DatePicker.Callback() {
        @Override
        public void onCancelled() {
        }

        @Override
        public void onDateTimeRecurrenceSet(SelectedDate selectedDate,
                                            int hourOfDay, int minute,
                                            SublimeRecurrencePicker.RecurrenceOption recurrenceOption,
                                            String recurrenceRule) {
            long milliseconds = (hourOfDay * 60 * 60 * 1000) + (minute * 60 * 1000);
            notificationDate = new NotificationDate(hourOfDay, minute, milliseconds, selectedDate.getStartDate().getTime());
            mSelectedNotificationDateTime = selectedDate;
            updateNotificationDateInfoView();
        }
    };

    private void setupTodoSelectionListener() {
        startDateEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicker pickerFrag = new DatePicker();
                pickerFrag.setCallback(mStartdateFragmentCallback);
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

        //Onclick Listener for the end date text view
        endDateEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicker pickerFrag = new DatePicker();
                pickerFrag.setCallback(mEndateFragmentCallback);
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

        //Onclick Listener for the end date text view
        notificationDateTimeEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicker pickerFrag = new DatePicker();
                pickerFrag.setCallback(mNotificationDateTimeFragmentCallback);
                SublimeOptions options = new SublimeOptions();
                options.setPickerToShow(SublimeOptions.Picker.DATE_PICKER);
                options.setDisplayOptions(SublimeOptions.ACTIVATE_DATE_PICKER | SublimeOptions.ACTIVATE_TIME_PICKER);
                options.setCanPickDateRange(false);
                options.setAnimateLayoutChanges(true);

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
                switch (i) {
                    case 0:
                        todoTypeDescription.setText(taskDescription);
                        startDateLl.setVisibility(View.VISIBLE);
                        endDateLl.setVisibility(View.GONE);
                        break;
                    case 1:
                        todoTypeDescription.setText(goalDescription);
                        startDateLl.setVisibility(View.GONE);
                        endDateLl.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        todoTypeDescription.setText(dailyDescription);
                        startDateLl.setVisibility(View.GONE);
                        endDateLl.setVisibility(View.GONE);
                        break;
                    default:
                        todoTypeDescription.setText("");
                        startDateLl.setVisibility(View.GONE);
                        endDateLl.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void updateEndDateInfoView() {
        if (mSelectedEndDate != null) {
            if (mSelectedEndDate.getType() == SelectedDate.Type.SINGLE) {
                endDateEt.setText(applyBoldStyle("Deadline: ")
                        .append(DateFormat.getDateInstance().format(mSelectedEndDate.getEndDate().getTime())));
            }
        }
    }

    private void updateStartDateInfoView() {
        if (mSelectedStartDate != null) {
            if (mSelectedStartDate.getType() == SelectedDate.Type.SINGLE) {
                startDateEt.setText(applyBoldStyle("Start: ")
                        .append(DateFormat.getDateInstance().format(mSelectedStartDate.getEndDate().getTime())));
            }
        }
    }

    private void updateNotificationDateInfoView() {
        if (mSelectedNotificationDateTime != null) {
            if (mSelectedNotificationDateTime.getType() == SelectedDate.Type.SINGLE) {
                notificationDateTimeEt.setText(applyBoldStyle("Notification: ")
                        .append(DateFormat.getDateInstance().format(mSelectedNotificationDateTime.getEndDate().getTime())));
            }
        }
    }

    private void silverEditTextListener() {
        silverEditText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (count > 0 && Integer.valueOf(s.toString()) > 100) {
                    silverEditText.setText("100");
                    silverEditText.setSelection(silverEditText.getText().length());
                    silverSeekBar.setProgress(100);
                } else if (count > 0) {
                    silverSeekBar.setProgress(Integer.valueOf(s.toString()));
                    silverEditText.setSelection(silverEditText.getText().length());
                }
            }
        });
    }

    private SpannableStringBuilder applyBoldStyle(String text) {
        SpannableStringBuilder ss = new SpannableStringBuilder(text);
        ss.setSpan(new StyleSpan(Typeface.BOLD), 0, text.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    private void setupSilverSeekBarListener() {
        silverSeekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    int progressValue;
                    final EditText silverTextView = (EditText) findViewById(R.id.silver_amount_edit_text);

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

    private int setupFutureNotificiation(String todoType, NotificationDate notificationDate) {
        int notificationId;
        if (todoType.equals("Daily")) {
            notificationId = jobService.setFutureNotificationDailies(notificationDate, userTaskGoalTitle.getText().toString(), userTaskGoalDescription.getText().toString());
        } else {
            notificationId = jobService.setFutureNotification(notificationDate, userTaskGoalTitle.getText().toString(), userTaskGoalDescription.getText().toString());
        }
        return notificationId;
    }

    private void setupAddItemButton() {
        userAcceptTaskGoalBtn.setButtonColor(getResources().getColor(R.color.fbutton_color_emerald));
        userAcceptTaskGoalBtn.setShadowColor(getResources().getColor(R.color.fbutton_color_green_sea));
        userAcceptTaskGoalBtn.setShadowEnabled(true);
        userAcceptTaskGoalBtn.setShadowHeight(8);
        userAcceptTaskGoalBtn.setCornerRadius(15);
        userAcceptTaskGoalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Checking user input data
                if (userTaskGoalTitle.getText().toString().isEmpty()) {
                    userTaskGoalTitleLayout.setError("Title must not be blank!");
                    return;
                }
                if (notificationDate != null && (notificationDate.getNotificationDate().getTime() + notificationDate.getMilliseconds()) < (new Date()).getTime()) {
                    notificationDateTimeLayout.setError("Notification date/time cannot be before current date/time");
                    return;
                }

                //Initialize variables
                String deadlineDateString = null;
                String startDateString = null;
                String notificationDateString = null;
                String creationDateString = DateUtils.getDateString(new Date());
                TodoType todoType = TodoType.valueOf(improvementCategory.getSelectedItem().toString().toUpperCase());
                int silverAmount = silverEditText.getText().toString().isEmpty() ? 0 : Integer.valueOf(silverEditText.getText().toString());
                final Map<TodoTag.Tag, Boolean> improvementTypeMap = getImprovementTypeMap();
                Integer notificationId = -1;
                if (notificationDate != null) {
                    Calendar calendarNotificationDateTime = mSelectedNotificationDateTime.getEndDate();
                    notificationDateString = DateUtils.getDateString(calendarNotificationDateTime.getTime());
                    notificationId = setupFutureNotificiation(improvementCategory.getSelectedItem().toString(), notificationDate);
                }
                boolean shouldSave = (id == -1);

                saveOrEditTodo(todoType, shouldSave, silverAmount, improvementTypeMap, null, null, new Date(), null, notificationId);
                Intent intent = new Intent(DialogAddGoalsAndTasks.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupDeleteItemButton() {
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

    public void editTodo() {
        GoalsAndTasks goalsAndTasks = databaseHelper.getTodo(id);
        userTaskGoalTitle.setText(goalsAndTasks.getTitle());
        userTaskGoalDescription.setText(goalsAndTasks.getDescription());
        ((CheckBox) findViewById(R.id.healthExercise)).setChecked(goalsAndTasks.getImprovementTypeMap().get(TABLE_TASKS_GOALS_HEALTH_EXERCISE));
        ((CheckBox) findViewById(R.id.work)).setChecked(goalsAndTasks.getImprovementTypeMap().get(TABLE_TASKS_GOALS_WORK));
        ((CheckBox) findViewById(R.id.school)).setChecked(goalsAndTasks.getImprovementTypeMap().get(TABLE_TASKS_GOALS_SCHOOL));
        ((CheckBox) findViewById(R.id.familyFriends)).setChecked(goalsAndTasks.getImprovementTypeMap().get(TABLE_TASKS_GOALS_FAMILY_FRIENDS));
        ((CheckBox) findViewById(R.id.learning)).setChecked(goalsAndTasks.getImprovementTypeMap().get(TABLE_TASKS_GOALS_LEARNING));
        ((CheckBox) findViewById(R.id.other)).setChecked(goalsAndTasks.getImprovementTypeMap().get(TABLE_TASKS_GOALS_OTHER));

        improvementCategory.setSelection(goalsAndTasks.getCategory().getOrderValue());

        if (goalsAndTasks.getStartDate() != null) {
            Calendar startDate = Calendar.getInstance();
            startDate.setTime(goalsAndTasks.getStartDate());
            mSelectedStartDate = new SelectedDate(startDate);
            updateStartDateInfoView();
        }

        if (goalsAndTasks.getNotificationDate() != null) {
            Calendar notificationDate = Calendar.getInstance();
            notificationDate.setTime(goalsAndTasks.getNotificationDate());
            mSelectedNotificationDateTime = new SelectedDate(notificationDate);
            updateNotificationDateInfoView();
        }
        ((SeekBar) findViewById(R.id.silver_seek_bar)).setProgress(goalsAndTasks.getSilver());
    }

    private void saveOrEditTodo(TodoType todoType, boolean shouldSave, int silverAmount, Map<TodoTag.Tag, Boolean> tagMap, Date deadlineDate, Date startDate, Date creationDate, Date notificationDateString, int notificationId) {
        switch (todoType) {
            case DAILY:
                saveOrEditDaily(shouldSave, silverAmount, tagMap, deadlineDate, startDate, creationDate, notificationDateString, notificationId);
                break;
            case TASK:
                saveOrEditTask(shouldSave, silverAmount, tagMap, deadlineDate, startDate, creationDate, notificationDateString, notificationId);
                break;
            case GOAL:
                saveOrEditGoal(shouldSave, silverAmount, tagMap, deadlineDate, startDate, creationDate, notificationDateString, notificationId);
                break;
            default:
                break;
        }
    }

    private void saveOrEditGoal(boolean shouldSave, int silverAmount, Map<TodoTag.Tag, Boolean> improvementType, Date deadlineDate, Date startDate, Date creationDate, Date notificationDateString, int notificationId) {
        if (mSelectedEndDate == null) {
            endDateLayout.setError("Select a Deadline Date");
            return;
        } else if ((mSelectedEndDate.getEndDate().compareTo(Calendar.getInstance())) < 0) {
            endDateLayout.setError("Deadline cannot be before or equal to current date");
            return;
        }
        Calendar calendarDeadline = mSelectedEndDate.getEndDate();
        deadlineDate = calendarDeadline.getTime();
        if (shouldSave) {
            saveTodo(TodoTypes.TodoType.HABIT, silverAmount, improvementType, deadlineDate, startDate, creationDate, notificationDateString, notificationId);
        } else {
            updateTodo(silverAmount, improvementType, deadlineDate, startDate, creationDate, notificationDateString, notificationId);
        }
    }

    private void saveOrEditTask(boolean shouldSave, int silverAmount, Map<TodoTag.Tag, Boolean> tagMap, Date deadlineDate, Date startDate, Date creationDate, Date notificationDateString, int notificationId) {
        if (mSelectedStartDate == null) {
            endDateLayout.setError("Select a Start Date");
            return;
        } else if (DateUtils.getZeroTimeDate(mSelectedStartDate.getEndDate().getTime()).compareTo(DateUtils.getZeroTimeDate(Calendar.getInstance().getTime())) < 0) {
            endDateLayout.setError("Start date cannot be before current date");
            return;
        }
        Calendar calendarStartDate = mSelectedStartDate.getEndDate();
        startDate = calendarStartDate.getTime();

        if (shouldSave) {
            saveTodo(TodoTypes.TodoType.TASK, silverAmount, tagMap, deadlineDate, startDate, creationDate, notificationDateString, notificationId);
        } else {
            updateTodo(silverAmount, tagMap, deadlineDate, startDate, creationDate, notificationDateString, notificationId);
        }
    }

    private void saveOrEditDaily(boolean shouldSave, int silverAmount, Map<TodoTag.Tag, Boolean> tagMap, Date deadlineDate, Date startDate, Date creationDate, Date notificationDate, int notificationId) {
        if (shouldSave) {
            saveTodo(TodoTypes.TodoType.DAILY, silverAmount, tagMap, deadlineDate, startDate, creationDate, notificationDate, notificationId);
        } else {
            updateTodo(silverAmount, tagMap, deadlineDate, startDate, creationDate, notificationDate, notificationId);
        }
    }

    private void saveTodo(TodoTypes.TodoType todoType, int silverAmount, Map<TodoTag.Tag, Boolean> tagMap, Date deadlineDate, Date startDate, Date creationDate, Date notificationDate, int notificationId) {
        /*databaseHelper.updateTodo(id,
                userTaskGoalDescription.getText().toString(),
                improvementCategory.getSelectedItem().toString(),
                userTaskGoalTitle.getText().toString(),
                silverAmount,
                improvementType,
                deadlineDate,
                creationDate,
                startDate,
                notificationDateString,
                notificationId);*/

        TodoTag todoTag = new TodoTag (
            tagMap.get(TodoTag.Tag.WORK),
            tagMap.get(TodoTag.Tag.EXERCISE),
            tagMap.get(TodoTag.Tag.HEALTH_WELLNESS),
            tagMap.get(TodoTag.Tag.SCHOOL),
            tagMap.get(TodoTag.Tag.TEAMS),
            tagMap.get(TodoTag.Tag.CHORES),
            tagMap.get(TodoTag.Tag.CREATIVITY),
            tagMap.get(TodoTag.Tag.HOME),
            tagMap.get(TodoTag.Tag.OTHER)
        );

        switch (todoType) {
            case TASK:
                TaskTodo taskTodo = new TaskTodo (
                        userTaskGoalTitle.getText().toString(),
                        userTaskGoalDescription.getText().toString(),
                        silverAmount,
                        0,
                        false,
                        deadlineDate,
                        notificationDate,
                        todoType
                );
                todoRepository.insertTaskTodo(todoTag, false, null, (taskTodo));
                break;
            case DAILY:
                break;
            case HABIT:
                break;
        }

        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("ToDo-Update")
                .setAction(userTaskGoalTitle.getText().toString())
                .setLabel(userTaskGoalDescription.getText().toString())
                .build());
    }

    private void updateTodo(int silverAmount, Map<TodoTag.Tag, Boolean> improvementType, Date deadlineDate, Date startDate, Date creationDate, Date notificationDateString, int notificationId) {
/*        databaseHelper.addData(userTaskGoalDescription.getText().toString(),
                improvementCategory.getSelectedItem().toString(),
                userTaskGoalTitle.getText().toString(),
                silverAmount,
                improvementType,
                deadlineDate,
                creationDate,
                startDate,
                notificationDateString,
                0,
                0,
                notificationId);*/
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("ToDo-Addition")
                .setAction(userTaskGoalTitle.getText().toString())
                .setLabel(userTaskGoalDescription.getText().toString())
                .build());
    }

    private Map<TodoTag.Tag, Boolean> getImprovementTypeMap() {
        final Map<TodoTag.Tag, Boolean> improvementTypeMap = new HashMap<TodoTag.Tag, Boolean>();
        improvementTypeMap.put(TodoTag.Tag.EXERCISE, userHealthExercise.isChecked());
        improvementTypeMap.put(TodoTag.Tag.WORK, userWork.isChecked());
        improvementTypeMap.put(TodoTag.Tag.SCHOOL, userSchool.isChecked());
        improvementTypeMap.put(TodoTag.Tag.HOME, userFamilyFriends.isChecked());
        improvementTypeMap.put(TodoTag.Tag.CREATIVITY, userLearning.isChecked());
        improvementTypeMap.put(TodoTag.Tag.HEALTH_WELLNESS, true);
        improvementTypeMap.put(TodoTag.Tag.TEAMS, true);
        improvementTypeMap.put(TodoTag.Tag.CHORES, true);
        improvementTypeMap.put(TodoTag.Tag.OTHER, userOther.isChecked());

        return improvementTypeMap;
    }
}


