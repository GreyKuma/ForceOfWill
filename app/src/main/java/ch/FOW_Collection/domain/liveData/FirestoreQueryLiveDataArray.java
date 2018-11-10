package ch.FOW_Collection.domain.liveData;

import android.os.Handler;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import ch.FOW_Collection.data.parser.EntityClassSnapshotParser;
import ch.FOW_Collection.domain.models.Entity;
import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.firestore.ChangeEventListener;
import com.firebase.ui.firestore.FirestoreArray;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;

import java.util.List;

public class FirestoreQueryLiveDataArray<T extends Entity> extends LiveData<List<T>> implements ChangeEventListener {

    private static final String TAG = "FQueryLiveDataArray";

    private final Query mQuery;
    private final SnapshotParser<T> mParser;
    private final FirestoreArray<T> mSnapshots;
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
        this(query, new EntityClassSnapshotParser<>(modelClass));
    }

    public FirestoreQueryLiveDataArray(Query query, SnapshotParser<T> parser) {
        this.mQuery = query;
        this.mParser = parser;
        this.mSnapshots = new FirestoreArray<>(mQuery, MetadataChanges.EXCLUDE, mParser);
        setValue(mSnapshots);
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