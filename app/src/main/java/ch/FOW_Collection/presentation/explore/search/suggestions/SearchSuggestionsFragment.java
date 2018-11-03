package ch.FOW_Collection.presentation.explore.search.suggestions;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ch.FOW_Collection.R;
import ch.FOW_Collection.domain.models.Search;
import ch.FOW_Collection.presentation.explore.search.SearchViewModel;


public class SearchSuggestionsFragment extends Fragment {

    private static final String TAG = "SearchSuggestionsFragme";

    private OnItemSelectedListener listener;
    private SearchSuggestionsRecyclerViewAdapter adapter;

    public SearchSuggestionsFragment() {
    }

    private void updateMyLatestSearches(List<Search> searches) {
        if (searches.isEmpty()) {
            adapter.setPreviousSearches(Arrays.asList(new Search("", "-")));
        } else {
            adapter.setPreviousSearches(searches);
        }
    }

    @Override
    public void onAttach(Context context) {
        Log.i(TAG, "onAttach");
        super.onAttach(context);
        if (context instanceof OnItemSelectedListener) {
            listener = (OnItemSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnItemSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_searchsuggestion_list, container, false);

        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new DividerItemDecoration(context, layoutManager.getOrientation()));

        SearchViewModel model = ViewModelProviders.of(getActivity()).get(SearchViewModel.class);

        adapter = new SearchSuggestionsRecyclerViewAdapter(listener);

        recyclerView.setAdapter(adapter);

        // Aufgabe: Diesen Aufruf vor das erstellen eines neuen Adapters schieben und dann debuggen.
        model.getMyLatestSearches().observe(getActivity(), this::updateMyLatestSearches);

        model.getCardsTopRated(3).observe(this, adapter::setPopularSearches);

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnItemSelectedListener {
        void onSearchSuggestionListItemSelected(String text);
    }
}
