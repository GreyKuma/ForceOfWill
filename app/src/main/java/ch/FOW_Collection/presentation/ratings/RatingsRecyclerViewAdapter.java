package ch.FOW_Collection.presentation.ratings;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import ch.FOW_Collection.domain.models.Card;
import ch.FOW_Collection.domain.models.User;
import com.bumptech.glide.request.RequestOptions;

import java.text.DateFormat;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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

import static ch.FOW_Collection.presentation.utils.DrawableHelpers.setDrawableTint;


public class RatingsRecyclerViewAdapter extends ListAdapter<Pair<Rating, Wish>, RatingsRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RatingsRecyclerViewAdap";

    private static final DiffUtil.ItemCallback<Pair<Rating, Wish>> DIFF_CALLBACK = new EntityPairDiffItemCallback<>();

    private final OnRatingsItemInteractionListener listener;
    private Fragment fragment;
    private final User user;

    public RatingsRecyclerViewAdapter(OnRatingsItemInteractionListener listener, Fragment fragment, User user) {
        super(DIFF_CALLBACK);
        this.listener = listener;
        this.fragment = fragment;
        this.user = user;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.fragment_ratings_listentry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Pair<Rating, Wish> item = getItem(position);
        holder.bind(item.first, item.second, listener);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.comment)
        TextView comment;

        @BindView(R.id.beerName)
        TextView beerName;

        @BindView(R.id.avatar)
        ImageView avatar;

        @BindView(R.id.cardRatingBar)
        RatingBar ratingBar;

        @BindView(R.id.authorName)
        TextView authorName;

        @BindView(R.id.date)
        TextView date;

        @BindView(R.id.numLikes)
        TextView numLikes;

        @BindView(R.id.details)
        Button details;

        @BindView(R.id.wishlist)
        Button wishlist;

        @BindView(R.id.like)
        Button like;

        @BindView(R.id.cardImage)
        ImageView photo;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);
        }

        void bind(Rating item, Wish wish, OnRatingsItemInteractionListener listener) {
            // TODO This code is almost the same in MyBeersRecyclerViewAdapter.. could be simplified
            // with databinding!
            item.getCard().observeForever(new Observer<Card>() {
                @Override
                public void onChanged(Card card) {
                    if (card != null) {
                        item.getCard().removeObserver(this);
                        beerName.setText(card.getName().getDe());
                    }
                }
            });
            comment.setText(item.getComment());

            ratingBar.setNumStars(5);
            ratingBar.setRating(item.getRating());
            String formattedDate =
                    DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT).format(item.getCreationDate());
            date.setText(formattedDate);

            if (item.getPhoto() != null) {
                // Take a look at https://bumptech.github.io/glide/int/recyclerview.html
                GlideApp.with(fragment).load(item.getPhoto()).into(photo);
                photo.setVisibility(View.VISIBLE);
            } else {
                GlideApp.with(fragment).clear(photo);
                photo.setVisibility(View.GONE);
            }

            item.getUser().observeForever(new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    if (user != null) {
                        item.getUser().removeObserver(this);
                        authorName.setText(user.getName());
                        GlideApp.with(itemView).load(user.getPhoto()).apply(new RequestOptions().circleCrop()).into(avatar);
                    }
                }
            });

            numLikes.setText(itemView.getResources().getQuantityString(R.plurals.fmt_num_likes, item.getLikes().size(), item.getLikes().size()));

            if (item.getLikes().containsKey(user.getId())) {
                int color = fragment.getResources().getColor(R.color.colorPrimary);
                setDrawableTint(like, color);
            } else {
                int color = fragment.getResources().getColor(android.R.color.darker_gray);
                setDrawableTint(like, color);
            }

            if (wish != null) {
                int color = fragment.getResources().getColor(R.color.colorPrimary);
                setDrawableTint(wishlist, color);
            } else {
                int color = fragment.getResources().getColor(android.R.color.darker_gray);
                setDrawableTint(wishlist, color);
            }

            if (listener != null) {
                like.setOnClickListener(v -> listener.onRatingLikedListener(item));
                details.setOnClickListener(v -> listener.onMoreClickedListener(item));
                wishlist.setOnClickListener(v -> listener.onWishClickedListener(item));
            }
        }
    }
}
