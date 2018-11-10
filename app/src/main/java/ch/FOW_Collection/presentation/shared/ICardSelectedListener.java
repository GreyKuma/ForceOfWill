package ch.FOW_Collection.presentation.shared;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.util.Pair;
import android.widget.ImageView;
import ch.FOW_Collection.domain.models.Card;
import ch.FOW_Collection.presentation.cardDetails.CardDetailsActivity;

public interface ICardSelectedListener {
    public static void DefaultBehavior(Activity activity, ImageView imageView, Card card) {
        Intent intent = new Intent(activity, CardDetailsActivity.class);
        intent.putExtra(CardDetailsActivity.ITEM_ID, card.getId());
        ActivityOptions options = ActivityOptions
                .makeSceneTransitionAnimation(
                        activity,
                        Pair.create(imageView, "image")
                );
        activity.startActivity(intent, options.toBundle());
    }

    void onCardSelectedListener(ImageView imageView, Card card);
}
