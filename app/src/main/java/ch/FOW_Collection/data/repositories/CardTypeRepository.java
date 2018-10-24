package ch.FOW_Collection.data.repositories;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

import androidx.lifecycle.LiveData;
import ch.FOW_Collection.domain.liveData.FirestoreQueryLiveData;
import ch.FOW_Collection.domain.liveData.FirestoreQueryLiveDataArray;
import ch.FOW_Collection.domain.models.CardType;

public class CardTypeRepository {
    //region private static
    // For the pattern, we don't want to use static methods,
    // instead we define public / nonStatic after this region.

    /**
     * Get Query for all cardType.
     * @return Query for all cardType
     */
    private static Query allCardTypesQuery() {
        return FirebaseFirestore
                .getInstance()
                .collection(CardType.COLLECTION);
    }

    /**
     * Get LiveData of all cardType.
     * @return LiveDataArray of all cardType.
     */
    private FirestoreQueryLiveDataArray<CardType> allCardTypes() {
        return new FirestoreQueryLiveDataArray<>(
                allCardTypesQuery(), CardType.class);
    }

    /**
     * Get DocumentReference of a single cardType.
     * @param cardTypeId Id of the card.
     * @return DocumentReference of a single cardType.
     */
    private static DocumentReference cardTypeByIdQuery(String cardTypeId) {
        return FirebaseFirestore
                .getInstance()
                .collection(CardType.COLLECTION)
                .document(cardTypeId);
    }

    /**
     * Get LiveData of a single cardType.
     * @param cardTypeId Id of the cardType.
     * @return LiveData of a single cardType.
     */
    private static LiveData<CardType> cardTypeById(String cardTypeId) {
        return new FirestoreQueryLiveData<>(
                cardTypeByIdQuery(cardTypeId), CardType.class);
    }

    //endregion

    //region public / nonStatic accessor

    public LiveData<List<CardType>> getAllTypes() {
        return allCardTypes();
    }

    public LiveData<CardType> getTypeById(int cardTypeId) {
        return cardTypeById(Integer.toString(cardTypeId));
    }

    //endregion
}
