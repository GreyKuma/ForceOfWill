package ch.FOW_Collection.presentation.shared;

import ch.FOW_Collection.domain.models.Card;
/**
 * The owner of {@link ch.FOW_Collection.presentation.shared.CardListFragment} or
 * {@link ch.FOW_Collection.presentation.shared.CardInfiniteListFragment} needs to implement
 * this interface so we can notify it when the user has clicked on one of the entries in the grid.
 */
public interface OnCardSelectedListener {
    void onCardSelected(Card card);
}
