package ch.FOW_Collection.presentation.explore.search.suggestions;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.FOW_Collection.R;
import ch.FOW_Collection.domain.models.Search;
import ch.FOW_Collection.presentation.explore.search.suggestions.SearchSuggestionsFragment.OnItemSelectedListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class SearchSuggestionsRecyclerViewAdapter
        extends RecyclerView.Adapter<SearchSuggestionsRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "SearchSuggestionsRecycl";

    private static final int VIEW_TYPE_NO_SEARCH_HEADER = 1;
    private static final int VIEW_TYPE_NO_SEARCH_ENTRY = 2;

    /*
     * LETZTE SUCHEN
     *  - term
     *  - term
     *  - term
     * BELIEBTE SUCHEN
     *  - term 1
     *  - term 10
     * */
    private final List<String> previousSearches = new ArrayList<>();
    private final List<String> popularSearches;
    private final OnItemSelectedListener listener;

    public SearchSuggestionsRecyclerViewAdapter(List<String> popularSearches, OnItemSelectedListener listener) {
        Log.i(TAG, "SearchSuggestionsRecyclerViewAdapter");
        this.popularSearches = popularSearches;
        this.listener = listener;
    }

    public void setPreviousSearches(List<Search> previousSearches) {
        Log.i(TAG, "setPreviousSearches, length " + previousSearches.size());
        this.previousSearches.clear();
        Set<Search> alreadyAdded = new HashSet<>();
        for (Search search : previousSearches) {
            if (alreadyAdded.contains(search)) {
                // don't add searches several times
            } else {
                alreadyAdded.add(search);
                this.previousSearches.add(search.getTerm());
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view;
        switch (viewType) {
            case VIEW_TYPE_NO_SEARCH_HEADER:
                view = layoutInflater.inflate(R.layout.fragment_searchsuggestion_header, parent, false);
                break;
            default:
                view = layoutInflater.inflate(R.layout.fragment_searchsuggestion_listentry, parent, false);
                break;
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        if (position == 0) {
            holder.bind("LETZTE SUCHEN", null);
        } else if (position - 1 == previousSearches.size()) {
            holder.bind("BELIEBTE SUCHEN", null);
        } else if (position <= previousSearches.size()) {
            String text = previousSearches.get(position - 1);
            holder.bind(text, listener);
        } else {
            String text = popularSearches.get(position - 2 - previousSearches.size());
            holder.bind(text, listener);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position - 1 == previousSearches.size()) {
            return VIEW_TYPE_NO_SEARCH_HEADER;
        } else {
            return VIEW_TYPE_NO_SEARCH_ENTRY;
        }
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "getItemCount " + (previousSearches.size() + popularSearches.size() + 2));
        return previousSearches.size() + popularSearches.size() + 2 /*headers*/;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.content)
        TextView content;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, itemView);
        }

        void bind(String text, OnItemSelectedListener listener) {
            content.setText(text);
            if (listener != null) {
                itemView.setOnClickListener(v -> listener.onSearchSuggestionListItemSelected(text));
            }
        }
    }
}
