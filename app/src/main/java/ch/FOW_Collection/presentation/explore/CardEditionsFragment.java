package ch.FOW_Collection.presentation.explore;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.FOW_Collection.R;
import ch.FOW_Collection.domain.models.CardEdition;
import ch.FOW_Collection.presentation.MainViewModel;
import ch.FOW_Collection.presentation.utils.GridSpacingItemDecoration;


/**
 * This class is really similar to {@link CardPopularFragment}, see the documentation there.
 */
public class CardEditionsFragment extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private OnItemSelectedListener mListener;

    public CardEditionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnItemSelectedListener) {
            mListener = (OnItemSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_explore_card_editions, container, false);
        ButterKnife.bind(this, view);

        LinearLayoutManager layoutManager = new GridLayoutManager(view.getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);


        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_layout_margin);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, false, 0));

        CardEditionsRecyclerViewAdapter adapter = new CardEditionsRecyclerViewAdapter(mListener);

        MainViewModel model = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        /// model.getBeerManufacturers().observe(this, adapter::submitList);
        model.getCardEditions().observe(this, adapter::submitList);

        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnItemSelectedListener {
        void onCardEditionSelected(CardEdition cardEdition);
    }
}
