package ch.FOW_Collection.presentation.profile.mywishlist;

import android.widget.ImageView;
import ch.FOW_Collection.domain.models.Beer;
import ch.FOW_Collection.domain.models.Card;

public interface OnWishlistItemInteractionListener {

    void onMoreClickedListener(ImageView photo, Card card);

    void onWishClickedListener(Card card);
}
