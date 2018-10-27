package ch.FOW_Collection.presentation.splash;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import ch.FOW_Collection.R;
import ch.FOW_Collection.domain.models.User;
import ch.FOW_Collection.presentation.MainActivity;

public class SplashScreenActivity extends AppCompatActivity {

    private static final String TAG = "SplashScreenActivity";

    private static final int RC_SIGN_IN = 123;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Log.i(TAG, "No user found, redirect to Login screen");
            List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.GoogleBuilder().build());
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setIsSmartLockEnabled(false)
                    .setAvailableProviders(providers).setLogo(R.drawable.fow_logo)
                    .setTheme(R.style.LoginScreenTheme).build(), RC_SIGN_IN);
        } else {
            Log.i(TAG, "User found, redirect to Home screen");
            redirectToHomeScreenActivity(currentUser);
        }
    }

    private void redirectToHomeScreenActivity(FirebaseUser currentUser) {
        String uid = currentUser.getUid();
        String displayName = currentUser.getDisplayName();
        String photoUrl = currentUser.getPhotoUrl().toString();
        Map<String, Object> data = new HashMap<>();
        data.put(User.FIELD_NAME, displayName);
        data.put(User.FIELD_PHOTO, photoUrl);
        DocumentReference userDoc = FirebaseFirestore.getInstance().collection(User.COLLECTION).document(uid);
        Log.d(TAG, "userDoc = " + userDoc);
        userDoc.set(data)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "User document successfully written!");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error writing User document", e);
                }
            });

//        userDoc.update(User.FIELD_NAME, displayName, User.FIELD_PHOTO, photoUrl);
        Log.d(TAG, "Updating User document");

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                Log.i(TAG, "User signed in");
                redirectToHomeScreenActivity(firebaseUser);
            } else if (response == null) {
                Log.w(TAG, "User cancelled signing in");
            } else {
                Log.e(TAG, "Error logging in", response.getError());
            }
        }
    }

}
