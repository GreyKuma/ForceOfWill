package ch.FOW_Collection.presentation.profile.myratings;

import ch.FOW_Collection.domain.models.Rating;

public interface OnMyRatingItemInteractionListener {

    void onMoreClickedListener(Rating item);

    void onWishClickedListener(Rating item);
}
