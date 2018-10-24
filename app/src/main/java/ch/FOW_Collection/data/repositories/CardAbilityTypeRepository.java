package ch.FOW_Collection.data.repositories;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

import androidx.lifecycle.LiveData;
import ch.FOW_Collection.domain.liveData.FirestoreQueryLiveData;
import ch.FOW_Collection.domain.liveData.FirestoreQueryLiveDataArray;
import ch.FOW_Collection.domain.models.CardAbilityType;

public class CardAbilityTypeRepository {
    //region private static
    // For the pattern, we don't want to use static methods,
    // instead we define public / nonStatic after this region.

    /**
     * Get Query for all cardAbilityTypes.
     * @return Query for all cardAbilityTypes
     */
    private static Query allCardAbilityTypesQuery() {
        return FirebaseFirestore
                .getInstance()
                .collection(CardAbilityType.COLLECTION);
    }

    /**
     * Get LiveData of all cardAbilityTypes.
     * @return LiveDataArray of all cardAbilityTypes.
     */
    private FirestoreQueryLiveDataArray<CardAbilityType> allCardAbilityTypes() {
        return new FirestoreQueryLiveDataArray<>(
                allCardAbilityTypesQuery(), CardAbilityType.class);
    }

    /**
     * Get DocumentReference of a single cardAbilityType.
     * @param cardAbilityTypeId Id of the cardAbilityType.
     * @return DocumentReference of a single cardAbilityType.
     */
    private static DocumentReference cardAbilityTypeByIdQuery(String cardAbilityTypeId) {
        return FirebaseFirestore
                .getInstance()
                .collection(CardAbilityType.COLLECTION)
                .document(cardAbilityTypeId);
    }

    /**
     * Get LiveData of a single cardAbilityType.
     * @param cardAbilityTypeId Id of the cardAbilityType.
     * @return LiveData of a single cardAbilityType.
     */
    private static LiveData<CardAbilityType> cardAbilityTypeById(String cardAbilityTypeId) {
        return new FirestoreQueryLiveData<>(
                cardAbilityTypeByIdQuery(cardAbilityTypeId), CardAbilityType.class);
    }

    //endregion

    //region public / nonStatic accessor

    public LiveData<List<CardAbilityType>> getAllAbilityTypes() {
        return allCardAbilityTypes();
    }

    public LiveData<CardAbilityType> getAbilityTypeById(int cardAbilityTypeId) {
        return cardAbilityTypeById(Integer.toString(cardAbilityTypeId));
    }

    //endregion
}
