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

import ch.FOW_Collection.presentation.utils.StringDiffItemCallback;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;


public class MyCollectionRecyclerViewAdapter extends ListAdapter<String, MyCollectionRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "CollectionRecyclerViewAdapter";

    private static final DiffUtil.ItemCallback<String> DIFF_CALLBACK = new StringDiffItemCallback();

    private final Context context;
    private final LifecycleOwner lifecycleOwner;
    private final OnMyCardItemInteractionListener listener;
    private final MyCollectionViewModel viewModel;

    public MyCollectionRecyclerViewAdapter(MyCollectionActivity listener, MyCollectionViewModel viewModel) {
        this(listener, listener, listener, viewModel);
    }

    public MyCollectionRecyclerViewAdapter(ComponentActivity activity, OnMyCardItemInteractionListener listener, MyCollectionViewModel viewModel) {
        this(activity, activity, listener, viewModel);
    }

    public MyCollectionRecyclerViewAdapter(Context context, LifecycleOwner lifecycleOwner, OnMyCardItemInteractionListener listener, MyCollectionViewModel viewModel) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.lifecycleOwner = lifecycleOwner;
        this.listener = listener;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public MyCollectionRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        //View view = layoutInflater.inflate(R.layout.activity_my_wishlist_listentry, parent, false);
        View view = layoutInflater.inflate(R.layout.activity_my_collection_item, parent, false);

        return new MyCollectionRecyclerViewAdapter.ViewHolder(view);
    }


    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public void onBindViewHolder(@NonNull final MyCollectionRecyclerViewAdapter.ViewHolder holder, int position) {
        String myCardId = getItem(position);
        //Log.d(TAG, "item.wish = " + item.first.toString() + " item.card = " + item.second.toString());
        holder.bind(myCardId, listener, lifecycleOwner, viewModel);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cardName)
        TextView name;
        @BindView(R.id.category)
        TextView category;
        @BindView(R.id.removeFromCollection)
        Button remove;
        @BindView(R.id.cardImage)
        ImageView cardImage;
        @BindView(R.id.cardRatingBar)
        RatingBar ratingBar;
        @BindView(R.id.cardNumRatings)
        TextView numRatings;
        @BindView(R.id.cardId)
        TextView cardId;
        @BindView(R.id.normal1Down)
        Button normal1Down;
        @BindView(R.id.normal1Up)
        Button normal1Up;
        @BindView(R.id.foil1Down)
        Button foil1Down;
        @BindView(R.id.foil1Up)
        Button foil1Up;
        @BindView(R.id.normalAmount)
        TextView normalAmount;
        @BindView(R.id.foilAmount)
        TextView foilAmount;

        String myCardId;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);
        }

        void bind(String myCardId, OnMyCardItemInteractionListener listener, LifecycleOwner lifecycleOwner, MyCollectionViewModel viewModel) {
            // clear observer, when already observed
            if (this.myCardId != null && !this.myCardId.equals("")) {
                viewModel.getMyCardById(this.myCardId).removeObservers(lifecycleOwner);
            }
            // observe
            viewModel.getMyCardById(this.myCardId = myCardId).observe(lifecycleOwner, myCard -> {
                normalAmount.setText(Integer.toString(myCard.getAmountNormal()));
                normal1Up.setOnClickListener(v -> listener.onNormalUpClickedListener(myCard));
                normal1Down.setOnClickListener(v -> listener.onNormalDownClickedListener(myCard));

                foilAmount.setText(Integer.toString(myCard.getAmountFoil()));
                foil1Up.setOnClickListener(v -> listener.onFoilUpClickedListener(myCard));
                foil1Down.setOnClickListener(v -> listener.onFoilDownClickedListener(myCard));

                Observer<Card> cardObserver = new Observer<Card>() {
                    @Override
                    public void onChanged(Card card) {
                        if (card != null) {
                            myCard.getCard().removeObserver(this);

                            name.setText(card.getName().getDe());
                            cardId.setText(card.getIdStr());
                            if (card.getRarity() != null) {
                                category.setText(itemView.getResources().getString(R.string.fmt_rarity, card.getRarity()));
                            } else {
                                category.setText(card.getRarity());
                            }
                            GlideApp.with(itemView)
                                    .load(FirebaseStorage.getInstance().getReference().child(card.getImageStorageUrl()))
                                    .apply(new RequestOptions().override(240, 240).centerInside()).into(cardImage);
                            ratingBar.setNumStars(5);
                            ratingBar.setRating(card.getAvgRating());
                            if(card.getNumRatings() == 0){
                                numRatings.setText(R.string.fmt_no_ratings);
                            }else{
                                numRatings.setText(itemView.getResources().getQuantityString(R.plurals.fmt_num_ratings, card.getNumRatings(), card.getNumRatings()));
                            }
                            itemView.setOnClickListener(v -> listener.onMoreClickedListener(cardImage, card));
                            remove.setOnClickListener(v -> listener.onWishClickedListener(card));
                        }
                    }
                };
                myCard.getCard().observe(lifecycleOwner, cardObserver);
            });
        }

    }
}
