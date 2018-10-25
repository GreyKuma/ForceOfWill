package ch.FOW_Collection.presentation.shared.cardList;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.FOW_Collection.R;
import ch.FOW_Collection.domain.models.Card;
import ch.FOW_Collection.presentation.shared.CardImageLoader;


/**
 * The ViewHolder class holds a reference to the layout that we instantiated and takes care of setting all the
 * values in the layout. So we see the now familiar pattern of {@link BindView} calls to get to the view elements
 * followed by a lot of setter calls.
 */
public class CardListViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.content)
    TextView content;

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    @BindView(R.id.imageCard)
    ImageView imageView;

    CardListViewHolder(View view) {
        super(view);

        ButterKnife.bind(this, itemView);
    }

    /**
     * The entries in the list are rather simple so there's not that much data to bind to the view elements. The
     * categories don't really have a background image assigned so we just get one from a helper class.
     * And finally, we register the listener that we have passed through so many layer at the itemView. The
     * itemView is the whole layout, meaning that the user can click anywhere on the list item to invoke the
     * callback. It's always a good idea to make these touch targets as large as possible. As an experiment, try
     * binding the callback to the content instead and see how much harder it will be to interact with the list
     * item.
     */
    void bind(Card item, ICardListFragmentListener listener, RequestManager glide) {
        content.setText(item.getName().getDe());
        ratingBar.setRating(item.getAvgRating());

        if (listener != null) {
            itemView.setOnClickListener(v -> listener.onMoreClickedListener(imageView, item));
        }

        CardImageLoader.loadImageIntoImageView(glide, item.getImageStorageUrl(), imageView);
    }
}