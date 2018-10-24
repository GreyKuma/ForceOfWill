package ch.FOW_Collection.presentation.shared.cardList;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.security.MessageDigest;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import ch.FOW_Collection.R;
import ch.FOW_Collection.domain.models.Card;


/**
 * The ViewHolder class holds a reference to the layout that we instantiated and takes care of setting all the
 * values in the layout. So we see the now familiar pattern of {@link BindView} calls to get to the view elements
 * followed by a lot of setter calls.
 */
public class CardListViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.content)
    TextView content;

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    @BindView(R.id.imageView)
    ImageView imageView;

    CardListViewHolder(View view) {
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
    void bind(Card item, String cardListId, ICardListFragmentListener listener, RequestManager glide) {
        content.setText(item.getName().getDe());
        ratingBar.setRating(item.getAvgRating());

        if (listener != null) {
            itemView.setOnClickListener(v -> listener.onCardSelected(cardListId, item));
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