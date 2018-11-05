package ch.FOW_Collection.presentation.shared;

import ch.FOW_Collection.domain.models.MyCard;

public interface ICollectionInteractionListener extends ICardSelectedListener {
    void onCollectionRemoveClickedListener(MyCard myCard);

    void onCollectionNormalUpClickedListener(MyCard myCard);

    void onCollectionNormalDownClickedListener(MyCard myCard);

    void onCollectionFoilUpClickedListener(MyCard myCard);

    void onCollectionFoilDownClickedListener(MyCard myCard);
}
