package ch.FOW_Collection.presentation.shared.cardList;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.security.MessageDigest;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.FOW_Collection.R;
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
        extends FirestoreRecyclerAdapter<Card, CardListViewHolder> {

    /**
     * The entries of the adapter need a callback listener to notify the {@link ch.FOW_Collection.presentation.MainActivity}
     * when an entry was clicked. This listener is passed from the {@link CardPopularFragment}.
     */
    private final ICardListFragmentListener listener;
    private final RequestManager glide;
    private final String cardListId;

    public CardSimpleListFragmentViewAdapter(
            String cardListId,
            RequestManager glide,
            FirestoreRecyclerOptions option,
            ICardListFragmentListener listener) {
        /*
         * Whenever a new list is submitted to the ListAdapter, it needs to compute the set of changes in the list.
         * for example, a new string might have been added to the front of the list. in that case, the ListAdapter
         * does not have to recreate all the items in the list but can just insert a new entry at the top of the list
         * and shift the rest down. Because the ListAdapter can work with all kinds of classes (here it's just
         * a String - the first type parameter passed to the superclass), it needs a way to diff the entry items.
         * This is implemented in the StringDiffItemCallback class.
         */
        ///super(new EntityDiffItemCallback<>());
        super(option);

        this.listener = listener;
        this.glide = glide;
        this.cardListId = cardListId;
    }

    /**
     * A recyclerview can display a heterogeneous list of items, i.e., not all entries need to use the same layout.
     * These different layouts can be distinguished by the viewType parameter. In this list though, all items have
     * the same layout so we can ignore the viewType and just create and return our
     * fragment_explore_beer_categories_card layout.
     * <p>
     * For an adapter that implements different viewTypes, see
     * {@link ch.FOW_Collection.presentation.explore.search.suggestions.SearchSuggestionsRecyclerViewAdapter}.
     *
     * @param parent   The parent of the layout, but we don't really do anything useful with it except getting to the
     *                 context (the activity) from it.
     * @param viewType Indicates the viewType, but as explained, we ignore that.
     * @return the {@link CardListViewHolder} instance for this kind of entry.
     */
    @NonNull
    @Override
    public CardListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.fragment_card, parent, false);
        return new CardListViewHolder(view);
    }

    /**
     * With the onCreateViewHolder method, we instantiated an empty layout and a ViewHolder for the adapter. Now we
     * also need to bind some data (our beer categories) to the list entries. The method will be called for each
     * entry in the list.
     *
     * @param holder   The ViewHolder instance we created in onCreateViewHolder.
     * @param position The position in the list we are currently drawing.
     */
    @Override
    protected void onBindViewHolder(@NonNull final CardListViewHolder holder, int position, @NonNull Card card) {
        /*
         * We just delegate all the work to the ViewHolder:
         */
        holder.bind(card, cardListId, listener, glide);
    }
}
