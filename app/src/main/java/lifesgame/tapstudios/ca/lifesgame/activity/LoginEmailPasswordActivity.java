package lifesgame.tapstudios.ca.lifesgame.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import info.hoang8f.widget.FButton;
import lifesgame.tapstudios.ca.lifesgame.R;

public class LoginEmailPasswordActivity extends AppCompatActivity {

    private TextInputEditText email;
    private TextInputEditText password;

    private TextInputLayout emailLl;
    private TextInputLayout passwordLl;

    private FButton userAcceptLogin;
    private FButton userCancelLogin;
    private LinearLayout forgotPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email_password);

        this.email = findViewById(R.id.user_email);
        this.password = findViewById(R.id.user_password);
        this.emailLl = findViewById(R.id.user_email_Ll);
        this.passwordLl = findViewById(R.id.user_password_Ll);
        this.forgotPassword = (LinearLayout) findViewById(R.id.forgot_password_Ll);
        mAuth = FirebaseAuth.getInstance();
        setupListeners();
    }

    private void setupListeners() {
        userAcceptLogin = (FButton) findViewById(R.id.btn_user_accept_login_email_password);
        userAcceptLogin.setButtonColor(getResources().getColor(R.color.fbutton_color_emerald));
        userAcceptLogin.setShadowColor(getResources().getColor(R.color.fbutton_color_green_sea));
        userAcceptLogin.setShadowEnabled(true);
        userAcceptLogin.setShadowHeight(8);
        userAcceptLogin.setCornerRadius(15);

        userCancelLogin = (FButton) findViewById(R.id.btn_user_cancel_login);
        userCancelLogin.setButtonColor(getResources().getColor(R.color.fbutton_color_alizarin));
        userCancelLogin.setShadowColor(getResources().getColor(R.color.fbutton_color_pomegranate));
        userCancelLogin.setShadowEnabled(true);
        userCancelLogin.setShadowHeight(8);
        userCancelLogin.setCornerRadius(15);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPassword();
            }
        });

        userAcceptLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!verifyInformation()) {
                    return;
                }
                mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(LoginEmailPasswordActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        Toast.makeText(LoginEmailPasswordActivity.this, "Login Successful.",
                                                Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginEmailPasswordActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }
                                    //updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(LoginEmailPasswordActivity.this, "Incorrect Email Address or Password",
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                }

                                // ...
                            }
                        });
            }
        });

        userCancelLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginEmailPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void forgotPassword() {
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        startActivity(intent);
    }

    private Boolean verifyInformation() {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String emailString = email.getText().toString();
        String passwordString = password.getText().toString();

        if (emailString == null) {
            emailLl.setError("Email cannot be blank");
            return false;
        }

        if (passwordString == null) {
            passwordLl.setError("Password cannot be blank");
            return false;
        }

        if (!emailString.matches(emailPattern)) {
            emailLl.setError("Not a valid Email address");
            return false;
        }

        return true;
    }
}
