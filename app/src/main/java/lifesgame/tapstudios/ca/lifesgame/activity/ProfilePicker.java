package lifesgame.tapstudios.ca.lifesgame.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import lifesgame.tapstudios.ca.lifesgame.AnalyticsApplication;
import lifesgame.tapstudios.ca.lifesgame.adapter.CustomGridAdapter;
import lifesgame.tapstudios.ca.lifesgame.R;
import lifesgame.tapstudios.ca.lifesgame.helper.DatabaseHelper;

public class ProfilePicker extends AppCompatActivity {
    @BindView(R.id.choose_custom_profile_picture) ImageButton chooseCustomProfilePictureButton;
    @BindView(R.id.save_profile_picture) ImageButton saveProfilePictureButton;
    @BindView(R.id.invite) ImageButton inviteButton;
    @BindView(R.id.crop) ImageButton cropButton;
    @BindView(R.id.username) TextView username;
    @BindView(R.id.chosen_profile_picture) ImageView chosenProfilePic;
    @BindView(R.id.signout_ll) LinearLayout signOutLl;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_INVITE = 0;
    private DatabaseHelper databaseHelper;
    private Bitmap profilePicture;
    private Tracker tracker;
    Uri profilePictureUri;
    private ProgressDialog dialog;
    private int selectedCustomImage;
    private static final Integer[] customImageResources = { R.drawable.chicken,
            R.drawable.cheetah, R.drawable.elephant,
            R.drawable.lion_female, R.drawable.lion_male,
            R.drawable.monkey, R.drawable.monkey_1,
            R.drawable.panda, R.drawable.penguin, R.drawable.puma, R.drawable.tiger, R.drawable.zeebra };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_picker);
        ButterKnife.bind(this);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        profilePicture = null;
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
                displayImageSelectionPopup();
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
            Toast.makeText(this, "Select a custom image...", Toast.LENGTH_SHORT).show();
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
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
            } else {

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
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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

    private void displayImageSelectionPopup() {
        String title = "Type of Profile Picture";
        String[] filterChoices = {"Choose your own", "Pick from default"};
        Context context = this;

        new MaterialDialog.Builder(this)
                .title(title)
                .titleColor(getResources().getColor(R.color.colorPrimaryDark))
                .items(filterChoices)
                .itemsCallbackSingleChoice(
                        0,
                        (dialog, view, which, text) -> {
                            pickImageType(which);
                            return true;
                        })
                .backgroundColor(Color.WHITE)
                .positiveText("Choose")
                .negativeText("Cancel")
                .choiceWidgetColor(ColorStateList.valueOf(context.getResources().getColor(R.color.colorPrimary)))
                .contentColor(getResources().getColor(R.color.colorPrimaryDark))
                .show();
    }

    private void pickImageType(int filter) {
        switch (filter) {
            case 0:
                chooseCustomProfilePicture();
                break;
            case 1:
                chooseDefaultProfilePicture();
                break;
            default:
                break;
        }
    }

    private void chooseDefaultProfilePicture() {
        GridView gridView = new GridView(this);
        List<Integer> mList = new ArrayList<Integer>();
        for (int i = 1; i < 36; i++) {
            mList.add(i);
        }

        gridView.setAdapter(new CustomGridAdapter(this, customImageResources));
        gridView.setNumColumns(2);

        MaterialDialog chooseCustomImageDialog = new MaterialDialog.Builder(this)
                .title("Custom Profile Picture:")
                .titleColor(getResources().getColor(R.color.colorPrimaryDark))
                .customView(gridView, false)
                .itemsCallbackSingleChoice(
                        0,
                        (dialog, view, which, text) -> {
                            pickImageType(which);
                            return true;
                        })
                .negativeText("Cancel")
                .backgroundColor(Color.WHITE)
                .choiceWidgetColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)))
                .contentColor(getResources().getColor(R.color.colorPrimaryDark))
                .show();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chooseCustomImageDialog.dismiss();
                selectedCustomImage = position;
                profilePicture = BitmapFactory.decodeResource( getResources(), customImageResources[selectedCustomImage]);
                chosenProfilePic.setImageBitmap(profilePicture);
            }
        });
    }
}
