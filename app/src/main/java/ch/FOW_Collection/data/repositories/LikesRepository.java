package ch.FOW_Collection.data.repositories;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Transaction;

import java.util.List;
import java.util.Map;

import ch.FOW_Collection.data.parser.EntityClassSnapshotParser;
import ch.FOW_Collection.domain.models.Rating;

public class LikesRepository {

    private final static EntityClassSnapshotParser<Rating> parser = new EntityClassSnapshotParser<>(Rating.class);


    public void toggleLike(Rating rating) {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference ratingRef = db.collection(Rating.COLLECTION).document(rating.getId());

        db.runTransaction((Transaction.Function<Void>) transaction -> {
            Rating currentRating = parser.parseSnapshot(transaction.get(ratingRef));
            Map<String, Boolean> likes = currentRating.getLikes();
            String currentUserUid = currentUser.getUid();
            if (likes.containsKey(currentUserUid)) {
                likes.remove(currentUserUid);
            } else {
                likes.put(currentUserUid, true);
            }
            transaction.update(ratingRef, Rating.FIELD_LIKES, likes);
            return null;
        });
    }
}
