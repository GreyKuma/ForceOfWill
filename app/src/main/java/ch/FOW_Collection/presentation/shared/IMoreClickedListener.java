package ch.FOW_Collection.presentation.shared;

import android.widget.ImageView;

import ch.FOW_Collection.domain.models.Card;

public interface IMoreClickedListener {
    void onMoreClickedListener(ImageView imageView, Card card);
}
