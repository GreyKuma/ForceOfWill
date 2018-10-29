package ch.FOW_Collection.presentation.profile.mybeers;

import android.widget.ImageView;

import ch.FOW_Collection.domain.models.Beer;

public interface OnMyBeerItemInteractionListener {

    void onMoreClickedListener(ImageView photo, Beer item);

    void onWishClickedListener(Beer item);
}
