package ch.FOW_Collection.presentation.profile.mycollection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.FOW_Collection.GlideApp;
import ch.FOW_Collection.R;
import ch.FOW_Collection.domain.models.*;
import ch.FOW_Collection.presentation.profile.mycollection.OnMyCardItemInteractionListener;
import ch.FOW_Collection.presentation.utils.DrawableHelpers;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;


public class MyCollectionRecyclerViewAdapter extends ListAdapter<MyCard, MyCollectionRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "MyCollectionRecyclerViewAdap";

    private static final DiffUtil.ItemCallback<MyCard> DIFF_CALLBACK = new DiffUtil.ItemCallback<MyCard>() {
        @Override
        public boolean areItemsTheSame(@NonNull MyCard oldItem, @NonNull MyCard newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull MyCard oldItem, @NonNull MyCard newItem) {
            return oldItem.equals(newItem);
        }
    };

    private final OnMyCardItemInteractionListener listener;
    private FirebaseUser user;

    public MyCollectionRecyclerViewAdapter(OnMyCardItemInteractionListener listener, FirebaseUser user) {
        super(DIFF_CALLBACK);
        this.listener = listener;
        this.user = user;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.activity_my_beers_listentry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        MyCard entry = getItem(position);
        holder.bind(entry, listener);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name)
        TextView name;

//        @BindView(R.id.manufacturer)
//        TextView manufacturer;

//        @BindView(R.id.category)
//        TextView category;

        @BindView(R.id.photo)
        ImageView photo;

//        @BindView(R.id.ratingBar)
//        RatingBar ratingBar;
//
//        @BindView(R.id.numRatings)
//        TextView numRatings;

        @BindView(R.id.addedAt)
        TextView addedAt;

        @BindView(R.id.onTheListSince)
        TextView onTheListSince;

        @BindView(R.id.removeFromWishlist)
        Button removeFromWishlist;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);
        }

        public void bind(MyCard entry, OnMyCardItemInteractionListener listener) {

//            Card item = entry.getCard();

//            name.setText(item.getName().getDe());
//            manufacturer.setText(item.getManufacturer());
//            category.setText(item.getCategory());
//            name.setText(item.getName());
//            GlideApp.with(itemView).load(item.getPhoto()).apply(new RequestOptions().override(240, 240).centerInside())
//                    .into(photo);
//            GlideApp.with(itemView)
//                    .load(FirebaseStorage.getInstance().getReference().child(item.getImageStorageUrl()))
//                    .apply(new RequestOptions().override(240, 240).centerInside()).into(photo);
//            ratingBar.setNumStars(5);
//            ratingBar.setRating(item.getAvgRating());
//            numRatings.setText(itemView.getResources().getString(R.string.fmt_num_ratings, item.getNumRatings()));
//            itemView.setOnClickListener(v -> listener.onMoreClickedListener(photo, item));
//            removeFromWishlist.setOnClickListener(v -> listener.onWishClickedListener(item));

//            String formattedDate =
//                    DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT).format(entry.getDate());
//            addedAt.setText(formattedDate);
        }
    }
}
