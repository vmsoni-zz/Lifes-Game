package lifesgame.tapstudios.ca.lifesgame.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import info.hoang8f.widget.FButton;
import lifesgame.tapstudios.ca.lifesgame.R;

public class SignupActivity extends AppCompatActivity {

    private TextInputEditText email;
    private TextInputEditText password;
    private TextInputEditText username;
    private TextInputEditText confirmPassword;

    private TextInputLayout emailLl;
    private TextInputLayout passwordLl;
    private TextInputLayout usernameLl;
    private TextInputLayout confirmPasswordLl;

    private FirebaseAuth mAuth;

    private FButton userAcceptSignup;
    private FButton userCancelSignup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        this.email = findViewById(R.id.user_email);
        this.password = findViewById(R.id.user_password);
        this.confirmPassword = findViewById(R.id.user_confirm_password);
        this.username = findViewById(R.id.user_username);
        this.emailLl = findViewById(R.id.user_email_Ll);
        this.passwordLl = findViewById(R.id.user_password_Ll);
        this.usernameLl = findViewById(R.id.user_username_Ll);
        this.confirmPasswordLl = findViewById(R.id.user_confirm_password_Ll);

        mAuth = FirebaseAuth.getInstance();
        setupListeners();
    }

    private void setupListeners() {
        userAcceptSignup = (FButton) findViewById(R.id.btn_user_accept_signup);
        userAcceptSignup.setButtonColor(getResources().getColor(R.color.fbutton_color_emerald));
        userAcceptSignup.setShadowColor(getResources().getColor(R.color.fbutton_color_green_sea));
        userAcceptSignup.setShadowEnabled(true);
        userAcceptSignup.setShadowHeight(8);
        userAcceptSignup.setCornerRadius(15);

        userCancelSignup = (FButton) findViewById(R.id.btn_user_cancel_signup);
        userCancelSignup.setButtonColor(getResources().getColor(R.color.fbutton_color_alizarin));
        userCancelSignup.setShadowColor(getResources().getColor(R.color.fbutton_color_pomegranate));
        userCancelSignup.setShadowEnabled(true);
        userCancelSignup.setShadowHeight(8);
        userCancelSignup.setCornerRadius(15);

        userAcceptSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!verifyInformation()) {
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        Toast.makeText(SignupActivity.this, "Signup Successful.",
                                                Toast.LENGTH_SHORT).show();

                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(username.getText().toString())
                                                .build();

                                        user.updateProfile(profileUpdates);

                                        user.sendEmailVerification()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                                            startActivity(intent);
                                                            Toast.makeText(SignupActivity.this, "Verification Email Sent", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }
                                    //updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(SignupActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                }

                                // ...
                            }
                        });
            }
        });

        userCancelSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private Boolean verifyInformation() {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String emailString = email.getText().toString();
        String confirmPasswordString = confirmPassword.getText().toString();
        String passwordString = password.getText().toString();
        String usernameString = username.getText().toString();

        if (usernameString.isEmpty() || usernameString == null) {
            usernameLl.setError("Username cannot be blank");
            return false;
        }
        else {
            usernameLl.setError(null);
        }

        if(emailString.isEmpty() || emailString == null) {
            emailLl.setError("Email cannot be blank");
            return false;
        }
        else {
            emailLl.setError(null);
        }

        if(passwordString.isEmpty() || passwordString == null) {
            passwordLl.setError("Password cannot be blank");
            return false;
        }
        else {
            passwordLl.setError(null);
        }

        if (!confirmPasswordString.equals(passwordString)) {
            confirmPasswordLl.setError("Password and Confirm password do not match");
            return false;
        }
        else {
            confirmPasswordLl.setError(null);
        }

        if(!emailString.matches(emailPattern)) {
            emailLl.setError("Not a valid Email address");
            return false;
        }
        else {
            emailLl.setError(null);
        }

        return true;
    }
}