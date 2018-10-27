package ch.FOW_Collection.presentation.profile.mywishlist;

import android.widget.ImageView;

import ch.FOW_Collection.domain.models.Beer;

public interface OnWishlistItemInteractionListener {

    void onMoreClickedListener(ImageView photo, Beer beer);

    void onWishClickedListener(Beer beer);
}
