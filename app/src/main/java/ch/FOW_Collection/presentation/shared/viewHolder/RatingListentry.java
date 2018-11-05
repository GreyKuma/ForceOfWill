package ch.FOW_Collection.presentation.shared.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import ch.FOW_Collection.GlideApp;
import ch.FOW_Collection.R;
import ch.FOW_Collection.domain.models.Rating;
import ch.FOW_Collection.domain.models.User;
import ch.FOW_Collection.presentation.shared.CardImageLoader;
import ch.FOW_Collection.presentation.shared.IRatingLikedListener;
import com.bumptech.glide.request.RequestOptions;

import java.text.DateFormat;

public interface RatingListentry {
    /**
     * Bind values to view <a href="file://../../../../res/layout/fragment_rating_listentry.xml">fragment_rating_listentry.xml</a>
     *
     * @param view     {@code itemView}
     * @param userId   Current userId for "Liked"
     * @param item     The Rating to bind to
     * @param listener The Listener for "Liked"-OnClick
     */
    default void bindRatingListentry(View view, @Nullable LiveData<String> userId, Rating item, @Nullable IRatingLikedListener listener) {
        TextView ratingComment = view.findViewById(R.id.ratingComment);
        ImageView ratingAvatar = view.findViewById(R.id.ratingAvatar);
        RatingBar ratingRatingBar = view.findViewById(R.id.ratingRatingBar);
        TextView ratingAuthorName = view.findViewById(R.id.ratingAuthorName);
        TextView ratingDate = view.findViewById(R.id.ratingDate);
        TextView ratingNumLikes = view.findViewById(R.id.ratingNumLikes);
        ImageView ratingLike = view.findViewById(R.id.ratingLike);
        ImageView ratingImage = view.findViewById(R.id.ratingImage);

        ratingComment.setText(item.getComment());
        ratingRatingBar.setRating(item.getRating());
        String formattedDate =
                DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT).format(item.getCreationDate());
        ratingDate.setText(formattedDate);

        if (item.getPhoto() != null) {
            GlideApp.with(view).load(item.getPhoto()).into(ratingImage);
            ratingImage.setVisibility(View.VISIBLE);
            ratingImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CardImageLoader.openImageExtern(view.getContext(), item.getPhoto());
                }
            });
        } else {
            GlideApp.with(view).clear(ratingImage);
            ratingImage.setVisibility(View.GONE);
        }

        // observer Forever, but remove observer when first called -> Promise
        item.getUser().observeForever(new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null) {
                    item.getUser().removeObserver(this);

                    ratingAuthorName.setText(user.getName());
                    GlideApp.with(view).load(user.getPhoto()).apply(new RequestOptions().circleCrop()).into(ratingAvatar);
                }
            }
        });

        if (item.getLikes().size() == 0) {
            ratingNumLikes.setText(R.string.fmt_no_likes);
        } else {
            ratingNumLikes.setText(view.getResources().getQuantityString(R.plurals.fmt_num_likes, item.getLikes().size(), item.getLikes().size()));
        }

        if (userId != null) {
            // ratingLike.setVisibility(View.VISIBLE);
            if (item.getLikes().containsKey(userId.getValue())) {
                ratingLike.setColorFilter(view.getResources().getColor(R.color.colorPrimary));
            } else {
                ratingLike.setColorFilter(view.getResources().getColor(android.R.color.darker_gray));
            }
        }/* else {
            ratingLike.setVisibility(View.GONE);
        }*/

        if (listener != null) {
            ratingLike.setOnClickListener(v -> listener.onRatingLikedListener(item));
        }
    }
}
