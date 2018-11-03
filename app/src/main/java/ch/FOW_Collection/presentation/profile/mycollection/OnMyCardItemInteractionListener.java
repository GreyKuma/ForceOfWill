package ch.FOW_Collection.presentation.profile.mycollection;

import android.widget.ImageView;
import ch.FOW_Collection.domain.models.Card;
import ch.FOW_Collection.domain.models.MyCard;

public interface OnMyCardItemInteractionListener {

    void onMoreClickedListener(ImageView photo, Card item);

    void onRemoveClickedListener(Card item);

    void onNormalUpClickedListener(MyCard myCard);
    void onNormalDownClickedListener(MyCard myCard);
    void onFoilUpClickedListener(MyCard myCard);
    void onFoilDownClickedListener(MyCard myCard);
}
