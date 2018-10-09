package lifesgame.tapstudios.ca.lifesgame.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import lifesgame.tapstudios.ca.lifesgame.helper.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton signInGoogle;
    private static final int RC_SIGN_IN = 9001;
    private Tracker tracker;
    private DatabaseHelper databaseHelper;
    private Integer expiredGoalsAndTasksCount;
    private LinearLayout skipSignIn;
    private LinearLayout forgotPassword;
    private FirebaseAuth mAuth;
    private Button signup;
    private Button loginEmailPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(this);

/*        setContentView(R.layout.activity_login);
        databaseHelper = new DatabaseHelper(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();
        signInGoogle = findViewById(R.id.sign_in_google);
        signInGoogle.setSize(SignInButton.SIZE_WIDE);
        skipSignIn = (LinearLayout) findViewById(R.id.skip_sign_ll);
        forgotPassword = (LinearLayout) findViewById(R.id.forgot_password_Ll);
        signup = (Button) findViewById(R.id.btn_signup);
        loginEmailPassword = (Button) findViewById(R.id.btn_login_email_password);
        setupLoginButtonOnClick();

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        tracker = application.getDefaultTracker();
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("App Opened")
                .build());

        tracker.setScreenName("LoginScreen");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());*/
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("PASSCODE_SET", databaseHelper.passcodeSet());
        startActivity(intent);
    }

/*    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        expiredGoalsAndTasksCount = databaseHelper.resetExpiredGoalsAndTasks();
        if (currentUser != null && currentUser.isEmailVerified()) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("EXPIRED_TODO_COUNT", expiredGoalsAndTasksCount);
            intent.putExtra("PASSCODE_SET", databaseHelper.passcodeSet());
            startActivity(intent);
        }
    }*/

    @Override
    public void onBackPressed() {
        // do nothing.
    }

    private void setupLoginButtonOnClick() {
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPassword();
            }
        });
        signInGoogle.setOnClickListener(new View.OnClickListener() {
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
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSignupActivity();
            }
        });
        loginEmailPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLoginEmailPasswordActivity();
            }
        });
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("EXPIRED_TODO_COUNT", expiredGoalsAndTasksCount);
        intent.putExtra("PASSCODE_SET", databaseHelper.passcodeSet());
        startActivity(intent);
    }

    private void goToSignupActivity() {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

    private void goToLoginEmailPasswordActivity() {
        Intent intent = new Intent(this, LoginEmailPasswordActivity.class);
        startActivity(intent);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void forgotPassword() {
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account, task);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct, final Task<GoogleSignInAccount> googleSignInAccountTask) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            handleSignInResult(googleSignInAccountTask);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Login Failed:\nTry again later", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
