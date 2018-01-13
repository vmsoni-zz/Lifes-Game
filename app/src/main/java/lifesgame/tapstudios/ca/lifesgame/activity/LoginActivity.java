package lifesgame.tapstudios.ca.lifesgame.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.orangegangsters.lollipin.lib.managers.AppLock;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import lifesgame.tapstudios.ca.lifesgame.AnalyticsApplication;
import lifesgame.tapstudios.ca.lifesgame.R;
import lifesgame.tapstudios.ca.lifesgame.User;
import lifesgame.tapstudios.ca.lifesgame.helper.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton signInButton;
    private static final int RC_SIGN_IN = 9001;
    private Tracker tracker;
    private DatabaseHelper databaseHelper;
    private Integer expiredGoalsAndTasksCount;
    private LinearLayout skipSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        databaseHelper = new DatabaseHelper(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signInButton = findViewById(R.id.sign_in_button);
        skipSignIn = (LinearLayout) findViewById(R.id.skip_sign_ll);
        setupLoginButtonOnClick();

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        tracker = application.getDefaultTracker();
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("App Opened")
                .build());

        tracker.setScreenName("LoginScreen");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        expiredGoalsAndTasksCount = databaseHelper.resetExpiredGoalsAndTasks();
        if (account != null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("EXPIRED_TODO_COUNT", expiredGoalsAndTasksCount);
            intent.putExtra("PASSCODE_SET", databaseHelper.passcodeSet());
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        // do nothing.
    }

    private void setupLoginButtonOnClick() {
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        skipSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainActivity();
            }
        });
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("EXPIRED_TODO_COUNT", expiredGoalsAndTasksCount);
        intent.putExtra("PASSCODE_SET", databaseHelper.passcodeSet());
        startActivity(intent);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            Intent intent = new Intent(this, MainActivity.class);
            if (expiredGoalsAndTasksCount > 0) {
                intent.putExtra("EXPIRED_TODO_COUNT", expiredGoalsAndTasksCount);
                intent.putExtra("PASSCODE_SET", databaseHelper.passcodeSet());
            }
            startActivity(intent);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(this, "Login Failed:\nTry again later", Toast.LENGTH_SHORT).show();
        }
    }
}
