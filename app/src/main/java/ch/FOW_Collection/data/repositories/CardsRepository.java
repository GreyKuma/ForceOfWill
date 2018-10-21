package ch.FOW_Collection.data.repositories;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import ch.FOW_Collection.domain.models.Card;
import ch.FOW_Collection.domain.utils.FirestoreQueryLiveData;
import ch.FOW_Collection.domain.utils.FirestoreQueryLiveDataArray;

import static androidx.lifecycle.Transformations.map;

public class CardsRepository {
    private final static Function<List<Card>, List<Card>> resolveForeignKeys = (List<Card> cards) -> {

        return cards;
    };

    private final FirestoreQueryLiveDataArray<Card> allCards =
            new FirestoreQueryLiveDataArray<>(
                    FirebaseFirestore
                        .getInstance()
                        .collection(Card.COLLECTION), Card.class);

    private static LiveData<Card> cardById(String cardId) {
        return new FirestoreQueryLiveData<>(
                FirebaseFirestore
                        .getInstance()
                        .collection(Card.COLLECTION)
                        .document(cardId), Card.class);
    }

    private static FirestoreQueryLiveDataArray<Card> cardsTopRated(int count) {
        return new FirestoreQueryLiveDataArray<>(
                FirebaseFirestore
                        .getInstance()
                        .collection(Card.COLLECTION)
                        .orderBy(Card.FIELD_RATING)
                        .limit(count), Card.class);
    }

    public LiveData<List<Card>> getAllCards() {
        return allCards;
    }

    public FirestoreQueryLiveDataArray<Card> getCardsTopRated(int count) {
        return cardsTopRated(count);
    }
}
