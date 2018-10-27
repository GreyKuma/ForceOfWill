package ch.FOW_Collection.presentation.shared.cardList;

import ch.FOW_Collection.domain.liveData.FirestoreQueryLiveDataArray;
import ch.FOW_Collection.domain.models.Card;
import ch.FOW_Collection.presentation.shared.ICardSelectedListener;

/**
 * The owner of {@link CardListFragment} needs to implement
 * this interface so we can notify it when the user has clicked on one of the entries in the grid.
 */
public interface ICardListFragmentListener extends ICardSelectedListener {
    FirestoreQueryLiveDataArray<Card> getData(String cardListId);
}
