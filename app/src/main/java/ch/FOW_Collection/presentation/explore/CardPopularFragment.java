package ch.FOW_Collection.presentation.explore;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.FOW_Collection.R;
import ch.FOW_Collection.presentation.shared.cardList.CardSimpleListFragment;


/**
 * The fragment, nested inside the {@link ExploreFragment}, which in turn is part of the
 * {@link ch.FOW_Collection.presentation.MainActivity} shows a two by N grid with beer categories.
 */
public class CardPopularFragment extends Fragment {
    private final static String TAG = "CardPopularFragment";


    @BindView(R.id.button)
    Button button;

    CardSimpleListFragment cardListFragment;

    /**
     * The fragment needs to notify the {@link ch.FOW_Collection.presentation.MainActivity} when the user clicks on one of
     * the categories. This is done by capturing the attaching fragment (in the onAttach method below) and passing
     * the reference to the listener to the .
     */
    private ICardPopularFragmentListener listener;

    public CardPopularFragment() {
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
        Log.d(TAG, "onAttach: ");
        if (context instanceof ICardPopularFragmentListener) {
            listener = (ICardPopularFragmentListener) context;
        } else {
            /*
             * The activity might have forgotten to implement the interface, so we kindly remind the developer:
             * */
            throw new RuntimeException(context.toString() + " must implement ICardListFragmentListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore_card_populars, container, false);
        ButterKnife.bind(this, view);
        Log.d(TAG, "onCreateView: " + (savedInstanceState == null ? "NULL" : savedInstanceState.toString()));


        /*Transition transform = TransitionInflater
                .from(this.getContext()).
                inflateTransition(R.transition.change_view_transform);*/

        cardListFragment = CardSimpleListFragment.newInstance("popular", false);
        /*
        cardListFragment.setSharedElementReturnTransition(transform);
        cardListFragment.setExitTransition(transform);*/

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.cardListFragment, cardListFragment);
        //ft.addSharedElement(container, "image");
        ft.commit();

        button.setOnClickListener(this::onShowMoreClick);

        return view;
    }

    private void onShowMoreClick(View view) {
        listener.onShowMoreClick(view);
    }

    /**
     * When the fragment is destroyed or detached from the activity (in this app, this only happens when a new
     * activity is started), we reset the listener. Note that we don't have to do anything, the getBeerCategories
     * LiveData will automatically be unsubscribed.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach");
        listener = null;
    }

    public interface ICardPopularFragmentListener {
        void onShowMoreClick(View view);
    }
}
