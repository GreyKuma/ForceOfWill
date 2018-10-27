package ch.FOW_Collection.data.repositories;

import android.util.Pair;

import ch.FOW_Collection.domain.models.Card;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import androidx.lifecycle.LiveData;
import ch.FOW_Collection.domain.liveData.FirestoreQueryLiveData;
import ch.FOW_Collection.domain.liveData.FirestoreQueryLiveDataArray;
import ch.FOW_Collection.domain.models.Beer;
import ch.FOW_Collection.domain.models.Entity;
import ch.FOW_Collection.domain.models.Wish;

import static androidx.lifecycle.Transformations.map;
import static androidx.lifecycle.Transformations.switchMap;
import static ch.FOW_Collection.domain.liveData.LiveDataExtensions.combineLatest;

public class WishlistRepository {


    private static LiveData<List<Wish>> getWishesByUser(String userId) {
        return new FirestoreQueryLiveDataArray<>(FirebaseFirestore.getInstance().collection(Wish.COLLECTION)
                .orderBy(Wish.FIELD_ADDED_AT, Query.Direction.DESCENDING).whereEqualTo(Wish.FIELD_USER_ID, userId),
                Wish.class);
    }

    private static LiveData<Wish> getUserWishListFor(Pair<String, Card> input) {
        String userId = input.first;
        Card card = input.second;
        DocumentReference document = FirebaseFirestore.getInstance().collection(Wish.COLLECTION)
                .document(Wish.generateId(userId, card.getId()));
        return new FirestoreQueryLiveData<>(document, Wish.class);
    }

    public Task<Void> toggleUserWishlistItem(String userId, String itemId) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String wishId = Wish.generateId(userId, itemId);

        DocumentReference wishEntryQuery = db.collection(Wish.COLLECTION).document(wishId);

        return wishEntryQuery.get().continueWithTask(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                return wishEntryQuery.delete();
            } else if (task.isSuccessful()) {
                return wishEntryQuery.set(new Wish(userId, itemId, new Date()));
            } else {
                throw task.getException();
            }
        });
    }

    public LiveData<List<Pair<Wish, Card>>> getMyWishlistWithCards(LiveData<String> currentUserId,
                                                                   LiveData<List<Card>> allCards) {
        return map(combineLatest(getMyWishlist(currentUserId), map(allCards, Entity::entitiesById)), input -> {
            List<Wish> wishes = input.first;
            HashMap<String, Card> cardsById = input.second;

            ArrayList<Pair<Wish, Card>> result = new ArrayList<>();
            for (Wish wish : wishes) {
                Card card = cardsById.get(wish.getCardId());
                result.add(Pair.create(wish, card));
            }
            return result;
        });
    }

    public LiveData<List<Wish>> getMyWishlist(LiveData<String> currentUserId) {
        return switchMap(currentUserId, WishlistRepository::getWishesByUser);
    }


    public LiveData<Wish> getMyWishForCard(LiveData<String> currentUserId, LiveData<Card> card) {
        return switchMap(combineLatest(currentUserId, card), WishlistRepository::getUserWishListFor);
    }
}
