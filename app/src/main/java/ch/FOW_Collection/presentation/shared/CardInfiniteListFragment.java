package ch.FOW_Collection.presentation.shared;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.firebase.firestore.Query;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.FOW_Collection.GlideApp;
import ch.FOW_Collection.R;
import ch.FOW_Collection.domain.models.Card;
import ch.FOW_Collection.presentation.MainViewModel;
import ch.FOW_Collection.presentation.explore.CardPopularRecyclerViewAdapter;
import ch.FOW_Collection.presentation.explore.ExploreFragment;
import ch.FOW_Collection.presentation.utils.GridAutofitLayoutManager;
import ch.FOW_Collection.presentation.utils.GridSpacingItemDecoration;


/**
 * The fragment, nested inside the {@link ExploreFragment}, which in turn is part of the
 * {@link ch.FOW_Collection.presentation.MainActivity} shows a two by N grid with beer categories.
 */
public class CardInfiniteListFragment extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    /**
     * The fragment needs to notify the {@link ch.FOW_Collection.presentation.MainActivity} when the user clicks on one of
     * the categories. This is done by capturing the attaching fragment (in the onAttach method below) and passing
     * the reference to the listener to the {@link CardPopularRecyclerViewAdapter}.
     */
    private CardListFragmentListener listener;

    public CardInfiniteListFragment() {
    }

    /**
     * When the {@link ch.FOW_Collection.presentation.MainActivity} displays this fragment, it "attaches" the fragment and
     * calls this lifecycle method.
     *
     * @param context the activity that attached this fragment. Context is a superclass of Activity.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CardListFragmentListener) {
            listener = (CardListFragmentListener) context;
        } else {
            /*
             * The activity might have forgotten to implement the interface, so we kindly remind the developer:
             * */
            throw new RuntimeException(context.toString() + " must implement OnItemSelectedListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //((CardPopularRecyclerViewAdapter)recyclerView.getAdapter()).notify();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_list, container, false);
        ButterKnife.bind(this, view);

        /*
         * We ususally use the RecyclerView for lists, but with a GridLayoutManager it can also display grids.
         * The GridAutofitLayoutManager also calcs it columnsCount by the given width (in pixel!),
         * so we don't have to care about it even if display gets rotated.
         * */
        // int px = Math.round(92 * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        // then we create layoutManager and set it
        GridLayoutManager layoutManager = new GridAutofitLayoutManager(view.getContext(), getResources().getDimensionPixelSize(R.dimen.fragment_card_width)); // new GridLayoutManager(view.getContext(), 3);
        recyclerView.setLayoutManager(layoutManager);

        /*
         * This recyclerview is nested inside the fragment which should take care of the scrolling, i.e., we don't
         * want nested scrolling behaviour so we can disabled that:
         */
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(layoutManager.getSpanCount(), getResources().getDimensionPixelSize(R.dimen.fragment_card_padding), false, 0));

        /*
         * We get the same ViewModel as the MainActivity, and because the MainActivity is already running we get the
         * same instance and can share the data of the MainActivity.
         * */
        MainViewModel model = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

        /*
         * The adapter registers the MainActivity (the listener) directly on the individual items of the grid, so
         * when the user clicks an item the activity will be notified, bypassing this fragment and its parent.
         *
         * We pass a query to the collection to the adapter, so it can handle loading by itself.
         */
        // This configuration comes from the Paging Support Library
        // https://developer.android.com/reference/android/arch/paging/PagedList.Config.html
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(20)
                .setPrefetchDistance(10)
                .setPageSize(10)
                .build();

        // The options for the adapter combine the paging configuration with query information
        // and application-specific options for lifecycle, etc.
        FirestorePagingOptions<Card> options = new FirestorePagingOptions.Builder<Card>()
                .setLifecycleOwner(this)
                .setQuery(getQuery().limit(10), config, Card.class)
                .build();

        CardInfiniteListFragmentViewAdapter adapter = new CardInfiniteListFragmentViewAdapter(
                GlideApp.with(getContext()),
                options,
                listener) {
            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                switch (state) {
                    case LOADING_INITIAL:
                    case LOADING_MORE:
                        progressBar.setVisibility(View.VISIBLE);
                        break;
                    case LOADED:
                    case ERROR:
                        progressBar.setVisibility(View.GONE);
                        break;
                }
            }
        };

        /*
         * Now we register the scrollListener
         * */
        /// model.getCardsTopRated(12).observe(this, categories -> adapter.submitList(categories));

        recyclerView.setAdapter(adapter);

        return view;
    }

    public Query getQuery() {
        return listener.getQuery();
    }

    public void onCardSelected(Card card){
        listener.onCardSelected(card);
    }

    /**
     * When the fragment is destroyed or detached from the activity (in this app, this only happens when a new
     * activity is started), we reset the listener. Note that we don't have to do anything, the getBeerCategories
     * LiveData will automatically be unsubscribed.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }


    public interface CardListFragmentListener extends OnCardSelectedListener {
        Query getQuery();
    }
}
