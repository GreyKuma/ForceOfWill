package ch.FOW_Collection.data.repositories;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

import androidx.lifecycle.LiveData;
import ch.FOW_Collection.domain.liveData.FirestoreQueryLiveData;
import ch.FOW_Collection.domain.liveData.FirestoreQueryLiveDataArray;
import ch.FOW_Collection.domain.models.CardRace;

public class CardRaceRepository {
    //region private static
    // For the pattern, we don't want to use static methods,
    // instead we define public / nonStatic after this region.

    /**
     * Get Query for all cardRaces.
     * @return Query for all cardRaces
     */
    private static Query allCardRacesQuery() {
        return FirebaseFirestore
                .getInstance()
                .collection(CardRace.COLLECTION);
    }

    /**
     * Get LiveData of all cardRaces.
     * @return LiveDataArray of all cardRaces.
     */
    private FirestoreQueryLiveDataArray<CardRace> allCardRaces() {
        return new FirestoreQueryLiveDataArray<>(
                allCardRacesQuery(), CardRace.class);
    }

    /**
     * Get DocumentReference of a single cardRace.
     * @param cardRaceId Id of the cardRace.
     * @return DocumentReference of a single cardRace.
     */
    private static DocumentReference cardRaceByIdQuery(String cardRaceId) {
        return FirebaseFirestore
                .getInstance()
                .collection(CardRace.COLLECTION)
                .document(cardRaceId);
    }

    /**
     * Get LiveData of a single cardRace.
     * @param cardRaceId Id of the cardRace.
     * @return LiveData of a single cardRace.
     */
    private static LiveData<CardRace> cardRaceById(String cardRaceId) {
        return new FirestoreQueryLiveData<>(
                cardRaceByIdQuery(cardRaceId), CardRace.class);
    }

    //endregion

    //region public / nonStatic accessor

    public LiveData<List<CardRace>> getAllRaces() {
        return allCardRaces();
    }

    public LiveData<CardRace> getRaceById(int cardRaceId) {
        return cardRaceById(Integer.toString(cardRaceId));
    }

    public DocumentReference getCardRaceByIdQuery(int cardRaceId) {
        return cardRaceByIdQuery(Integer.toString(cardRaceId));
    }

    //endregion
}
