package ch.FOW_Collection.presentation.explore;

import android.content.Context;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.firestore.Query;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.FOW_Collection.R;
import ch.FOW_Collection.domain.models.Card;
import ch.FOW_Collection.presentation.MainViewModel;
import ch.FOW_Collection.presentation.shared.cardList.CardListFragment;
import ch.FOW_Collection.presentation.shared.cardList.CardSimpleListFragment;
import ch.FOW_Collection.presentation.shared.cardList.ICardListFragmentListener;


/**
 * The fragment, nested inside the {@link ExploreFragment}, which in turn is part of the
 * {@link ch.FOW_Collection.presentation.MainActivity} shows a two by N grid with beer categories.
 */
public class CardPopularFragment extends Fragment {

    // @BindView(R.id.cardListFragment)
    // Fragment cardListFragment;

    @BindView(R.id.button)
    Button button;

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

        Transition transform = TransitionInflater
                .from(this.getContext()).
                inflateTransition(R.transition.change_view_transform);

        CardSimpleListFragment cardListFragment = CardSimpleListFragment.newInstance("popular", false);
        cardListFragment.setSharedElementReturnTransition(transform);
        cardListFragment.setExitTransition(transform);

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
        listener = null;
    }

    public interface ICardPopularFragmentListener {
        void onShowMoreClick(View view);
    }
}
