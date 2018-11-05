package ch.FOW_Collection.presentation.profile.mywishlist;

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
import ch.FOW_Collection.domain.models.Card;
import ch.FOW_Collection.domain.models.Wish;
import ch.FOW_Collection.presentation.shared.IWishClickedListener;
import ch.FOW_Collection.presentation.shared.viewHolder.CardBaseListentry;
import ch.FOW_Collection.presentation.shared.viewHolder.WishHandler;
import ch.FOW_Collection.presentation.utils.EntityDiffItemCallback;


public class WishlistRecyclerViewAdapter extends ListAdapter<Wish, WishlistRecyclerViewAdapter.ViewHolder> {
    //extends ListAdapter<Pair<Wish, Card>, WishlistRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "WishlistRecyclerViewAda";

    //private static final DiffUtil.ItemCallback<Pair<Wish, Card>> DIFF_CALLBACK = new EntityPairDiffItemCallback<>();
    private static final DiffUtil.ItemCallback<Wish> DIFF_CALLBACK = new EntityDiffItemCallback<>();

    private final Context context;
    private final LifecycleOwner lifecycleOwner;
    private final IWishClickedListener listener;

    public WishlistRecyclerViewAdapter(WishlistActivity listener) {
        this(listener, listener, listener);
    }

    public WishlistRecyclerViewAdapter(ComponentActivity activity, IWishClickedListener listener) {
        this(activity, activity, listener);
    }

    public WishlistRecyclerViewAdapter(Context context, LifecycleOwner lifecycleOwner, IWishClickedListener listener) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.lifecycleOwner = lifecycleOwner;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.fragment_my_wishlist_listentry, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        //Pair<Wish, Card> item = getItem(position);
        Wish wish = getItem(position);
        //Log.d(TAG, "item.wish = " + item.first.toString() + " item.card = " + item.second.toString());
        holder.bind(wish, listener);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements WishHandler, CardBaseListentry {

        ViewHolder(View view) {
            super(view);
        }

        void bind(Wish wish, IWishClickedListener listener) {
            wish.getCard().observe(lifecycleOwner, new Observer<Card>() {
                @Override
                public void onChanged(Card card) {
                    if (card != null) {
                        View cardListentryBase = itemView.findViewById(R.id.cardListentryBase);
                        View wishHandler = itemView.findViewById(R.id.wishlistHandler);

                        bindCardBaseListentry(cardListentryBase, card, listener, null);
                        bindWishHandler(wishHandler, wish, card, listener);
                    }
                }
            });
        }

    }
}
