package ch.FOW_Collection.presentation.explore;

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
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
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

/**
 * This adapter is responsible for displaying the different beer categories (Lager, Pale Ale, etc) in a grid (see
 * {@link CardPopularFragment}). It extends a ListAdapter, a convenient subclass of the
 * {@link RecyclerView.Adapter} class that is useful whenever the data you're displaying is held in a list. This is
 * almost always the case so you will typically want to extend {@link ListAdapter}.
 * <p>
 * The {@link ListAdapter} is parametrized by the type of the list it contains. Here it's just strings, but it can be
 * any other class as well.
 * <p>
 * The second parameter is the {@link ViewHolder}, which is ususally implemented as a nested class of the adapter.
 */
public class CardPopularRecyclerViewAdapter
        /// extends ListAdapter<Card, CardPopularRecyclerViewAdapter.ViewHolder> {
        extends FirestorePagingAdapter<Card, CardPopularRecyclerViewAdapter.ViewHolder> {

    /**
     * The entries of the adapter need a callback listener to notify the {@link ch.FOW_Collection.presentation.MainActivity}
     * when an entry was clicked. This listener is passed from the {@link CardPopularFragment}.
     */
    private final CardPopularFragment.OnItemSelectedListener listener;

    private final RequestManager glide;
    public CardPopularRecyclerViewAdapter(
            RequestManager glide,
            FirestorePagingOptions option,
            CardPopularFragment.OnItemSelectedListener listener) {
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
     * @return the {@link ViewHolder} instance for this kind of entry.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.fragment_card, parent, false);
        return new ViewHolder(view);
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
    protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull Card card) {
        /*
         * We just delegate all the work to the ViewHolder:
         */
        holder.bind(card, listener);
    }

    /**
     * The ViewHolder class holds a reference to the layout that we instantiated and takes care of setting all the
     * values in the layout. So we see the now familiar pattern of {@link BindView} calls to get to the view elements
     * followed by a lot of setter calls.
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.content)
        TextView content;

        @BindView(R.id.ratingBar)
        RatingBar ratingBar;

        @BindView(R.id.imageView)
        ImageView imageView;

        ViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, itemView);
        }

        /**
         * The entries in the list are rather simple so there's not that much data to bind to the view elements. The
         * categories don't really have a background image assigned so we just get one from a helper class.
         * And finally, we register the listener that we have passed through so many layer at the itemView. The
         * itemView is the whole layout, meaning that the user can click anywhere on the list item to invoke the
         * callback. It's always a good idea to make these touch targets as large as possible. As an experiment, try
         * binding the callback to the content instead and see how much harder it will be to interact with the list
         * item.
         */
        void bind(Card item, CardPopularFragment.OnItemSelectedListener listener) {
            content.setText(item.getName().getDe());
            ratingBar.setRating(item.getAvgRating());

            if (listener != null) {
                itemView.setOnClickListener(v -> listener.onCardSelected(item));
            }

            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(item.getImageStorageUrl());
            glide.load(storageReference)
                    .apply(new RequestOptions().transforms(new BitmapTransformation() {
                        @NonNull
                        @Override
                        protected Bitmap transform(BitmapPool pool, Bitmap toTransform,
                                                   int outWidth, int outHeight) {
                            Bitmap imageRounded = Bitmap.createBitmap(toTransform.getWidth(), toTransform.getHeight(), toTransform.getConfig());

                            Canvas canvas = new Canvas(imageRounded);
                            Paint mpaint = new Paint();
                            mpaint.setAntiAlias(true);
                            mpaint.setShader(new BitmapShader(toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                            int corner = (int)Math.floor(Math.max(toTransform.getWidth(), toTransform.getHeight()) * 0.05);
                            canvas.drawRoundRect((new RectF(0, 0, toTransform.getWidth(), toTransform.getHeight())), corner, corner, mpaint);// Round Image Corner 100 100 100 100
                            return imageRounded;
                        }

                        @Override
                        public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

                        }
                    }))
                    .into(imageView);
                    /*.listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    // do a CROP_TOP
                    final Matrix matrix = imageView.getImageMatrix();
                    final float imageWidth = resource.getIntrinsicWidth();
                    final int screenWidth = itemView.getMeasuredWidth();
                    final float scaleRatio = screenWidth / imageWidth;
                    matrix.postScale(scaleRatio, scaleRatio);
                    imageView.setImageMatrix(matrix);

                    return false;
                }
            }).into(imageView);*/
        }
    }
}
