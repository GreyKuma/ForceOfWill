package ch.FOW_Collection.data.repositories;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import ch.FOW_Collection.domain.models.Card;
import ch.FOW_Collection.domain.utils.FirestoreQueryLiveData;
import ch.FOW_Collection.domain.utils.FirestoreQueryLiveDataArray;

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

    private static Query cardsTopRated() {
        return FirebaseFirestore
                        .getInstance()
                        .collection(Card.COLLECTION)
                        .orderBy(Card.FIELD_RATING, Query.Direction.DESCENDING);
    }

    public LiveData<List<Card>> getAllCards() {
        return allCards;
    }

    public Query getCardsTopRated() {
        return cardsTopRated();
    }
}
