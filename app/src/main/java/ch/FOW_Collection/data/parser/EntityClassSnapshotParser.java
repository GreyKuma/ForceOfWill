package ch.FOW_Collection.data.parser;

import androidx.annotation.NonNull;
import ch.FOW_Collection.domain.models.Entity;
import com.firebase.ui.firestore.ClassSnapshotParser;
import com.google.firebase.firestore.DocumentSnapshot;

public class EntityClassSnapshotParser<T extends Entity> extends ClassSnapshotParser<T> {
    public EntityClassSnapshotParser(Class<T> modelClass) {
        super(modelClass);
    }

    @NonNull
    @Override
    public T parseSnapshot(@NonNull DocumentSnapshot snapshot) {
        T t = super.parseSnapshot(snapshot);
        t.setId(snapshot.getId());
        return t;
    }
}
