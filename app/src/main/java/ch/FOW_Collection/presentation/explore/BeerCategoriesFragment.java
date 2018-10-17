package ch.FOW_Collection.presentation.explore;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.FOW_Collection.R;
import ch.FOW_Collection.presentation.MainViewModel;
import ch.FOW_Collection.presentation.utils.GridSpacingItemDecoration;


/**
 * The fragment, nested inside the {@link ExploreFragment}, which in turn is part of the
 * {@link ch.FOW_Collection.presentation.MainActivity} shows a two by N grid with beer categories.
 */
public class BeerCategoriesFragment extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    /**
     * The fragment needs to notify the {@link ch.FOW_Collection.presentation.MainActivity} when the user clicks on one of
     * the categories. This is done by capturing the attaching fragment (in the onAttach method below) and passing
     * the reference to the listener to the {@link BeerCategoriesRecyclerViewAdapter}.
     */
    private OnItemSelectedListener listener;

    public BeerCategoriesFragment() {
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
        if (context instanceof OnItemSelectedListener) {
            listener = (OnItemSelectedListener) context;
        } else {
            /*
             * The activity might have forgotten to implement the interface, so we kindly remind the developer:
             * */
            throw new RuntimeException(context.toString() + " must implement OnItemSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore_beer_categories, container, false);
        ButterKnife.bind(this, view);

        /*
         * We ususally use the RecyclerView for lists, but with a GridLayoutManager it can also display grids.
         * */
        LinearLayoutManager layoutManager = new GridLayoutManager(view.getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        /*
         * This recyclerview is nested inside the fragment which should take care of the scrolling, i.e., we don't
         * want nested scrolling behaviour so we can disabled that:
         */
        recyclerView.setNestedScrollingEnabled(false);

        /*
         * TODO Spacings should really be extracted into dimension resources.
         * */
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_layout_margin);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, false, 0));

        /*
         * The adapter registers the MainActivity (the listener) directly on the individual items of the grid, so
         * when the user clicks an item the activity will be notified, bypassing this fragment and its parent.
         *
         * Note that we don't have to pass any items or a collection to the adapter, this is done with
         * submitList below.
         */
        BeerCategoriesRecyclerViewAdapter adapter = new BeerCategoriesRecyclerViewAdapter(listener);

        /*
         * We get the same ViewModel as the MainActivity, and because the MainActivity is already running we get the
         * same instance and can share the data of the MainActivity.
         * */
        MainViewModel model = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

        /*
         * The RecyclerViewAdapter has a submitList method that allows us to submit the new data. So whenever the
         * beer cagetories LiveData changes, we will get notified and submit the new list to the adapter.
         * */
        model.getBeerCategories().observe(this, categories -> adapter.submitList(categories));

        recyclerView.setAdapter(adapter);

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

    /**
     * The {@link ch.FOW_Collection.presentation.MainActivity} needs to implement this interface so we can notify it when
     * the user has clicked on one of the entries in the grid.
     */
    public interface OnItemSelectedListener {
        void onBeerCategorySelected(String name);
    }
}
