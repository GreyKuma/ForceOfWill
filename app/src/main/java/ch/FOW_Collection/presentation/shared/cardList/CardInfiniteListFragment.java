package ch.FOW_Collection.presentation.shared.cardList;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import ch.FOW_Collection.GlideApp;
import ch.FOW_Collection.R;
import ch.FOW_Collection.domain.models.Card;
import ch.FOW_Collection.presentation.explore.ExploreFragment;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;


/**
 * The fragment, nested inside the {@link ExploreFragment}, which in turn is part of the
 * {@link ch.FOW_Collection.presentation.MainActivity} shows a two by N grid with beer categories.
 */
public class CardInfiniteListFragment extends CardListFragment {
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    public static CardInfiniteListFragment newInstance(
            String cardListId,
            boolean nestedScrolling,
            int pageSize,
            int loadSizeHint,
            int prefetchDistance) {
        Bundle args = new Bundle();

        args.putString(ARG_Id, cardListId);
        args.putBoolean(ARG_NestedScrolling, nestedScrolling);

        CardInfiniteListFragment fragment = new CardInfiniteListFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public RecyclerView.Adapter<CardListViewHolder> adapterFactory() {
        // This configuration comes from the Paging Support Library
        // https://developer.android.com/reference/android/arch/paging/PagedList.Config.html
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(6)
                .setPrefetchDistance(12)
                .setPageSize(6)
                .build();

        // The options for the adapter combine the paging configuration with query information
        // and application-specific options for lifecycle, etc.
        FirestorePagingOptions<Card> options = new FirestorePagingOptions.Builder<Card>()
                .setLifecycleOwner(this)

                //.setQuery(listener.getData(cardListId), config, new CardClassSnapshotParser())
                .build();

        CardInfiniteListFragmentViewAdapter adapter = new CardInfiniteListFragmentViewAdapter(
                cardListId,
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

        return adapter;
    }
}
