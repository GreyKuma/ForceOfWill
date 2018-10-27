package ch.FOW_Collection.data.repositories;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

import androidx.lifecycle.LiveData;
import ch.FOW_Collection.data.parser.EntityClassSnapshotParser;
import ch.FOW_Collection.domain.liveData.FirestoreQueryLiveDataArray;
import ch.FOW_Collection.domain.models.Search;

public class SearchesRepository {

    private final static EntityClassSnapshotParser<Search> parser = new EntityClassSnapshotParser<>(Search.class);

    public static LiveData<List<Search>> getLatestSearchesByUser(String userId) {
        return new FirestoreQueryLiveDataArray<>(FirebaseFirestore.getInstance().collection(Search.COLLECTION)
                .orderBy(Search.FIELD_CREATION_DATE, Query.Direction.DESCENDING)
                .whereEqualTo(Search.FIELD_USER_ID, userId).limit(3), Search.class);
    }

    public void addSearchTerm(String term) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Search search = new Search(currentUser.getUid(), term);
        db.collection(Search.COLLECTION).add(search);
    }
}
