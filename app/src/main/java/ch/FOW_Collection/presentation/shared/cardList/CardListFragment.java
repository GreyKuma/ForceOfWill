package ch.FOW_Collection.presentation.shared.cardList;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.FOW_Collection.R;
import ch.FOW_Collection.presentation.explore.ExploreFragment;
import ch.FOW_Collection.presentation.utils.GridAutofitLayoutManager;


/**
 * The fragment, nested inside the {@link ExploreFragment}, which in turn is part of the
 * {@link ch.FOW_Collection.presentation.MainActivity} shows a two by N grid with beer categories.
 */
public abstract class CardListFragment extends Fragment {
    protected static final String ARG_Id = "cardListId";
    protected static final String ARG_NestedScrolling = "nestedScrolling";

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    /**
     * The fragment needs to notify the owner when the user clicks on one of
     * the categories. This is done by capturing the attaching fragment (in the onAttach method below) and passing
     * the reference to the listener to the {@link RecyclerView.Adapter<CardListViewHolder>}.
     */
    protected ICardListFragmentListener listener;

    protected String cardListId;
    public String getCardListId() { return this.cardListId; }

    protected boolean nestedScrolling;
    public boolean getNestedScrolling() {
        return nestedScrolling;
    }

    public CardListFragment() {
    }

    public abstract RecyclerView.Adapter<CardListViewHolder> adapterFactory();

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        if (args != null) {
            this.cardListId = args.getString(ARG_Id);
            this.nestedScrolling = args.getBoolean(ARG_NestedScrolling);
        }
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
        if (context instanceof ICardListFragmentListener) {
            listener = (ICardListFragmentListener) context;
        } else {
            /*
             * The activity might have forgotten to implement the interface, so we kindly remind the developer:
             * */
            throw new RuntimeException(context.toString() + " must implement CardListFragmentListener");
        }
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
        GridLayoutManager layoutManager = new GridAutofitLayoutManager(view.getContext(), getResources().getDimensionPixelSize(R.dimen.fragment_card_width));
        recyclerView.setLayoutManager(layoutManager);

        /*
         * This recyclerview is nested inside the fragment which should take care of the scrolling, i.e., we don't
         * want nested scrolling behaviour so we can disabled that:
         */
        recyclerView.setNestedScrollingEnabled(nestedScrolling);
        //recyclerView.addItemDecoration(new GridSpacingItemDecoration(layoutManager.getSpanCount(), getResources().getDimensionPixelSize(R.dimen.fragment_card_padding), false, 0));

        recyclerView.setAdapter(adapterFactory());

        return view;
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

    private final static String KEY_RECYCLER_STATE = "recyclerViewState";
    private Bundle mBundleRecyclerViewState;

    @Override
    public void onPause() {
        super.onPause();

        // save RecyclerView state
        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
    }

    @Override
    public void onResume()
    {
        super.onResume();

        // restore RecyclerView state
        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            recyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
    }
}
