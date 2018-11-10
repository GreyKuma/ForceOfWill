package ch.FOW_Collection;

import android.app.Application;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;

/**
 * The MyApplication class is instantiated once for the whole application and can (but should not) be used as a
 * singleton to store application wide data. The manifest contains a reference to this class, apart from that it is
 * not referenced in this application.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        /*
         * This will log whenever we reveice data from firestore. This is useful for debugging and to get a feeling
         * of how much and when new data is received from the database.
         * */
        FirebaseFirestore.setLoggingEnabled(true);

        /*
         * Apply settings
         */
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        FirebaseFirestore.getInstance().setFirestoreSettings(settings);

        FirebaseStorage.getInstance().setMaxUploadRetryTimeMillis(12000);
        // DISABLE NETWORK-TRAFFIC
        //
        /*
        FirebaseFirestore.getInstance().disableNetwork().continueWith(t ->
                Log.d("APP", "FirebaseFirestore_disableNetwork: " + (
                        t.isSuccessful() ? "Diasbled" : "Enabled" )));*/
    }
}