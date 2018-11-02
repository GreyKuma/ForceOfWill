package ch.FOW_Collection.presentation.profile.mycollection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.app.ComponentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.FOW_Collection.GlideApp;
import ch.FOW_Collection.R;
import ch.FOW_Collection.data.repositories.MyCollectionRepository;
import ch.FOW_Collection.domain.models.*;
import ch.FOW_Collection.presentation.profile.mycollection.OnMyCardItemInteractionListener;

import ch.FOW_Collection.presentation.utils.DrawableHelpers;
import ch.FOW_Collection.presentation.utils.EntityDiffItemCallback;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;

import java.text.DateFormat;


public class MyCollectionRecyclerViewAdapter extends ListAdapter<MyCard, MyCollectionRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "CollectionRecyclerViewAdapter";

    private static final DiffUtil.ItemCallback<MyCard> DIFF_CALLBACK = new EntityDiffItemCallback<>();

    private final Context context;
    private final LifecycleOwner lifecycleOwner;
    private final OnMyCardItemInteractionListener listener;

    public MyCollectionRecyclerViewAdapter(MyCollectionActivity listener) {
        this(listener, listener, listener);
    }

    public MyCollectionRecyclerViewAdapter(ComponentActivity activity, OnMyCardItemInteractionListener listener) {
        this(activity, activity, listener);
    }

    public MyCollectionRecyclerViewAdapter(Context context, LifecycleOwner lifecycleOwner, OnMyCardItemInteractionListener listener) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.lifecycleOwner = lifecycleOwner;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyCollectionRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        //View view = layoutInflater.inflate(R.layout.activity_my_collection_listentryl, parent, false);
        //return new ViewHolder(view);

        View view = layoutInflater.inflate(R.layout.activity_my_wishlist_listentry, parent, false);
        return new MyCollectionRecyclerViewAdapter.ViewHolder(view);
    }


    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public void onBindViewHolder(@NonNull final MyCollectionRecyclerViewAdapter.ViewHolder holder, int position) {
        //Pair<Wish, Card> item = getItem(position);
        MyCard myCard = getItem(position);
        //Log.d(TAG, "item.wish = " + item.first.toString() + " item.card = " + item.second.toString());
        holder.bind(myCard, listener);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cardName)
        TextView name;

        @BindView(R.id.cardId)
        TextView cardId;

        @BindView(R.id.category)
        TextView category;

        @BindView(R.id.cardImage)
        ImageView photo;

        @BindView(R.id.cardRatingBar)
        RatingBar ratingBar;

        @BindView(R.id.cardNumRatings)
        TextView numRatings;

        @BindView(R.id.addedAt)
        TextView addedAt;

        @BindView(R.id.removeFromWishlist)
        Button remove;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);
        }

        void bind(MyCard myCard, /*Card item,*/ OnMyCardItemInteractionListener listener) {
            myCard.getCard().observe(lifecycleOwner, new Observer<Card>() {
                @Override
                public void onChanged(Card item) {
                    if (item != null) {
                        name.setText(item.getName().getDe());
                        cardId.setText(item.getIdStr());
                        if(item.getRarity() != null){
                            category.setText(itemView.getResources().getString(R.string.fmt_rarity, item.getRarity()));
                        }else{
                            category.setText(item.getRarity());
                        }
                        //TODO get date when the card was added to the collection
                        addedAt.setText("added at");
                        GlideApp.with(itemView)
                                .load(FirebaseStorage.getInstance().getReference().child(item.getImageStorageUrl()))
                                .apply(new RequestOptions().override(240, 240).centerInside()).into(photo);
                        ratingBar.setNumStars(5);
                        ratingBar.setRating(item.getAvgRating());
                        numRatings.setText(itemView.getResources().getString(R.string.fmt_num_ratings, item.getNumRatings()));
                        itemView.setOnClickListener(v -> listener.onMoreClickedListener(photo, item));
                    }
                }
            });
        }

    }
}
