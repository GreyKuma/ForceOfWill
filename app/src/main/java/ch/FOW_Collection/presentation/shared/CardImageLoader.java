package ch.FOW_Collection.presentation.shared;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ViewTarget;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.security.MessageDigest;

import androidx.annotation.NonNull;

public final class CardImageLoader {
    public static ViewTarget<ImageView, Drawable> loadImageIntoImageView(RequestManager glide, String imageStorageUrl, ImageView imageView) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(imageStorageUrl);
        return glide.load(storageReference)
                .apply(imageRenderer)
                .into(imageView);
    }

    public static final RequestOptions imageRenderer = new RequestOptions().transforms(new BitmapTransformation() {
        @NonNull
        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform,
        int outWidth, int outHeight) {
            // make image rounded like a card
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
            // what should we do here?
        }
    });
}
