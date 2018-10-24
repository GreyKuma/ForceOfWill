package ch.FOW_Collection.presentation.shared.cardList;

import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import androidx.recyclerview.widget.RecyclerView;
import ch.FOW_Collection.GlideApp;
import ch.FOW_Collection.domain.models.Card;
import ch.FOW_Collection.presentation.explore.ExploreFragment;


/**
 * The fragment, nested inside the {@link ExploreFragment}, which in turn is part of the
 * {@link ch.FOW_Collection.presentation.MainActivity} shows a two by N grid with beer categories.
 */
public class CardSimpleListFragment extends CardListFragment {

    public static CardSimpleListFragment newInstance(String cardListId, boolean nestedScrolling) {
        Bundle args = new Bundle();

        args.putString(ARG_Id, cardListId);
        args.putBoolean(ARG_NestedScrolling, nestedScrolling);

        CardSimpleListFragment fragment = new CardSimpleListFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public RecyclerView.Adapter<CardListViewHolder> adapterFactory() {
        // The options for the adapter combine the paging configuration with query information
        // and application-specific options for lifecycle, etc.
        FirestoreRecyclerOptions<Card> options = new FirestoreRecyclerOptions.Builder<Card>()
                .setLifecycleOwner(this)
                .setQuery(listener.getData(cardListId), Card.class)
                .build();

        return new CardSimpleListFragmentViewAdapter(
                cardListId,
                GlideApp.with(getContext()),
                options,
                listener);
    }
}
