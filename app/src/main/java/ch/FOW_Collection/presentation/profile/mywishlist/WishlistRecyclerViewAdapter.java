package ch.FOW_Collection.presentation.profile.mywishlist;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.core.app.ComponentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import ch.FOW_Collection.presentation.utils.EntityDiffItemCallback;
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
import ch.FOW_Collection.domain.models.Card;
import ch.FOW_Collection.domain.models.Beer;
import ch.FOW_Collection.domain.models.Wish;
import ch.FOW_Collection.presentation.utils.EntityPairDiffItemCallback;
import com.google.firebase.storage.FirebaseStorage;


public class WishlistRecyclerViewAdapter extends ListAdapter<Wish, WishlistRecyclerViewAdapter.ViewHolder> {
        //extends ListAdapter<Pair<Wish, Card>, WishlistRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "WishlistRecyclerViewAda";

    //private static final DiffUtil.ItemCallback<Pair<Wish, Card>> DIFF_CALLBACK = new EntityPairDiffItemCallback<>();
    private static final DiffUtil.ItemCallback<Wish> DIFF_CALLBACK = new EntityDiffItemCallback<>();

    private final Context context;
    private final LifecycleOwner lifecycleOwner;
    private final OnWishlistItemInteractionListener listener;

    public WishlistRecyclerViewAdapter(WishlistActivity listener) {
        this(listener, listener, listener);
    }

    public WishlistRecyclerViewAdapter(ComponentActivity activity, OnWishlistItemInteractionListener listener) {
        this(activity, activity, listener);
    }

    public WishlistRecyclerViewAdapter(Context context, LifecycleOwner lifecycleOwner, OnWishlistItemInteractionListener listener) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.lifecycleOwner = lifecycleOwner;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.activity_my_wishlist_listentry, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        //Pair<Wish, Card> item = getItem(position);
        Wish wish = getItem(position);
        //Log.d(TAG, "item.wish = " + item.first.toString() + " item.card = " + item.second.toString());
        holder.bind(wish, listener);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name)
        TextView name;

//        @BindView(R.id.manufacturer)
//        TextView manufacturer;

        @BindView(R.id.category)
        TextView category;

        @BindView(R.id.photo)
        ImageView photo;

        @BindView(R.id.ratingBar)
        RatingBar ratingBar;

        @BindView(R.id.numRatings)
        TextView numRatings;

        @BindView(R.id.addedAt)
        TextView addedAt;

        @BindView(R.id.removeFromWishlist)
        Button remove;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);
        }

        void bind(Wish wish, /*Card item,*/ OnWishlistItemInteractionListener listener) {
            wish.getCard().observe(lifecycleOwner, new Observer<Card>() {
                @Override
                public void onChanged(Card item) {
                    if (item != null) {

                        name.setText(item.getName().getDe());

                        GlideApp.with(itemView)
                                .load(FirebaseStorage.getInstance().getReference().child(item.getImageStorageUrl()))
                                .apply(new RequestOptions().override(240, 240).centerInside()).into(photo);

                        ratingBar.setNumStars(5);
                        ratingBar.setRating(item.getAvgRating());
                        numRatings.setText(itemView.getResources().getString(R.string.fmt_num_ratings, item.getNumRatings()));
                        itemView.setOnClickListener(v -> listener.onMoreClickedListener(photo, item));

                        String formattedDate =
                                DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT).format(wish.getAddedAt());
                        addedAt.setText(formattedDate);
                        remove.setOnClickListener(v -> listener.onWishClickedListener(item));
                    }
                }
            });
        }

    }
}
