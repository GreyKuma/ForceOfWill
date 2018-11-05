package ch.FOW_Collection.presentation.explore.search.cards;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import ch.FOW_Collection.presentation.shared.ICardSelectedListener;
import ch.FOW_Collection.presentation.shared.viewHolder.CardBaseListentry;
import com.bumptech.glide.request.RequestOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.FOW_Collection.GlideApp;
import ch.FOW_Collection.R;
import ch.FOW_Collection.domain.models.Card;
import ch.FOW_Collection.presentation.explore.search.cards.SearchResultFragment.OnItemSelectedListener;
import ch.FOW_Collection.presentation.utils.EntityDiffItemCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SearchResultRecyclerViewAdapter extends RecyclerView.Adapter<SearchResultRecyclerViewAdapter.ViewHolder> {

    private static final EntityDiffItemCallback<Card> DIFF_CALLBACK = new EntityDiffItemCallback<>();

    private final ICardSelectedListener listener;
    private final MutableLiveData<String> searchText;
    private List<Card> cards;

    public SearchResultRecyclerViewAdapter(ICardSelectedListener listener, @Nullable MutableLiveData<String> searchText) {
        super();
        this.listener = listener;
        this.searchText = searchText;
        this.cards = new ArrayList<>();
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

    @Override
    public int getItemCount() {
        return this.cards.size();
    }

    public Card getItem(int position) {
        return this.cards.get(position);
    }

    public void submitList(List<Card> cards) {
        this.cards = cards;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements CardBaseListentry {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        void bind(Card card, ICardSelectedListener listener, String searchText) {
            bindCardBaseListentry(itemView, card, listener, searchText);
        }
    }
}
