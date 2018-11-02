package ch.FOW_Collection.presentation.explore.search.cards;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.FOW_Collection.GlideApp;
import ch.FOW_Collection.R;
import ch.FOW_Collection.domain.models.Card;
import ch.FOW_Collection.presentation.explore.search.cards.SearchResultFragment.OnItemSelectedListener;
import ch.FOW_Collection.presentation.utils.EntityDiffItemCallback;


public class SearchResultRecyclerViewAdapter extends ListAdapter<Card, SearchResultRecyclerViewAdapter.ViewHolder> {

    private static final EntityDiffItemCallback<Card> DIFF_CALLBACK = new EntityDiffItemCallback<>();

    private final OnItemSelectedListener listener;

    public SearchResultRecyclerViewAdapter(OnItemSelectedListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.fragment_searchresult_listentry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.bind(getItem(position), listener);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.rarity)
        TextView rarityLable;

        @BindView(R.id.category)
        TextView category;

        @BindView(R.id.cardId)
        TextView cardId;

        @BindView(R.id.photo)
        ImageView photo;

        @BindView(R.id.ratingBar)
        RatingBar ratingBar;

        @BindView(R.id.numRatings)
        TextView numRatings;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);
        }

        void bind(Card card, OnItemSelectedListener listener) {
            name.setText(card.getName().getDe());
            if(card.getRarity() != null){
                category.setText(itemView.getResources().getString(R.string.fmt_rarity, card.getRarity()));
            }else{
                category.setText(card.getRarity());
            }
            cardId.setText(card.getIdStr());
            GlideApp.with(itemView).load(card.getImageSrcUrl()).apply(new RequestOptions().override(240, 240).centerInside())
                    .into(photo);
            ratingBar.setNumStars(5);
            ratingBar.setRating(card.getAvgRating());
            numRatings.setText(itemView.getResources().getString(R.string.fmt_num_ratings, card.getNumRatings()));
            itemView.setOnClickListener(v -> listener.onSearchResultListItemSelected(photo, card));
        }
    }
}
