package lifesgame.tapstudios.ca.lifesgame.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import info.hoang8f.widget.FButton;
import lifesgame.tapstudios.ca.lifesgame.R;

public class ChangePasswordActivity extends AppCompatActivity {

    private TextInputEditText email;
    private TextInputLayout emailLl;

    private FButton userAcceptPasswordChange;
    private FButton userCancelPasswordChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        email = (TextInputEditText) findViewById(R.id.user_email);
        emailLl = (TextInputLayout) findViewById(R.id.user_email_Ll);

        setupListeners();
    }

    private void setupListeners() {
        userAcceptPasswordChange = (FButton) findViewById(R.id.btn_user_accept_password_reset);
        userAcceptPasswordChange.setButtonColor(getResources().getColor(R.color.fbutton_color_emerald));
        userAcceptPasswordChange.setShadowColor(getResources().getColor(R.color.fbutton_color_green_sea));
        userAcceptPasswordChange.setShadowEnabled(true);
        userAcceptPasswordChange.setShadowHeight(8);
        userAcceptPasswordChange.setCornerRadius(15);

        userCancelPasswordChange = (FButton) findViewById(R.id.btn_user_cancel_password_reset);
        userCancelPasswordChange.setButtonColor(getResources().getColor(R.color.fbutton_color_alizarin));
        userCancelPasswordChange.setShadowColor(getResources().getColor(R.color.fbutton_color_pomegranate));
        userCancelPasswordChange.setShadowEnabled(true);
        userCancelPasswordChange.setShadowHeight(8);
        userCancelPasswordChange.setCornerRadius(15);

        userAcceptPasswordChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!verifyInformation()) {
                    return;
                }
                changePassword();
            }
        });

        userCancelPasswordChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private Boolean verifyInformation() {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String emailString = email.getText().toString();
        if (!emailString.matches(emailPattern)) {
            emailLl.setError("Not a valid Email address");
            return false;
        } else {
            emailLl.setError(null);
        }

        return true;
    }

    private void changePassword() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = email.getText().toString();

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ChangePasswordActivity.this, "Email Sent",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(ChangePasswordActivity.this, "An error occurred, try again",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
