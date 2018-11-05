package ch.FOW_Collection.presentation.profile.mycollection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.app.ComponentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import ch.FOW_Collection.R;
import ch.FOW_Collection.domain.models.*;

import ch.FOW_Collection.presentation.shared.ICollectionInteractionListener;
import ch.FOW_Collection.presentation.shared.viewHolder.CardBaseListentry;
import ch.FOW_Collection.presentation.shared.viewHolder.CollectionHandler;
import ch.FOW_Collection.presentation.utils.StringDiffItemCallback;
import com.google.common.base.Strings;


public class MyCollectionRecyclerViewAdapter extends ListAdapter<String, MyCollectionRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "CollectionRecyclerViewAdapter";

    private static final DiffUtil.ItemCallback<String> DIFF_CALLBACK = new StringDiffItemCallback();

    private final Context context;
    private final LifecycleOwner lifecycleOwner;
    private final ICollectionInteractionListener listener;
    private final MyCollectionViewModel viewModel;

    public MyCollectionRecyclerViewAdapter(MyCollectionActivity listener, MyCollectionViewModel viewModel) {
        this(listener, listener, listener, viewModel);
    }

    public MyCollectionRecyclerViewAdapter(ComponentActivity activity, ICollectionInteractionListener listener, MyCollectionViewModel viewModel) {
        this(activity, activity, listener, viewModel);
    }

    public MyCollectionRecyclerViewAdapter(Context context, LifecycleOwner lifecycleOwner, ICollectionInteractionListener listener, MyCollectionViewModel viewModel) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.lifecycleOwner = lifecycleOwner;
        this.listener = listener;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public MyCollectionRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        //View view = layoutInflater.inflate(R.layout.fragment_my_wishlist_listentry, parent, false);
        View view = layoutInflater.inflate(R.layout.fragment_my_collection_listentry, parent, false);

        return new MyCollectionRecyclerViewAdapter.ViewHolder(view);
    }


    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public void onBindViewHolder(@NonNull final MyCollectionRecyclerViewAdapter.ViewHolder holder, int position) {
        String myCardId = getItem(position);
        //Log.d(TAG, "item.wish = " + item.first.toString() + " item.card = " + item.second.toString());
        holder.bind(myCardId, listener, lifecycleOwner, viewModel);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements CollectionHandler, CardBaseListentry {
        String currentMyCardId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        void bind(String myCardId, ICollectionInteractionListener listener, LifecycleOwner lifecycleOwner, MyCollectionViewModel viewModel) {
            // clear observer, when already observed
            if (!Strings.isNullOrEmpty(currentMyCardId)) {
                viewModel.getMyCardById(currentMyCardId).removeObservers(lifecycleOwner);
            }

            View cardBase = itemView.findViewById(R.id.cardListentryBase);
            View collectionHandler = itemView.findViewById(R.id.myCollectionHandler);

            // observe
            viewModel.getMyCardById(currentMyCardId = myCardId).observe(lifecycleOwner, myCard -> {
                bindCollectionHandler(collectionHandler, myCard, listener);

                myCard.getCard().observe(lifecycleOwner, new Observer<Card>() {
                    @Override
                    public void onChanged(Card card) {
                        if (card != null) {
                            myCard.getCard().removeObserver(this);
                            bindCardBaseListentry(cardBase, card, listener, null);
                        }
                    }
                });
            });
        }
    }
}
