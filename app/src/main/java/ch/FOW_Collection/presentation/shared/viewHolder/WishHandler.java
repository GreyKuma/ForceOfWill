package ch.FOW_Collection.presentation.shared.viewHolder;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import ch.FOW_Collection.R;
import ch.FOW_Collection.domain.models.Card;
import ch.FOW_Collection.domain.models.Wish;
import ch.FOW_Collection.presentation.shared.IWishClickedListener;

import java.text.DateFormat;

import static ch.FOW_Collection.presentation.utils.DrawableHelpers.setDrawableTint;

public interface WishHandler {
    default void bindWishHandler(View view, Wish wish, Card card, IWishClickedListener listener) {
        TextView wishAddedAt = view.findViewById(R.id.wishAddedAt);
        ToggleButton wishStatus = view.findViewById(R.id.wishStatus);

        wishStatus.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
            if (isChecked) {
                setDrawableTint(buttonView, buttonView.getResources().getColor(R.color.colorPrimaryLight));
            } else {
                setDrawableTint(buttonView, buttonView.getResources().getColor(android.R.color.darker_gray));
            }
        });

        if (wish != null) {
            String formattedDate =
                    DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT).format(wish.getAddedAt());
            wishAddedAt.setText(formattedDate);

            wishStatus.setChecked(true);
        } else {
            wishStatus.setChecked(false);
        }

        wishStatus.setOnClickListener(v -> listener.onWishClickedListener(card));
    }
}
