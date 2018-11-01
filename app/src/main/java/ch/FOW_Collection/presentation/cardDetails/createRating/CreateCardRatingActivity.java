package ch.FOW_Collection.presentation.cardDetails.createRating;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;

import androidx.appcompat.app.AlertDialog;
import ch.FOW_Collection.domain.models.Rating;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.List;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.FOW_Collection.GlideApp;
import ch.FOW_Collection.R;
import ch.FOW_Collection.domain.models.Card;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

public class CreateCardRatingActivity extends AppCompatActivity {

    public static final String ITEM_CARD = "item_card";
    public static final String ITEM_RATING = "item_rating";
    public static final String RATING_VALUE = "rating";
    private static final String TAG = "CreateCardRatingActivit";
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.addRatingBar)
    RatingBar addRatingBar;

    @BindView(R.id.cardImage)
    ImageView photo;

    @BindView(R.id.avatar)
    ImageView avatar;

    @BindView(R.id.ratingText)
    EditText ratingText;

    @BindView(R.id.photoExplanation)
    TextView photoExplanation;

    private CreateRatingViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_rating);
        ButterKnife.bind(this);
        Nammu.init(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.title_activity_rating));

        Card itemCard = getIntent().getExtras().getParcelable(ITEM_CARD);
        float rating = getIntent().getExtras().getFloat(RATING_VALUE);

        model = ViewModelProviders.of(this).get(CreateRatingViewModel.class);
        model.setItem(itemCard);
        addRatingBar.setRating(rating);

        Rating itemRating = getIntent().getExtras().getParcelable(ITEM_RATING);
        if (itemRating != null) {
            model.setOldRating(itemRating);
            loadRating(itemRating);
        }

        int permissionCheck =
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Nammu.askForPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionCallback() {
                @Override
                public void permissionGranted() {
                }

                @Override
                public void permissionRefused() {
                }
            });
        }

        EasyImage.configuration(this).setImagesFolderName("FOW_Collection");

        photo.setOnClickListener(view -> {
            EasyImage.openChooserWithDocuments(CreateCardRatingActivity.this, "", 0);
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            Uri photoUrl = user.getPhotoUrl();
            GlideApp.with(this).load(photoUrl).apply(new RequestOptions().circleCrop()).into(avatar);
        }

        if (model.getPhoto() != null) {
            photo.setImageURI(model.getPhoto());
            photoExplanation.setText(null);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
                e.printStackTrace();
            }

            @Override
            public void onImagesPicked(List<File> imageFiles, EasyImage.ImageSource source, int type) {
                Log.i("CreateCardRatingActivit", imageFiles.toString());

                UCrop.Options options = new UCrop.Options() {
                    {
                        setToolbarTitle("Foto zuschneiden");
                        setToolbarColor(getResources().getColor(R.color.colorPrimary));
                        setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
                        setActiveWidgetColor(getResources().getColor(R.color.colorAccent));
                        setCropFrameColor(getResources().getColor(R.color.colorAccent));
                        setCropGridColor(getResources().getColor(R.color.colorAccent));
                        setDimmedLayerColor(getResources().getColor(R.color.windowBackgroundColor));
                        setHideBottomControls(true);
                    }
                };

                UCrop.of(Uri.fromFile(imageFiles.get(0)),
                        Uri.fromFile(new File(getCacheDir(), "image_" + UUID.randomUUID().toString())))
                        .withAspectRatio(1, 1).withMaxResultSize(1024, 1024).withOptions(options)
                        .start(CreateCardRatingActivity.this);
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                //Cancel handling, you might wanna remove taken photo if it was canceled
                if (source == EasyImage.ImageSource.CAMERA_IMAGE) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(CreateCardRatingActivity.this);
                    if (photoFile != null)
                        photoFile.delete();
                }
            }
        });

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            handleCropResult(data);
        } else if (resultCode == UCrop.RESULT_ERROR) {
            handleCropError(data);
        }
    }

    private void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            model.setPhoto(resultUri);
            photo.setImageURI(resultUri);
            photoExplanation.setText(null);
        }
    }

    private void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Log.e(TAG, "handleCropError: ", cropError);
            Toast.makeText(this, cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, R.string.toast_unexpected_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_rating_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                saveRating();
                return true;
            case android.R.id.home:
                if (getParentActivityIntent() == null) {
                    onBackPressed();
                } else {
                    NavUtils.navigateUpFromSameTask(this);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveRating() {
        float rating = addRatingBar.getRating();
        String comment = ratingText.getText().toString();
        // TODO show a spinner!
        // TODO return the new rating to update the new average immediately

        View view = getLayoutInflater().inflate(R.layout.dialog_loading, null);
        final Dialog dialog=new Dialog(this, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        TextView dialogLabel = view.findViewById(R.id.dialogLabel);
        ProgressBar progressBar = view.findViewById(R.id.dialogProgressBar);
        dialogLabel.setText("Speichere Beurteilung...");

        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        dialog.show();

        model.saveRating(model.getItem(), rating, comment, model.getPhoto(), model.getOldRating(), dialogLabel, progressBar)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() || task.isCanceled()) {
                        dialog.hide();
                        Intent intent = new Intent();
                        // todo When offline, nothing happens: we dont get the Rating back or something... WorkAround: ignore it..
                        //intent.putExtra(ITEM_RATING, rating);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                })
                .addOnFailureListener(error -> {
                    dialog.hide();
                    AlertDialog.Builder errorDialog = new AlertDialog.Builder(this);
                    errorDialog.setTitle("Fehler beim speichern");
                    errorDialog.setMessage("Beim speichern ist ein Fehler aufgetretten:\n\n" + error.getMessage());
                    errorDialog.show();
                });
    }

    private void loadRating(Rating rating) {
        ratingText.setText(rating.getComment());

        if (rating.getPhoto() != null) {
            model.setPhoto(Uri.parse(rating.getPhoto()));
            photoExplanation.setText(null);
            GlideApp.with(this).load(rating.getPhoto()).into(photo);
        }
    }
}
