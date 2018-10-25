package ch.FOW_Collection.data.repositories;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

import androidx.lifecycle.LiveData;
import ch.FOW_Collection.domain.liveData.FirestoreQueryLiveData;
import ch.FOW_Collection.domain.liveData.FirestoreQueryLiveDataArray;
import ch.FOW_Collection.domain.models.CardAttribute;

public class CardAttributeRepository {
    //region private static
    // For the pattern, we don't want to use static methods,
    // instead we define public / nonStatic after this region.

    /**
     * Get Query for all cardAttributes.
     * @return Query for all cardAttributes
     */
    private static Query allCardAttributesQuery() {
        return FirebaseFirestore
                .getInstance()
                .collection(CardAttribute.COLLECTION);
    }

    /**
     * Get LiveData of all cardAttributes.
     * @return LiveDataArray of all cardAttributes.
     */
    private FirestoreQueryLiveDataArray<CardAttribute> allCardAttributes() {
        return new FirestoreQueryLiveDataArray<>(
                allCardAttributesQuery(), CardAttribute.class);
    }

    /**
     * Get DocumentReference of a single cardAttribute.
     * @param cardAttributeId Id of the card.
     * @return DocumentReference of a single cardAttribute.
     */
    private static DocumentReference cardAttributeByIdQuery(String cardAttributeId) {
        return FirebaseFirestore
                .getInstance()
                .collection(CardAttribute.COLLECTION)
                .document(cardAttributeId);
    }

    /**
     * Get LiveData of a single cardAttribute.
     * @param cardAttributeId Id of the cardAttribute.
     * @return LiveData of a single cardAttribute.
     */
    private static LiveData<CardAttribute> cardAttributeById(String cardAttributeId) {
        return new FirestoreQueryLiveData<>(
                cardAttributeByIdQuery(cardAttributeId), CardAttribute.class);
    }

    //endregion

    //region public / nonStatic accessor

    public LiveData<List<CardAttribute>> getAllAttributes() {
        return allCardAttributes();
    }

    public LiveData<CardAttribute> getAttributeById(int cardAttributeId) {
        return cardAttributeById(Integer.toString(cardAttributeId));
    }

    public DocumentReference getCardAttributeByIdQuery(int cardAttributeId) {
        return cardAttributeByIdQuery(Integer.toString(cardAttributeId));
    }

    //endregion
}
