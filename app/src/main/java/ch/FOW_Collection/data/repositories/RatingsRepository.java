package ch.FOW_Collection.data.repositories;

import android.util.Log;
import android.util.Pair;

import androidx.lifecycle.MutableLiveData;
import ch.FOW_Collection.data.parser.RatingClassSnapshotParser;
import ch.FOW_Collection.domain.liveData.FirestoreQueryLiveData;
import ch.FOW_Collection.domain.models.Card;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import androidx.lifecycle.LiveData;
import ch.FOW_Collection.domain.liveData.FirestoreQueryLiveDataArray;
import ch.FOW_Collection.domain.models.Rating;
import ch.FOW_Collection.domain.models.Wish;

import javax.annotation.Nullable;

import static androidx.lifecycle.Transformations.map;
import static androidx.lifecycle.Transformations.switchMap;
import static ch.FOW_Collection.domain.liveData.LiveDataExtensions.combineLatest;
import static ch.FOW_Collection.domain.liveData.LiveDataExtensions.zip;

public class RatingsRepository {
    //region private static
    // For the pattern, we don't want to use static methods,
    // instead we define public / nonStatic after this region.
    private static final RatingClassSnapshotParser parser = new RatingClassSnapshotParser();

    /**
     * Get Query for all ratings.
     * @return Query for all ratings
     */
    private static Query allRatingsQuery() {
        return FirebaseFirestore.getInstance()
                .collection(Rating.COLLECTION)
                .orderBy(Rating.FIELD_CREATION_DATE, Query.Direction.DESCENDING);
    }

    /**
     * Get LiveData of all ratings.
     * @return LiveDataArray of all ratings.
     */
    private final FirestoreQueryLiveDataArray<Rating> allRatings = new FirestoreQueryLiveDataArray<>(
            allRatingsQuery(), parser);

    public static LiveData<List<Rating>> ratingsByUserId(String userId) {
        return new FirestoreQueryLiveDataArray<>(
                allRatingsQuery().whereEqualTo(Rating.FIELD_USER_ID, userId), parser);
    }
/*
    public static LiveData<List<Rating>> ratingsByCard(Card card) {
        return ratingsByCardId(card.getId());
    }*/

    public static LiveData<List<Rating>> ratingsByCardId(String cardId) {
        return new FirestoreQueryLiveDataArray<>(
                allRatingsQuery().whereEqualTo(Rating.FIELD_CARD_ID, cardId), parser);
    }

    private static LiveData<Rating> ratingsByUserIdAndCardId(Pair<String, String> input) {
        return ratingsByUserIdAndCardId(input.first, input.second);
    }
/*

    private static LiveData<Rating> ratingsByCardAndUserId(Pair<String, Card> input) {
        return ratingsByCardIdAndUserId(input.first, input.second.getId());
    }*/

    public static LiveData<Rating> ratingsByUserIdAndCardId(String userId, String cardId) {
        return new FirestoreQueryLiveData<Rating>(
                FirebaseFirestore.getInstance()
                        .collection(Rating.COLLECTION)
                        .document(Rating.generateId(userId, cardId)), parser);
    }

    private static Task<Void> setRating(Rating rating) {
        return FirebaseFirestore
                .getInstance()
                .collection(Rating.COLLECTION)
                .document(rating.getId())
                .set(parser.parseMap(rating), SetOptions.merge());//, SetOptions.merge());
    }

    //endregion

    //region public / nonStatic accessor

    /*
    public LiveData<List<Pair<Rating, Wish>>> getAllRatingsWithWishes(LiveData<List<Wish>> myWishlist) {
        return map(combineLatest(getAllRatings(), map(myWishlist, entries -> {
            HashMap<String, Wish> byId = new HashMap<>();
            for (Wish entry : entries) {
                byId.put(entry.getCardId(), entry);
            }
            return byId;
        })), input -> {
            List<Rating> ratings = input.first;
            HashMap<String, Wish> wishesByItem = input.second;

            ArrayList<Pair<Rating, Wish>> result = new ArrayList<>();
            for (Rating rating : ratings) {
                Wish wish = wishesByItem.get(rating.getCardId());
                result.add(Pair.create(rating, wish));
            }
            return result;
        });
    }*/

    public LiveData<List<Rating>> getAllRatings() {
        return allRatings;
    }

    public LiveData<List<Rating>> getRatingsForCardId(String cardId) {
        return ratingsByCardId(cardId);
    }

    public LiveData<List<Rating>> getRatingsByUserId(String userId) {
        return ratingsByUserId(userId);
    }

    public LiveData<List<Rating>> getRatingsByUserId(MutableLiveData<String> userId) {
        return switchMap(userId, RatingsRepository::ratingsByUserId);
    }

    public LiveData<List<Rating>> getRatingsByCardId(MutableLiveData<String> cardId) {
        return switchMap(cardId, RatingsRepository::ratingsByCardId);
    }

    public LiveData<List<Rating>> getRatingsByCardId(String cardId) {
        return ratingsByCardId(cardId);
    }
/*
    public LiveData<List<Rating>> getRatingsByCard(MutableLiveData<Card> card) {
        return switchMap(card, RatingsRepository::ratingsByCard);
    }

    public LiveData<List<Rating>> getRatingsByCard(Card card) {
        return ratingsByCard(card);
    }
*/
    public LiveData<Rating> getRatingsByUserIdAndCardId(String userId, String cardId) {
        return ratingsByUserIdAndCardId(userId, cardId);
    }

    public LiveData<Rating> getRatingsByUserIdAndCardId(MutableLiveData<String> userId, MutableLiveData<String> cardId) {
        return switchMap(zip(userId, cardId), RatingsRepository::ratingsByUserIdAndCardId); //ratingsByCardIdAndUserId(cardId, userId);
    }
/*
    public LiveData<Rating> getRatingsByCardAndUserId(LiveData<Card> card, MutableLiveData<String> userId) {
        return switchMap(zip(userId, card), RatingsRepository::ratingsByCardAndUserId); //ratingsByCardIdAndUserId(cardId, userId);
    }
*/
    public Task<Void> putRating(Rating rating) {
        if (rating.getId() == null || rating.getId().equals("")) {
            rating.setId(Rating.generateId(rating.getUserId(), rating.getCardId()));
        }
        return setRating(rating);
    }

    @Deprecated
    public LiveData<List<Rating>> getMyRatings(LiveData<String> currentUserId) {
        return switchMap(currentUserId, RatingsRepository::ratingsByUserId);
    }



    //endregion
}
