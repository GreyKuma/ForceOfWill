package ch.FOW_Collection.presentation.shared;

import ch.FOW_Collection.domain.models.Card;

public interface IWishClickedListener extends ICardSelectedListener {
    void onWishClickedListener(Card card);
}
