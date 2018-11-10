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
import ch.FOW_Collection.presentation.MainViewModel;
import ch.FOW_Collection.presentation.profile.mycollection.MyCollectionViewModel;
import ch.FOW_Collection.presentation.shared.IWishClickedListener;
import ch.FOW_Collection.presentation.shared.viewHolder.CardBaseListentry;
import ch.FOW_Collection.presentation.shared.viewHolder.WishHandler;
import ch.FOW_Collection.presentation.utils.EntityDiffItemCallback;
import ch.FOW_Collection.presentation.utils.StringDiffItemCallback;
import com.google.common.base.Strings;


public class WishlistRecyclerViewAdapter extends ListAdapter<String, WishlistRecyclerViewAdapter.ViewHolder> {
    //extends ListAdapter<Pair<Wish, Card>, WishlistRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "WishlistRecyclerViewAda";

    //private static final DiffUtil.ItemCallback<Pair<Wish, Card>> DIFF_CALLBACK = new EntityPairDiffItemCallback<>();
    private static final DiffUtil.ItemCallback<String> DIFF_CALLBACK = new StringDiffItemCallback();

    private final Context context;
    private final LifecycleOwner lifecycleOwner;
    private final IWishClickedListener listener;
    private final MainViewModel viewModel;

    public WishlistRecyclerViewAdapter(WishlistActivity listener, MainViewModel viewModel) {
        this(listener, listener, listener, viewModel);
    }

    public WishlistRecyclerViewAdapter(ComponentActivity activity, IWishClickedListener listener, MainViewModel viewModel) {
        this(activity, activity, listener, viewModel);
    }

    public WishlistRecyclerViewAdapter(Context context, LifecycleOwner lifecycleOwner, IWishClickedListener listener, MainViewModel viewModel) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.lifecycleOwner = lifecycleOwner;
        this.listener = listener;
        this.viewModel = viewModel;
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
        String wishId = getItem(position);
        //Log.d(TAG, "item.wish = " + item.first.toString() + " item.card = " + item.second.toString());
        holder.bind(wishId, listener, lifecycleOwner, viewModel);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements WishHandler, CardBaseListentry {
        String currentWishId;

        ViewHolder(View view) {
            super(view);
        }

        void bind(String wishId, IWishClickedListener listener, LifecycleOwner lifecycleOwner, MainViewModel viewModel) {
            // clear observer, when already observed
            if (!Strings.isNullOrEmpty(wishId)) {
                if (!Strings.isNullOrEmpty(currentWishId)) {
                    viewModel.getWishById(currentWishId).removeObservers(lifecycleOwner);
                }

                View cardListentryBase = itemView.findViewById(R.id.cardListentryBase);
                View wishHandler = itemView.findViewById(R.id.wishlistHandler);

                viewModel.getWishById(currentWishId = wishId).observe(lifecycleOwner, wish -> {
                    wish.getCard().observe(lifecycleOwner, new Observer<Card>() {
                        @Override
                        public void onChanged(Card card) {
                            if (card != null) {
                                wish.getCard().removeObserver(this);

                                bindCardBaseListentry(cardListentryBase, card, listener, null);
                                bindWishHandler(wishHandler, wish, card, listener);
                            }
                        }
                    });
                });
            }
        }
    }
}
