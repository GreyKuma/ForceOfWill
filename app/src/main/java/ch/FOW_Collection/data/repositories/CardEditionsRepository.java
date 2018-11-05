package ch.FOW_Collection.data.repositories;

import ch.FOW_Collection.domain.liveData.FirestoreQueryLiveData;
import ch.FOW_Collection.domain.liveData.FirestoreQueryLiveDataArray;
import ch.FOW_Collection.domain.models.CardEdition;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class CardEditionsRepository {
    //region private static
    // For the pattern, we don't want to use static methods,
    // instead we define public / nonStatic after this region.

    /**
     * Get Query for all cards.
     *
     * @return Query for all cards
     */
    private static Query allEditionsQuery() {
        return FirebaseFirestore
                .getInstance()
                .collection(CardEdition.COLLECTION);
    }

    /**
     * Get LiveData of all cardEditions.
     *
     * @return LiveDataArray of all cardEditions.
     */
    private final FirestoreQueryLiveDataArray<CardEdition> allCardEditions() {
        return new FirestoreQueryLiveDataArray<>(
                allEditionsQuery(), CardEdition.class);
    }

    /**
     * Get DocumentReference of a single cardEdition.
     *
     * @param cardEditionId Id of the card.
     * @return DocumentReference of a single cardEdition.
     */
    private static DocumentReference cardEditionByIdQuery(String cardEditionId) {
        return FirebaseFirestore
                .getInstance()
                .collection(CardEdition.COLLECTION)
                .document(cardEditionId);
    }

    /**
     * Get LiveData of a single cardEdition.
     *
     * @param cardEditionId Id of the cardEdition.
     * @return LiveData of a single cardEdition.
     */
    private static FirestoreQueryLiveData<CardEdition> cardEditionById(String cardEditionId) {
        return new FirestoreQueryLiveData<>(
                cardEditionByIdQuery(cardEditionId), CardEdition.class);
    }

    //endregion

    //region public / nonStatic accessor

    public FirestoreQueryLiveDataArray<CardEdition> getAllEditions() {
        return allCardEditions();
    }

    public FirestoreQueryLiveData<CardEdition> getEditionById(int cardEditionId) {
        return cardEditionById(Integer.toString(cardEditionId));
    }

    //endregion
}
