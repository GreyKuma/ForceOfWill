package ch.FOW_Collection.presentation.explore.search.cards;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SearchResultRecyclerViewAdapter extends ListAdapter<Card, SearchResultRecyclerViewAdapter.ViewHolder> {

    private static final EntityDiffItemCallback<Card> DIFF_CALLBACK = new EntityDiffItemCallback<>();

    private final OnItemSelectedListener listener;
    private final MutableLiveData<String> searchText;

    public SearchResultRecyclerViewAdapter(OnItemSelectedListener listener, @Nullable MutableLiveData<String> searchText) {
        super(DIFF_CALLBACK);
        this.listener = listener;
        this.searchText = searchText;
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
        holder.bind(getItem(position), listener, searchText.getValue());
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cardName)
        TextView name;

        @BindView(R.id.category)
        TextView category;

        @BindView(R.id.cardImage)
        ImageView photo;

        @BindView(R.id.cardRatingBar)
        RatingBar ratingBar;

        @BindView(R.id.cardNumRatings)
        TextView numRatings;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);
        }

        void bind(Card card, OnItemSelectedListener listener, String searchText) {
            // highlight text
            SpannableString cardName = new SpannableString(card.getName().getDe());
            Pattern pattern = Pattern.compile(searchText);
            Matcher matcher = pattern.matcher(card.getName().getDe().toLowerCase());
            while (matcher.find()) {
                cardName.setSpan(new ForegroundColorSpan(itemView.getResources().getColor(R.color.colorPrimaryLight)), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            name.setText(cardName);


            category.setText(card.getRarity());
            GlideApp.with(itemView).load(card.getImageSrcUrl()).apply(new RequestOptions().override(240, 240).centerInside())
                    .into(photo);
            ratingBar.setNumStars(5);
            ratingBar.setRating(card.getAvgRating());
            numRatings.setText(itemView.getResources().getQuantityString(R.plurals.fmt_num_ratings, card.getNumRatings(), card.getNumRatings()));
            itemView.setOnClickListener(v -> listener.onSearchResultListItemSelected(photo, card));
        }
    }
}
