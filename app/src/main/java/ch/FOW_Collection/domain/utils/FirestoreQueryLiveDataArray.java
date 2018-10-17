package ch.FOW_Collection.domain.utils;

import android.os.Handler;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import ch.FOW_Collection.domain.models.Entity;
import ch.FOW_Collection.presentation.utils.EntityClassSnapshotParser;
import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.firestore.ChangeEventListener;
import com.firebase.ui.firestore.FirestoreArray;
import com.firebase.ui.firestore.ObservableSnapshotArray;
import com.google.firebase.firestore.*;

import java.util.List;

public class FirestoreQueryLiveDataArray<T extends Entity> extends LiveData<List<T>> implements ChangeEventListener {

    private static final String TAG = "FQueryLiveDataArray";

    private final ObservableSnapshotArray<T> mSnapshots;
    private final Handler handler = new Handler();
    private boolean listenerRemovePending = false;
    private final Runnable removeListener = new Runnable() {
        @Override
        public void run() {
            mSnapshots.removeChangeEventListener(FirestoreQueryLiveDataArray.this);
            listenerRemovePending = false;
        }
    };

    public FirestoreQueryLiveDataArray(Query query, Class<T> modelClass) {
        this.mSnapshots =
                new FirestoreArray<>(query, MetadataChanges.EXCLUDE, new EntityClassSnapshotParser<>(modelClass));
    }

    @Override
    protected void onActive() {
        if (listenerRemovePending) {
            handler.removeCallbacks(removeListener);
        } else if (!mSnapshots.isListening(this)) {
            mSnapshots.addChangeEventListener(this);
        }
        listenerRemovePending = false;
    }

    @Override
    protected void onInactive() {
        handler.postDelayed(removeListener, 2000);
        listenerRemovePending = true;
    }

    @Override
    public void onChildChanged(@NonNull ChangeEventType type, @NonNull DocumentSnapshot snapshot, int newIndex,
                               int oldIndex) {

    }

    @Override
    public void onDataChanged() {
        setValue(mSnapshots);
    }

    @Override
    public void onError(@NonNull FirebaseFirestoreException e) {
        Log.e(TAG, "Error:", e);
    }


}