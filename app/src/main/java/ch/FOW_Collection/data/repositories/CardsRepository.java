package ch.FOW_Collection.data.repositories;

import com.firebase.ui.firestore.FirestoreArray;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;

import java.util.List;

import androidx.lifecycle.LiveData;
import ch.FOW_Collection.data.parser.CardClassSnapshotParser;
import ch.FOW_Collection.domain.models.Card;
import ch.FOW_Collection.domain.liveData.FirestoreQueryLiveData;
import ch.FOW_Collection.domain.liveData.FirestoreQueryLiveDataArray;
import ch.FOW_Collection.data.parser.EntityClassSnapshotParser;

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
                allCardsQuery(), Card.class);
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
                cardByIdDocRef(cardId), Card.class);
    }

    /**
     * Get query for top rated cards.
     * @return query for top rated cards.
     */
    private static Query cardsTopRatedQuery() {
        return FirebaseFirestore
                        .getInstance()
                        .collection(Card.COLLECTION)
                        .orderBy(Card.FIELD_RATING, Query.Direction.DESCENDING);
    }

    //endregion

    //region public / nonStatic accessor

    /**
     * Get LiveData of all cards.
     * @return LiveDataArray of all cards.
     */
    public LiveData<List<Card>> getAllCards() {
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

    /**
     * Get Query for top rated cards.
     * Should only be used by Firestore-ui-Components which has setLifecycleOwner!
     * @return FirestoreArray for top rated cards.
     */
    public Query getCardsTopRatedQuery(int limit) {
        return cardsTopRatedQuery().limit(limit);
    }

    //endregion
}
