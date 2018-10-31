package ch.FOW_Collection.presentation.cardDetails.createRating;

import android.net.Uri;
import android.util.Log;

import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.Nullable;
import ch.FOW_Collection.data.repositories.RatingsRepository;
import ch.FOW_Collection.domain.models.Card;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Collections;
import java.util.Date;

import androidx.lifecycle.ViewModel;
import ch.FOW_Collection.data.parser.EntityClassSnapshotParser;
import ch.FOW_Collection.domain.models.Rating;

public class CreateRatingViewModel extends ViewModel {

    private static final String TAG = "CreateRatingViewModel";

    private EntityClassSnapshotParser<Rating> parser = new EntityClassSnapshotParser<>(Rating.class);
    private Card item;
    private Rating oldRating;
    private Uri photo;

    public Card getItem() {
        return item;
    }
    public Rating getOldRating() { return oldRating; }
    public void setItem(Card item) {
        this.item = item;
    }
    public void setOldRating(Rating rating) { oldRating = rating; }
    public Uri getPhoto() {
        return photo;
    }

    public void setPhoto(Uri photo) {
        this.photo = photo;
    }

    public Task<Uri> saveRating(Card item, float rating, String comment, Uri localPhotoUri, @Nullable Rating oldRating, @Nullable TextView dialogLabel, @Nullable ProgressBar progressBar) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        assert item != null;

        if (oldRating.getPhoto().equals(localPhotoUri.toString())) {
            localPhotoUri = null;
        }

        if (dialogLabel != null) {
            dialogLabel.setText("Lade Bild hoch...");
        }

        return uploadFileToFirebaseStorage(localPhotoUri, progressBar).continueWithTask((Task<Uri> task) -> {
            String photoUrl = null;

            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
                photoUrl = downloadUri.toString();
            } else if (task.isCanceled()) {
                // the user did not take a photo, that's ok!
            } else {
                throw task.getException();
            }

            if (dialogLabel != null) {
                dialogLabel.setText("Speichere Beurteilung...");
            }

            if (oldRating != null) {
                photoUrl = oldRating.getPhoto();
            }

            // When we are offline, the Task from Firestore does not fire until real write on Database.
            // (It will be written immediately on cache)

            Rating newRating = new Rating(null, item.getId(), null, user.getUid(), null, photoUrl, rating, comment, Collections.emptyMap(), new Date());
            Log.i(TAG, "Adding new rating: " + newRating.toString());
            new RatingsRepository().putRating(newRating); //FirebaseFirestore.getInstance().collection("ratings").add(newRating);
            return task;
        }); /*.continueWithTask(task -> {
            if (task.isSuccessful()) {
                return task.getResult().get();
            } else {
                throw task.getException();
            }
        }).continueWithTask(task -> {
            if (task.isSuccessful()) {
                return Tasks.forResult(parser.parseSnapshot(task.getResult()));
            } else {
                throw task.getException();
            }
        });*/
    }

    private Task<Uri> uploadFileToFirebaseStorage(Uri localPhotoUri, @Nullable ProgressBar progressBar) {
        if (localPhotoUri == null || localPhotoUri.toString().startsWith("{}")) {
            return Tasks.forCanceled();
        }

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference imageRef = storageRef.child("userImages/" + localPhotoUri.getLastPathSegment());
        Log.i(TAG, "Uploading image " + localPhotoUri);

        return imageRef.putFile(localPhotoUri)
                .addOnProgressListener(snapshot -> {
                    progressBar.setProgress(50);
                    if (progressBar != null) {
                        int progress = Math.round((snapshot.getBytesTransferred() / snapshot.getTotalByteCount()) * 100);
                        progressBar.setProgress(progress);
                    }
                })
                .addOnFailureListener(exception -> {
                    Log.e(TAG, "Uploading image failed", exception);
                })
                .addOnSuccessListener(taskSnapshot -> {
                    Log.i(TAG, "Uploading image successful: " + taskSnapshot.getMetadata().getName());
                })
                .continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return imageRef.getDownloadUrl();
                });
    }
}