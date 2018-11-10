package ch.FOW_Collection.presentation.shared.viewHolder;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.Nullable;
import ch.FOW_Collection.GlideApp;
import ch.FOW_Collection.R;
import ch.FOW_Collection.domain.models.Card;
import ch.FOW_Collection.presentation.shared.CardImageLoader;
import ch.FOW_Collection.presentation.shared.ICardSelectedListener;
import com.google.common.base.Strings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface CardBaseListentry {
    default void bindCardBaseListentry(View view, Card card, ICardSelectedListener listener, @Nullable String searchText) {
        TextView cardName = view.findViewById(R.id.cardName);
        TextView cardRarity = view.findViewById(R.id.category);
        TextView cardId = view.findViewById(R.id.cardId);
        ImageView cardImage = view.findViewById(R.id.cardImage);
        RatingBar cardRatingBar = view.findViewById(R.id.cardRatingBar);
        TextView cardNumRatings = view.findViewById(R.id.cardNumRatings);

        Pattern pattern = null;
        if (!Strings.isNullOrEmpty(searchText)) {
            pattern = Pattern.compile(searchText);
        }
        // cardId: highlight text
        SpannableString cardIdText = new SpannableString(card.getIdStr());
        if (!Strings.isNullOrEmpty(searchText)) {
            Matcher matcher = pattern.matcher(card.getIdStr().toLowerCase());
            while (matcher.find()) {
                cardIdText.setSpan(new ForegroundColorSpan(view.getResources().getColor(R.color.colorPrimaryLight)), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        cardId.setText(card.getIdStr());

        // cardName: highlight text
        SpannableString cardNameText = new SpannableString(card.getName().getDe());
        if (!Strings.isNullOrEmpty(searchText)) {
            Matcher matcher = pattern.matcher(card.getName().getDe().toLowerCase());
            while (matcher.find()) {
                cardNameText.setSpan(new ForegroundColorSpan(view.getResources().getColor(R.color.colorPrimaryLight)), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        cardName.setText(cardNameText);

        // cardRarity
        if (card.getRarity() != null) {
            cardRarity.setText(view.getResources().getString(R.string.fmt_rarity, card.getRarity()));
        } else {
            cardRarity.setText(card.getRarity());
        }

        // cardImage

        CardImageLoader.loadImageIntoImageView(GlideApp.with(view), card.getImageStorageUrl(), cardImage);

        // cardAvgRating
        cardRatingBar.setRating(card.getAvgRating());

        // cardNumRatings
        if (card.getNumRatings() == 0) {
            cardNumRatings.setText(R.string.fmt_no_ratings);
        } else {
            cardNumRatings.setText(view.getResources().getQuantityString(R.plurals.fmt_num_ratings, card.getNumRatings(), card.getNumRatings()));
        }

        // ICardSelectedListener
        view.setOnClickListener(v -> listener.onCardSelectedListener(cardImage, card));
    }
}
