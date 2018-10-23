package ch.FOW_Collection.presentation.shared.cardList;

import com.google.firebase.firestore.Query;

import ch.FOW_Collection.domain.models.Card;
/**
 * The owner of {@link CardListFragment} needs to implement
 * this interface so we can notify it when the user has clicked on one of the entries in the grid.
 */
public interface ICardListFragmentListener {
    Query getQuery(String cardListId);
    void onCardSelected(String cardListId, Card card);
}
