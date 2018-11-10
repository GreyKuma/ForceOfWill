package ch.FOW_Collection.presentation.shared.cardList;

import android.content.Context;
import androidx.core.app.ComponentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import ch.FOW_Collection.domain.liveData.FirestoreQueryLiveDataArray;
import ch.FOW_Collection.domain.models.Card;
import ch.FOW_Collection.presentation.explore.CardPopularFragment;

/**
 * This adapter is responsible for displaying the different beer categories (Lager, Pale Ale, etc) in a grid (see
 * {@link CardPopularFragment}). It extends a ListAdapter, a convenient subclass of the
 * {@link RecyclerView.Adapter} class that is useful whenever the data you're displaying is held in a list. This is
 * almost always the case so you will typically want to extend {@link ListAdapter}.
 * <p>
 * The {@link ListAdapter} is parametrized by the type of the list it contains. Here it's just strings, but it can be
 * any other class as well.
 * <p>
 * The second parameter is the {@link CardListViewHolder}, which is ususally implemented as a nested class of the adapter.
 */
public class CardSimpleListFragmentViewAdapter
        /// extends ListAdapter<Card, CardPopularRecyclerViewAdapter.ViewHolder> {
        extends CardListFragmentViewAdapter {

    private static final String TAG = "CardSimpleListFragment";

    public CardSimpleListFragmentViewAdapter(ComponentActivity activity, FirestoreQueryLiveDataArray<Card> liveData, ICardListFragmentListener listener, String cardListId) {
        super(activity, liveData, listener, cardListId);
    }

    public CardSimpleListFragmentViewAdapter(Context context, LifecycleOwner lifecycleOwner, FirestoreQueryLiveDataArray<Card> liveData, ICardListFragmentListener listener, String cardListId) {
        super(context, lifecycleOwner, liveData, listener, cardListId);
    }
}
