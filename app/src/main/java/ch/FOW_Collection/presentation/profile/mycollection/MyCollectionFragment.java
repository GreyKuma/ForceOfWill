package ch.FOW_Collection.presentation.profile.mycollection;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.FOW_Collection.R;
import ch.FOW_Collection.domain.models.MyCard;
import ch.FOW_Collection.presentation.shared.ICollectionInteractionListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MyCollectionFragment extends Fragment {

    private static final String TAG = "MyCollectionFragment";

    private ICollectionInteractionListener interactionListener;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.emptyView)
    View emptyView;

    private MyCollectionRecyclerViewAdapter adapter;

    public MyCollectionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_searchresult_list, container, false);
        ButterKnife.bind(this, view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        MyCollectionViewModel model = ViewModelProviders.of(getActivity()).get(MyCollectionViewModel.class);

        //TODO Was here (not working)

        adapter = new MyCollectionRecyclerViewAdapter(getContext(), this, interactionListener, model);

        recyclerView.setAdapter(adapter);

        //TODO Is here now (working)
        model.getMyFilteredCards().observe(getActivity(), this::handleCollectionChanged);

        return view;
    }

    private void handleCollectionChanged(List<MyCard> cards) {
        List<String> myCardsIds = new ArrayList<>();
        Iterator<MyCard> it = cards.iterator();
        while(it.hasNext()) {
            myCardsIds.add(it.next().getId());
        }

        adapter.submitList(myCardsIds);
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
        if (context instanceof ICollectionInteractionListener) {
            interactionListener = (ICollectionInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement ICollectionInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        interactionListener = null;
    }
}
