package ch.FOW_Collection.presentation.explore.search.cards;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.FOW_Collection.R;
import ch.FOW_Collection.domain.models.Card;
import ch.FOW_Collection.presentation.explore.search.SearchViewModel;
import ch.FOW_Collection.presentation.shared.ICardSelectedListener;

public class SearchResultFragment extends Fragment {

    private static final String TAG = "SearchResultFragment";

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.emptyView)
    View emptyView;

    private ICardSelectedListener mListener;
    private SearchResultRecyclerViewAdapter adapter;

    public SearchResultFragment() {
    }

    private void handleCardsChanged(List<Card> cards) {
        adapter.submitList(new ArrayList<>(cards));
        if (cards.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ICardSelectedListener) {
            mListener = (ICardSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement ICardSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_searchresult_list, container, false);
        ButterKnife.bind(this, view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        SearchViewModel model = ViewModelProviders.of(getActivity()).get(SearchViewModel.class);

        adapter = new SearchResultRecyclerViewAdapter(mListener, model.getSearchTerm());

        model.getFilteredCards().observe(getActivity() , this::handleCardsChanged);

        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnItemSelectedListener {
        void onSearchResultListItemSelected(View animationSource, Card card);
    }
}
