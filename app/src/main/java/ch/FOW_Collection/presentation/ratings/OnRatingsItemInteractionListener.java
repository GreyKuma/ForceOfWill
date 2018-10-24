package ch.FOW_Collection.presentation.ratings;

import ch.FOW_Collection.domain.models.Rating;

public interface OnRatingsItemInteractionListener {
    void onRatingLikedListener(Rating rating);

    void onMoreClickedListener(Rating rating);

    void onWishClickedListener(Rating item);
}
