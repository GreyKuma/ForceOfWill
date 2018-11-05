package ch.FOW_Collection.domain.liveData;

import android.os.Handler;
import androidx.lifecycle.LiveData;
import ch.FOW_Collection.data.parser.EntityClassSnapshotParser;
import ch.FOW_Collection.domain.models.Entity;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.firestore.*;

import javax.annotation.Nullable;

public class FirestoreQueryLiveData<T extends Entity> extends LiveData<T> implements EventListener<DocumentSnapshot> {

    private static final String TAG = "FQueryLiveData";

    private final Handler handler = new Handler();
    private final DocumentReference documentReference;
    private boolean listenerRemovePending = false;
    private ListenerRegistration listenerRegistration;
    private final Runnable removeListener = new Runnable() {
        @Override
        public void run() {
            listenerRegistration.remove();
            listenerRemovePending = false;
        }
    };

    private SnapshotParser<T> parser;

    public FirestoreQueryLiveData(DocumentReference documentReference, Class<T> modelClass) {
        this.documentReference = documentReference;
        parser = new EntityClassSnapshotParser<T>(modelClass);
    }

    public FirestoreQueryLiveData(DocumentReference documentReference, SnapshotParser<T> parser) {
        this.documentReference = documentReference;
        this.parser = parser;
    }

    @Override
    protected void onActive() {
        if (listenerRemovePending) {
            handler.removeCallbacks(removeListener);
        } else if (listenerRegistration == null) {
            listenerRegistration = documentReference.addSnapshotListener(this);
        }
        listenerRemovePending = false;
    }

    @Override
    protected void onInactive() {
        handler.postDelayed(removeListener, 2000);
        listenerRemovePending = true;
    }

    @Override
    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
        if (documentSnapshot != null && documentSnapshot.exists()) {
            setValue(parser.parseSnapshot(documentSnapshot));
        }
    }
}