package lifesgame.tapstudios.ca.lifesgame.fragment;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.shawnlin.numberpicker.NumberPicker;
import com.timqi.sectorprogressview.ColorfulRingProgressView;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import info.hoang8f.widget.FButton;
import lifesgame.tapstudios.ca.lifesgame.AnalyticsApplication;
import lifesgame.tapstudios.ca.lifesgame.R;
import lifesgame.tapstudios.ca.lifesgame.fragment.CompletedToDoFragment;
import lifesgame.tapstudios.ca.lifesgame.fragment.ImprovementTypeFragment;
import lifesgame.tapstudios.ca.lifesgame.fragment.SilverFragment;
import lifesgame.tapstudios.ca.lifesgame.helper.DatabaseHelper;
import lifesgame.tapstudios.ca.lifesgame.model.PomodoroTimerStates;
import lifesgame.tapstudios.ca.lifesgame.utility.PrefUtils;
import lifesgame.tapstudios.ca.lifesgame.utility.TimeReceiver;

import static lifesgame.tapstudios.ca.lifesgame.model.PomodoroTimerStates.PAUSED;
import static lifesgame.tapstudios.ca.lifesgame.model.PomodoroTimerStates.STARTED;
import static lifesgame.tapstudios.ca.lifesgame.model.PomodoroTimerStates.STOPPED;

public class PomodoroTimerFragment extends Fragment {
    @BindView(R.id.horizontal_number_picker) NumberPicker numberPicker;
    @BindView(R.id.btn_start_timer) FButton btnStartTimer;
    @BindView(R.id.btn_stop_timer) FButton btnStopTimer;
    @BindView(R.id.pomodoroTimerProgress) ColorfulRingProgressView pomodoroTimerProgess;
    @BindView(R.id.minuteTv) TextView minuteTv;
    @BindView(R.id.secondTv) TextView secondTv;

    private PrefUtils prefUtils;
    private View pomodoroView;
    private Tracker tracker;
    private int timerValue;
    private CountDownTimer countDownTimer;
    private float progressViewMax;
    private float remainingProgressViewMax;
    private PomodoroTimerStates pomodoroTimerState;

    public PomodoroTimerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        pomodoroView = inflater.inflate(R.layout.activity_pomodoro_timer, container, false);
        ButterKnife.bind(this, pomodoroView);
        pomodoroTimerState = STOPPED;
        btnStartTimer.setText("Start");
        progressViewMax = 0;
        remainingProgressViewMax = 0;

        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        tracker = application.getDefaultTracker();
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Category")
                .setAction("Pomodoro")
                .build());

        tracker.setScreenName("PomodoroTimer");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());

        timerValue = 1;
        setupListeners();
        prefUtils = new PrefUtils(getActivity());

        return pomodoroView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //initializing a countdown timer
        initTimer();
        removeAlarmManager();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (pomodoroTimerState == PAUSED) {
            countDownTimer.cancel();
            setAlarmManager();
        }
    }

    public void removeAlarmManager() {
        Intent intent = new Intent(getActivity(), TimeReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        am.cancel(sender);
    }

    private long getNow() {
        Calendar rightNow = Calendar.getInstance();
        return rightNow.getTimeInMillis()/1000;
    }

    public void setAlarmManager() {
        long wakeUpTime = ((long) prefUtils.getStartedTime()*1000 +  (long) progressViewMax);
        AlarmManager am = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), TimeReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        am.set(AlarmManager.RTC_WAKEUP, wakeUpTime, sender);
    }

    private void setupListeners() {
        btnStartTimer.setButtonColor(getResources().getColor(R.color.fbutton_color_emerald));
        btnStartTimer.setShadowColor(getResources().getColor(R.color.fbutton_color_green_sea));
        btnStartTimer.setShadowEnabled(true);
        btnStartTimer.setShadowHeight(8);
        btnStartTimer.setCornerRadius(15);
        btnStartTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (pomodoroTimerState) {
                    case STOPPED:
                        timerValue = numberPicker.getValue();
                        prefUtils.setStartedTime((int) getNow());
                        progressViewMax = timerValue * 60 * 1000;
                        remainingProgressViewMax = timerValue * 60 * 1000;
                        pomodoroTimerProgess.setPercent(100);
                        countDownTimer(timerValue*60*1000);
                        pomodoroTimerState = PAUSED;
                        btnStartTimer.setText("Pause");
                        break;
                    case STARTED:
                        countDownTimer((int) remainingProgressViewMax);
                        pomodoroTimerState = PAUSED;
                        btnStartTimer.setText("Pause");
                        break;
                    case PAUSED:
                        countDownTimer.cancel();
                        pomodoroTimerState = STARTED;
                        btnStartTimer.setText("Start");
                        break;
                }
            }
        });

        btnStopTimer.setButtonColor(getResources().getColor(R.color.fbutton_color_alizarin));
        btnStopTimer.setShadowColor(getResources().getColor(R.color.fbutton_color_pomegranate));
        btnStopTimer.setShadowEnabled(true);
        btnStopTimer.setShadowHeight(8);
        btnStopTimer.setCornerRadius(15);
        btnStopTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pomodoroTimerState = STOPPED;
                if(countDownTimer != null) {
                    countDownTimer.cancel();
                }
                pomodoroTimerProgess.setPercent(0);
                minuteTv.setText("0");
                secondTv.setText("00");
                btnStartTimer.setText("Start");
            }
        });
    }

    private void countDownTimer(int countdownTime) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(countdownTime, 1000) {

            public void onTick(long millisUntilFinished) {
                if (pomodoroTimerState != PAUSED) {
                    pomodoroTimerState = PAUSED;
                    btnStartTimer.setText("Pause");
                }
                remainingProgressViewMax = millisUntilFinished;
                String minutes = String.valueOf((int) TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished));
                String seconds = String.valueOf((int) (TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                updateCircularProgress(progressViewMax, remainingProgressViewMax);
                minuteTv.setText(minutes);
                secondTv.setText(seconds);

            }

            public void onFinish() {
                pomodoroTimerState = STOPPED;
                updateCircularProgress(100, 0);
                secondTv.setText("00");
                minuteTv.setText("0");
                prefUtils.setStartedTime(0);
                vibratePhone(500);
            }
        };
        countDownTimer.start();
    }

    private void initTimer() {
        long startTime = prefUtils.getStartedTime();
        if (pomodoroTimerState == STARTED) {
            return;
        }
        if (startTime > 0) {
            remainingProgressViewMax = (int) (progressViewMax - (getNow()*1000 - startTime*1000));
            if (remainingProgressViewMax <= 0) {
                // TIMER EXPIRED
                remainingProgressViewMax = 0;
                pomodoroTimerState = STOPPED;
                onTimerFinish();
            } else {
                countDownTimer((int) remainingProgressViewMax);
                pomodoroTimerState = PAUSED;
            }
        } else {
            remainingProgressViewMax = 0;
            pomodoroTimerState = STOPPED;
        }
    }


    private void vibratePhone(int milliseconds) {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        Ringtone r = RingtoneManager.getRingtone(getActivity(), notification);
        r.play();
        v.vibrate(milliseconds);
    }

    private void updateCircularProgress(float max, float remaining) {
        float percentage = (remaining/max)*100;
        pomodoroTimerProgess.setPercent(percentage);
    }

    private void onTimerFinish() {
        prefUtils.setStartedTime(0);
        remainingProgressViewMax = 0;
        secondTv.setText("00");
        minuteTv.setText("0");
        updateCircularProgress(progressViewMax, remainingProgressViewMax);
    }
}
