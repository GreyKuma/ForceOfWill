package ch.FOW_Collection.presentation.shared.cardList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ComponentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import ch.FOW_Collection.GlideApp;
import ch.FOW_Collection.R;
import ch.FOW_Collection.domain.liveData.FirestoreQueryLiveDataArray;
import ch.FOW_Collection.domain.models.Card;

public abstract class CardListFragmentViewAdapter
        extends RecyclerView.Adapter<CardListViewHolder> {
    private final static String TAG = "CardListFragmentViewAda";

    private final ICardListFragmentListener listener;
    private final LifecycleOwner lifecycleOwner;
    private final Context context;
    private final String cardListId;
    private final FirestoreQueryLiveDataArray<Card> liveData;

    public CardListFragmentViewAdapter(
            ComponentActivity activity,
            FirestoreQueryLiveDataArray<Card> liveData,
            ICardListFragmentListener listener,
            String cardListId){
        this(activity, activity, liveData, listener, cardListId);
    }

    public CardListFragmentViewAdapter(
            Context context,
            LifecycleOwner lifecycleOwner,
            FirestoreQueryLiveDataArray<Card> liveData,
            ICardListFragmentListener listener,
            String cardListId) {
        /*
         * Whenever a new list is submitted to the ListAdapter, it needs to compute the set of changes in the list.
         * for example, a new string might have been added to the front of the list. in that case, the ListAdapter
         * does not have to recreate all the items in the list but can just insert a new entry at the top of the list
         * and shift the rest down. Because the ListAdapter can work with all kinds of classes (here it's just
         * a String - the first type parameter passed to the superclass), it needs a way to diff the entry items.
         * This is implemented in the StringDiffItemCallback class.
         */
        super();

        // save values
        this.context = context;
        this.lifecycleOwner = lifecycleOwner;
        this.listener = listener;
        this.liveData = liveData;
        this.cardListId = cardListId;

        // observe
        // Gets killed when onStop even not destroyed!!! Kills the whole list and have to reload...
        //liveData.observe(this.lifecycleOwner, this::onDataChanged);
        liveData.observeForever(this::onDataChanged);

        Log.d(TAG, "Init");
    }

    @NonNull
    @Override
    public CardListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.fragment_card, parent, false);
        CardListViewHolder viewHolder = new CardListViewHolder(view);
        Log.d(TAG, "onCreateViewHolder");
        return viewHolder;
    }

    public Card getItem(int position) {
        return this.liveData.getValue() != null ? this.liveData.getValue().get(position) : null;
    }

    @Override
    public void onBindViewHolder(@NonNull CardListViewHolder holder, int position) {
        onBindViewHolder(holder, position, getItem(position));
    }

    public void onBindViewHolder(@NonNull CardListViewHolder holder, int position, Card card) {
        /*
         * We just delegate all the work to the ViewHolder:
         */
        holder.bind(card, listener, GlideApp.with(this.context));
        Log.d(TAG, "Bind \"" + card.getId() + "\"");
    }

    @Override
    public int getItemCount() {
        return liveData.getValue() != null ? liveData.getValue().size() : 0;
    }

    public void onDataChanged(List<Card> newData) {
        // todo test data, what changed and notify the right way
        notifyDataSetChanged();
    }
}
