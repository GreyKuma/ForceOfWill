package ch.FOW_Collection.presentation.cardDetails;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;
import ch.FOW_Collection.R;
import ch.FOW_Collection.domain.models.Rating;
import ch.FOW_Collection.presentation.shared.IRatingLikedListener;
import ch.FOW_Collection.presentation.shared.viewHolder.RatingListentry;
import ch.FOW_Collection.presentation.utils.EntityDiffItemCallback;


public class RatingsRecyclerViewAdapter extends ListAdapter<Rating, RatingsRecyclerViewAdapter.ViewHolder> {
    private static final EntityDiffItemCallback<Rating> DIFF_CALLBACK = new EntityDiffItemCallback<>();

    private final IRatingLikedListener listener;
    private LiveData<String> userId;

    public RatingsRecyclerViewAdapter(IRatingLikedListener listener, LiveData<String> userId) {
        super(DIFF_CALLBACK);
        this.listener = listener;
        this.userId = userId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.fragment_rating_listentry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        // holder.bind(getItem(position), listener);
        holder.bind(userId, getItem(position), listener);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements RatingListentry {
        /*
        @BindView(R.id.ratingComment)
        TextView ratingComment;

        @BindView(R.id.ratingAvatar)
        ImageView ratingAvatar;

        @BindView(R.id.ratingRatingBar)
        RatingBar ratingRatingBar;

        @BindView(R.id.ratingAuthorName)
        TextView ratingAuthorName;

        @BindView(R.id.ratingDate)
        TextView ratingDate;

        @BindView(R.id.ratingNumLikes)
        TextView ratingNumLikes;

        @BindView(R.id.ratingLike)
        ImageView ratingLike;

        @BindView(R.id.ratingImage)
        ImageView ratingImage;*/

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);
        }

        void bind(LiveData<String> userId, Rating item, IRatingLikedListener listener) {
            bindRatingListentry(itemView, userId, item, listener);
        }

        /*
        void bind(Rating item, OnRatingLikedListener listener) {
            ratingComment.setText(item.getComment());

            ratingRatingBar.setNumStars(5);
            ratingRatingBar.setRating(item.getRating());
            String formattedDate =
                    DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT).format(item.getCreationDate());
            ratingDate.setText(formattedDate);

            if (item.getPhoto() != null) {
                GlideApp.with(itemView).load(item.getPhoto()).into(ratingImage);
                ratingImage.setVisibility(View.VISIBLE);
            } else {
                GlideApp.with(itemView).clear(ratingImage);
                ratingImage.setVisibility(View.GONE);
            }

            item.getUser().observeForever(new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    if (user != null) {
                        item.getUser().removeObserver(this);

                        ratingAuthorName.setText(user.getName());
                        GlideApp.with(itemView).load(user.getPhoto()).apply(new RequestOptions().circleCrop()).into(ratingAvatar);
                    }
                }
            });
            if(item.getLikes().size() == 0){
                ratingNumLikes.setText(R.string.fmt_no_likes);
            }else{
                ratingNumLikes.setText(itemView.getResources().getQuantityString(R.plurals.fmt_num_likes, item.getLikes().size(), item.getLikes().size()));
            }
            if (item.getLikes().containsKey(userId.getValue())) {
                ratingLike.setColorFilter(itemView.getResources().getColor(R.color.colorPrimary));
            } else {
                ratingLike.setColorFilter(itemView.getResources().getColor(android.R.color.darker_gray));
            }
            if (listener != null) {
                ratingLike.setOnClickListener(v -> listener.onRatingLikedListener(item));
            }
        }*/
    }
}
