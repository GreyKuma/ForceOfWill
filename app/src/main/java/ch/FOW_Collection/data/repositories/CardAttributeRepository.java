package ch.FOW_Collection.data.repositories;

import androidx.lifecycle.LiveData;
import ch.FOW_Collection.domain.liveData.FirestoreQueryLiveData;
import ch.FOW_Collection.domain.liveData.FirestoreQueryLiveDataArray;
import ch.FOW_Collection.domain.models.CardAttribute;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

import static androidx.lifecycle.Transformations.map;

public class CardAttributeRepository {
    //region private static
    // For the pattern, we don't want to use static methods,
    // instead we define public / nonStatic after this region.

    /**
     * Get Query for all cardAttributes.
     *
     * @return Query for all cardAttributes
     */
    private static Query allCardAttributesQuery() {
        return FirebaseFirestore
                .getInstance()
                .collection(CardAttribute.COLLECTION);
    }

    /**
     * Get LiveData of all cardAttributes.
     *
     * @return LiveDataArray of all cardAttributes.
     */
    private FirestoreQueryLiveDataArray<CardAttribute> allCardAttributes() {
        return new FirestoreQueryLiveDataArray<>(
                allCardAttributesQuery(), CardAttribute.class);
    }

    /**
     * Get DocumentReference of a single cardAttribute.
     *
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
     *
     * @param cardAttributeId Id of the cardAttribute.
     * @return LiveData of a single cardAttribute.
     */
    private static FirestoreQueryLiveData<CardAttribute> cardAttributeById(String cardAttributeId) {
        return new FirestoreQueryLiveData<>(
                cardAttributeByIdQuery(cardAttributeId), CardAttribute.class);
    }

    //endregion

    //region public / nonStatic accessor

    public FirestoreQueryLiveDataArray<CardAttribute> getAllAttributes() {
        return allCardAttributes();
    }

    public FirestoreQueryLiveData<CardAttribute> getCardAttributeById(int cardAttributeId) {
        return cardAttributeById(Integer.toString(cardAttributeId));
    }

    public LiveData<List<CardAttribute>> getAttributesByIds(List<Integer> cardAttributeIds) {
        List<String> cardAttributeIdsSet = new ArrayList<>();
        for (Integer i : cardAttributeIds) {
            cardAttributeIdsSet.add(Integer.toString(i));
        }

        return map(allCardAttributes(), input -> {
            List<CardAttribute> filtered = new ArrayList<>();
            for (CardAttribute cardAttribute : input) {
                if (cardAttributeIdsSet.contains(cardAttribute.getId())) {
                    filtered.add(cardAttribute);
                }
            }
            return filtered;
        });
    }

    //endregion
}
