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
import ch.FOW_Collection.domain.models.*;

import ch.FOW_Collection.presentation.utils.EntityDiffItemCallback;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;


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
        View view = layoutInflater.inflate(R.layout.activity_my_collection_item, parent, false);
        return new MyCollectionRecyclerViewAdapter.ViewHolder(view);
    }


    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public void onBindViewHolder(@NonNull final MyCollectionRecyclerViewAdapter.ViewHolder holder, int position) {
        MyCard myCard = getItem(position);
        //Log.d(TAG, "item.wish = " + item.first.toString() + " item.card = " + item.second.toString());
        holder.bind(myCard, listener);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cardName)
        TextView name;


        @BindView(R.id.category) TextView category;


        @BindView(R.id.removeFromCollection) Button remove;
        @BindView(R.id.cardImage)
        ImageView photo;

        @BindView(R.id.cardRatingBar)
        RatingBar ratingBar;

        @BindView(R.id.cardNumRatings)
        TextView numRatings;


        @BindView(R.id.normal1Down) Button normal1Down;
        @BindView(R.id.normal1Up) Button normal1Up;
        @BindView(R.id.foil1Down) Button foil1Down;
        @BindView(R.id.foil1Up) Button foil1Up;
        @BindView(R.id.normalAmount) TextView normalAmount;
        @BindView(R.id.foilAmount) TextView foilAmount;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);
        }

        void bind(MyCard myCard, OnMyCardItemInteractionListener listener) {
            myCard.getCard().observe(lifecycleOwner, item -> {
                if (item != null) {
                    name.setText(item.getName().getDe());
                    GlideApp.with(itemView)
                            .load(FirebaseStorage.getInstance().getReference().child(item.getImageStorageUrl()))
                            .apply(new RequestOptions().override(240, 240).centerInside()).into(photo);

                    itemView.setOnClickListener(v -> listener.onMoreClickedListener(photo, item));
                    remove.setOnClickListener(v -> listener.onWishClickedListener(item));
                    normal1Up.setOnClickListener(v -> listener.onNormalUpClickedListener(myCard));
                    normal1Down.setOnClickListener(v -> listener.onNormalDownClickedListener(myCard));
                    foil1Up.setOnClickListener(v -> listener.onFoilUpClickedListener(myCard));
                    foil1Down.setOnClickListener(v -> listener.onFoilDownClickedListener(myCard));
                }
            });
        }

    }
}
