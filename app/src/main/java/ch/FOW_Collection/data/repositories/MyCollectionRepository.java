package ch.FOW_Collection.data.repositories;

import android.util.Log;
import androidx.lifecycle.LiveData;
import ch.FOW_Collection.data.parser.MyCardClassSnapshotParser;
import ch.FOW_Collection.domain.liveData.FirestoreQueryLiveData;
import ch.FOW_Collection.domain.liveData.FirestoreQueryLiveDataArray;
import ch.FOW_Collection.domain.models.Card;
import ch.FOW_Collection.domain.models.Collection;
import ch.FOW_Collection.domain.models.MyCard;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

import static androidx.lifecycle.Transformations.switchMap;

public class MyCollectionRepository {
    public static final String TAG = "MyCollectionRepository";

    public static LiveData<List<MyCard>> getCollectionByUser(String userId) {
        if (userId == null) {
            return null;
        }
        return new FirestoreQueryLiveDataArray<>(FirebaseFirestore.getInstance()
                .collection(Collection.FIRST_COLLECTION + "/" + userId + "/" + Collection.SECOND_COLLECTION)
                .orderBy(Card.CARD_ID, Query.Direction.DESCENDING),
                new MyCardClassSnapshotParser());
    }

    public Task<Void> toggleCardInCollection(String userId, String cardId) {
        Log.d(TAG, "UserId: " + userId + ", cardId: " + cardId);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference cardRef = db
                .collection(Collection.FIRST_COLLECTION + "/" + userId + "/" + Collection.SECOND_COLLECTION)
                .document(cardId);

        return cardRef.get().continueWithTask(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                return cardRef.delete();
            } else if (task.isSuccessful()) {
                return cardRef.set(new MyCard(cardId));
            } else {
                throw task.getException();
            }
        });
    }

    public LiveData<List<MyCard>> getMyCollection(LiveData<String> currentUserId) {
        return switchMap(currentUserId, MyCollectionRepository::getCollectionByUser);
    }

    public Task<Void> addOneToCardAmount(String userId, MyCard myCard, final String type) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference cardRef = db
                .collection(Collection.FIRST_COLLECTION + "/" + userId + "/" + Collection.SECOND_COLLECTION)
                .document(myCard.getCardId());

        return cardRef.get().continueWithTask(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                if (type == MyCard.FIELD_AMOUNT_NORMAL) {
                    return cardRef.update(MyCard.FIELD_AMOUNT_NORMAL, myCard.getAmountNormal() + 1);
                } else if (type == MyCard.FIELD_AMOUNT_FOIL) {
                    return cardRef.update(MyCard.FIELD_AMOUNT_FOIL, myCard.getAmountFoil() + 1);
                } else {
                    return null;
                }
            } else {
                throw task.getException();
            }
        });
    }

    public Task<Void> subOneFromCardAmount(String userId, MyCard myCard, final String type) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference cardRef = db
                .collection(Collection.FIRST_COLLECTION + "/" + userId + "/" + Collection.SECOND_COLLECTION)
                .document(myCard.getCardId());

        return cardRef.get().continueWithTask(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                if (type == MyCard.FIELD_AMOUNT_NORMAL && myCard.getAmountNormal() != 0) {
                    return cardRef.update(MyCard.FIELD_AMOUNT_NORMAL, myCard.getAmountNormal() - 1);
                } else if (type == MyCard.FIELD_AMOUNT_FOIL && myCard.getAmountFoil() != 0) {
                    return cardRef.update(MyCard.FIELD_AMOUNT_FOIL, myCard.getAmountFoil() - 1);
                } else {
                    return null;
                }
            } else {
                throw task.getException();
            }
        });
    }

    public LiveData<MyCard> getCardById(String userId, String cardId) {
        return new FirestoreQueryLiveData<>(
                FirebaseFirestore
                        .getInstance()
                        .collection(Collection.FIRST_COLLECTION + "/" + userId + "/" + Collection.SECOND_COLLECTION)
                        .document(cardId), new MyCardClassSnapshotParser());
    }

}