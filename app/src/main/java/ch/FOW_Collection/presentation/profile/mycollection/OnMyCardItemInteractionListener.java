package ch.FOW_Collection.presentation.profile.mycollection;

import android.widget.ImageView;
import ch.FOW_Collection.domain.models.Beer;
import ch.FOW_Collection.domain.models.Card;

public interface OnMyCardItemInteractionListener {

    void onMoreClickedListener(ImageView photo, Card item);

    void onWishClickedListener(Card item);
}
