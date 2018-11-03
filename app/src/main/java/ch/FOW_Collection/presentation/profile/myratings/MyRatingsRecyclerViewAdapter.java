package ch.FOW_Collection.presentation.profile.myratings;

import android.graphics.drawable.Drawable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import ch.FOW_Collection.domain.models.Card;
import ch.FOW_Collection.domain.models.User;
import ch.FOW_Collection.presentation.shared.CardImageLoader;
import ch.FOW_Collection.presentation.shared.ICardSelectedListener;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;

import java.text.DateFormat;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.FOW_Collection.GlideApp;
import ch.FOW_Collection.R;
import ch.FOW_Collection.domain.models.Rating;
import ch.FOW_Collection.domain.models.Wish;
import ch.FOW_Collection.presentation.utils.EntityPairDiffItemCallback;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.storage.FirebaseStorage;


public class MyRatingsRecyclerViewAdapter
        extends ListAdapter<Pair<Rating, Wish>, MyRatingsRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "WishlistRecyclerViewAda";

    private static final DiffUtil.ItemCallback<Pair<Rating, Wish>> DIFF_CALLBACK = new EntityPairDiffItemCallback<>();

    private final ICardSelectedListener listener;

    public MyRatingsRecyclerViewAdapter(ICardSelectedListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.activity_my_ratings_listentry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Pair<Rating, Wish> entry = getItem(position);
        holder.bind(entry.first, entry.second, listener);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cardName)
        TextView cardName;

        @BindView(R.id.cardId)
        TextView cardId;

        @BindView(R.id.category)
        TextView category;

        @BindView(R.id.cardRatingBar)
        RatingBar cardRatingBar;

        @BindView(R.id.cardNumRatings)
        TextView cardNumRatings;

        @BindView(R.id.cardImage)
        ImageView cardImage;

        @BindView(R.id.ratingAvatar)
        ImageView ratingAvatar;

        @BindView(R.id.ratingAuthorName)
        TextView ratingAuthorName;

        @BindView(R.id.ratingComment)
        TextView ratingComment;

        @BindView(R.id.ratingRatingBar)
        RatingBar ratingRatingBar;

        @BindView(R.id.ratingDate)
        TextView ratingDate;

        @BindView(R.id.ratingNumLikes)
        TextView ratingNumLikes;

        @BindView(R.id.ratingImage)
        ImageView ratingImage;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);
        }

        void bind(Rating rating, Wish wish, ICardSelectedListener listener) {
            rating.getCard().observeForever(new Observer<Card>() {
                @Override
                public void onChanged(Card card) {
                    if (card != null) {
                        itemView.setOnClickListener(v -> listener.onCardSelectedListener(cardImage, card));
                        rating.getCard().removeObserver(this);
                        cardName.setText(card.getName().getDe());
                        if(card.getRarity() != null){
                            category.setText(itemView.getResources().getString(R.string.fmt_rarity, card.getRarity()));
                        }else{
                            category.setText(card.getRarity());
                        }
                        cardId.setText(card.getId());
                        cardNumRatings.setText(itemView.getResources().getQuantityString(R.plurals.fmt_num_ratings, card.getNumRatings(), card.getNumRatings()));
                        GlideApp.with(itemView)
                                .load(FirebaseStorage.getInstance().getReference().child(card.getImageStorageUrl()))
                                .apply(new RequestOptions().centerInside())
                                .apply(CardImageLoader.imageRenderer)
                                // for the animation
                                .dontAnimate()
                                .into(cardImage);
                    }
                }
            });

            if (rating.getComment() != null && rating.getComment().length() > 0) {
                ratingComment.setText(rating.getComment());
                ratingComment.setVisibility(View.VISIBLE);
            } else {
                ratingComment.setVisibility(View.GONE);
            }

            cardRatingBar.setNumStars(5);
            cardRatingBar.setRating(rating.getRating());
            String formattedDate =
                    DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT).format(rating.getCreationDate());
            ratingDate.setText(formattedDate);

            if (rating.getPhoto() != null) {
                // Take a look at https://bumptech.github.io/glide/int/recyclerview.html
                GlideApp.with(itemView).load(rating.getPhoto()).into(ratingImage);
                ratingImage.setVisibility(View.VISIBLE);
            } else {
                GlideApp.with(itemView).clear(ratingImage);
                ratingImage.setVisibility(View.GONE);
            }

            rating.getUser().observeForever(new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    if (user != null) {
                        rating.getUser().removeObserver(this);
                        ratingAuthorName.setText(user.getName());
                        GlideApp.with(itemView).load(user.getPhoto()).apply(new RequestOptions().circleCrop()).into(ratingAvatar);
                    }
                }
            });

            if(rating.getLikes().size() == 0){
                ratingNumLikes.setText(R.string.fmt_no_likes);
            }else{
                ratingNumLikes.setText(itemView.getResources().getQuantityString(R.plurals.fmt_num_likes, rating.getLikes().size(), rating.getLikes().size()));
            }

            /*
            // don't need it here
            like.setVisibility(View.GONE);

            if (wish != null) {
                int color = itemView.getResources().getColor(R.color.colorPrimaryLight);
                setDrawableTint(wishlist, color);
            } else {
                int color = itemView.getResources().getColor(android.R.color.darker_gray);
                setDrawableTint(wishlist, color);
            }

            if (listener != null) {
                details.setOnClickListener(v -> listener.onMoreClickedListener(item));
                wishlist.setOnClickListener(v -> listener.onWishClickedListener(item));
            }*/
        }
    }
}
