package ch.FOW_Collection.data.repositories;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import androidx.lifecycle.LiveData;
import ch.FOW_Collection.data.parser.CardClassSnapshotParser;
import ch.FOW_Collection.domain.liveData.FirestoreQueryLiveData;
import ch.FOW_Collection.domain.liveData.FirestoreQueryLiveDataArray;
import ch.FOW_Collection.domain.models.Card;

import static androidx.lifecycle.Transformations.switchMap;

public class CardsRepository {

    //region private static
    // For the pattern, we don't want to use static methods,
    // instead we define public / nonStatic after this region.

    /**
     * Get Query for all cards.
     * @return Query for all cards
     */
    private static Query allCardsQuery() {
        return FirebaseFirestore
                .getInstance()
                .collection(Card.COLLECTION);
    }

    /**
     * Get LiveData of all cards.
     * @return LiveDataArray of all cards.
     */
    private static FirestoreQueryLiveDataArray<Card> allCards() {
        return new FirestoreQueryLiveDataArray<>(
                allCardsQuery(), new CardClassSnapshotParser());
    }

    /**
     * Get DocumentReference of a single card.
     * @param cardId Id of the card.
     * @return DocumentReference of a single card.
     */
    private static DocumentReference cardByIdDocRef(String cardId) {
        return FirebaseFirestore
                .getInstance()
                .collection(Card.COLLECTION)
                .document(cardId);
    }

    /**
     * Get LiveData of all cards.
     * @param cardId Id of the card.
     * @return LiveData of all cards.
     */
    private static FirestoreQueryLiveData<Card> cardById(String cardId) {
        return new FirestoreQueryLiveData<>(
                cardByIdDocRef(cardId), new CardClassSnapshotParser());
    }

    /**
     * Get query for top rated cards.
     * @return query for top rated cards.
     */
    private static Query cardsTopRatedQuery(int limit) {
        return FirebaseFirestore
                        .getInstance()
                        .collection(Card.COLLECTION)
                        .orderBy(Card.FIELD_RATING, Query.Direction.DESCENDING)
                        .limit(limit);
    }

    /**
     * Get LiveData of all cards.
     * @return LiveDataArray of all cards.
     */
    private static FirestoreQueryLiveDataArray<Card> cardsTopRated(int limit) {
        return new FirestoreQueryLiveDataArray<>(
                cardsTopRatedQuery(limit), new CardClassSnapshotParser());
    }

    //endregion

    //region public / nonStatic accessor

    /**
     * Get LiveData of all cards.
     * @return LiveDataArray of all cards.
     */
    public FirestoreQueryLiveDataArray<Card> getAllCards() {
        return new FirestoreQueryLiveDataArray<>(
                allCardsQuery(), new CardClassSnapshotParser());
    }

    /**
     * Get LiveData of all cards.
     * @param cardId Id of the card.
     * @return LiveData of all cards.
     */
    public LiveData<Card> getCardById(String cardId) {
        return new FirestoreQueryLiveData<>(
                cardByIdDocRef(cardId), new CardClassSnapshotParser());
    }

    public LiveData<Card> getCardById(LiveData<String> cardId) {
        return switchMap(cardId, CardsRepository::cardById);
    }

    /**
     * Get Query for top rated cards.
     * Should only be used by Firestore-ui-Components which has setLifecycleOwner!
     * @return FirestoreArray for top rated cards.
     */
    //public Query getCardsTopRatedQuery(int limit) {
    //    return cardsTopRatedQuery().limit(limit);
    //}

    public FirestoreQueryLiveDataArray<Card> getCardsTopRated(int limit) {
        return cardsTopRated(limit);
    }

    //endregion
}
