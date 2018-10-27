package ch.FOW_Collection.presentation.shared.cardList;

import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;
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
        return new CardSimpleListFragmentViewAdapter(
                getContext(),
                this,
                listener.getData(cardListId),
                listener,
                cardListId);
    }
}
