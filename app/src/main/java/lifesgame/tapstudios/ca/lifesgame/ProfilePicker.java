package lifesgame.tapstudios.ca.lifesgame;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import lifesgame.tapstudios.ca.lifesgame.activity.LoginActivity;
import lifesgame.tapstudios.ca.lifesgame.activity.MainActivity;
import lifesgame.tapstudios.ca.lifesgame.helper.DatabaseHelper;

public class ProfilePicker extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_INVITE = 0;
    private ImageButton chooseCustomProfilePictureButton;
    private ImageButton saveProfilePictureButton;
    private ImageButton inviteButton;
    private ImageButton cropButton;
    private TextView username;
    private DatabaseHelper databaseHelper;
    private ImageView chosenProfilePic;
    private Bitmap profilePicture;
    private Tracker tracker;
    Uri profilePictureUri;
    private ProgressDialog dialog;
    private LinearLayout signOutLl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_picker);
        databaseHelper = new DatabaseHelper(getApplicationContext());
        chooseCustomProfilePictureButton = (ImageButton) findViewById(R.id.choose_custom_profile_picture);
        saveProfilePictureButton = (ImageButton) findViewById(R.id.save_profile_picture);
        inviteButton = (ImageButton) findViewById(R.id.invite);
        cropButton = (ImageButton) findViewById(R.id.crop);
        chosenProfilePic = (ImageView) findViewById(R.id.chosen_profile_picture);
        profilePicture = null;
        username = (TextView) findViewById(R.id.username);
        signOutLl = (LinearLayout) findViewById(R.id.signout_ll);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        dialog = new ProgressDialog(this);
        if (user != null) {
            username.setText(user.getDisplayName());
        }

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        tracker = application.getDefaultTracker();
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("Category")
                .setAction("ProfilePicker")
                .build());

        tracker.setScreenName("ProfilePicker");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());

        setupButtonListeners();
        setupProfilePicture();
    }

    private void setupButtonListeners() {
        chooseCustomProfilePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseCustomProfilePicture();
            }
        });

        saveProfilePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfilePicture();
            }
        });

        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onInviteClicked();
            }
        });

        cropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropImage();
            }
        });

        signOutLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOutDialog();
            }
        });

    }

    private void cropImage() {
        // start cropping activity for pre-acquired image saved on the device
        if (profilePictureUri != null) {
            CropImage.activity(profilePictureUri)
                    .start(this);
        } else {
            Toast.makeText(this, "Select an image...", Toast.LENGTH_SHORT).show();
        }
    }

    private void signOutDialog() {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("This will log you out of your account!")
                .setConfirmText("Yes")
                .setCancelText("Cancel")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        signOut();
                    }
                })
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    private void chooseCustomProfilePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void setupProfilePicture() {
        final Bitmap profilePicture = databaseHelper.getProfilePicture();
        if (profilePicture != null) {
            this.profilePicture = profilePicture;
            chosenProfilePic.setImageBitmap(profilePicture);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            profilePictureUri = data.getData();

            try {
                profilePicture = MediaStore.Images.Media.getBitmap(getContentResolver(), profilePictureUri);
                Bitmap resized;
                resized = Bitmap.createScaledBitmap(profilePicture, (int) (profilePicture.getWidth() * 0.6), (int) (profilePicture.getHeight() * 0.6), true);

                if (((int) resized.getHeight()) > 900) {
                    resized = Bitmap.createScaledBitmap(profilePicture, resized.getWidth(), 900, true);
                }
                if (((int) resized.getWidth()) > 900) {
                    resized = Bitmap.createScaledBitmap(profilePicture, 900, resized.getHeight(), true);
                }
                profilePicture = resized;
                chosenProfilePic.setImageBitmap(resized);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "An error occurred...", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Get the invitation IDs of all sent messages
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
            } else {
                // Sending failed or it was canceled, show failure message to the user
                // ...
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    profilePicture = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                    Bitmap resized;
                    resized = Bitmap.createScaledBitmap(profilePicture, (int) (profilePicture.getWidth() * 0.6), (int) (profilePicture.getHeight() * 0.6), true);

                    if (((int) resized.getHeight()) > 900) {
                        resized = Bitmap.createScaledBitmap(profilePicture, resized.getWidth(), 900, true);
                    }
                    if (((int) resized.getWidth()) > 900) {
                        resized = Bitmap.createScaledBitmap(profilePicture, 900, resized.getHeight(), true);
                    }
                    profilePicture = resized;
                    chosenProfilePic.setImageBitmap(resized);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void saveProfilePicture() {
        if (profilePicture != null) {
            this.dialog.setMessage("Saving Profile Picture...");
            this.dialog.show();
            databaseHelper.insertProfilePicture(profilePicture);
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    private void onInviteClicked() {
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setDeepLink(Uri.parse("/link"))
                .setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
                .setCallToActionText(getString(R.string.invitation_cta))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }
}
