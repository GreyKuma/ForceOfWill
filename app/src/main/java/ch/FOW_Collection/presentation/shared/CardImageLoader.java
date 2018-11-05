package ch.FOW_Collection.presentation.shared;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import ch.FOW_Collection.GlideApp;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ViewTarget;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.OutputStream;
import java.security.MessageDigest;

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
            int corner = (int) Math.floor(Math.max(toTransform.getWidth(), toTransform.getHeight()) * 0.05);
            canvas.drawRoundRect((new RectF(0, 0, toTransform.getWidth(), toTransform.getHeight())), corner, corner, mpaint);// Round Image Corner 100 100 100 100
            return imageRounded;
        }

        @Override
        public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
            // what should we do here?
        }
    });

    public static void openImageExternStorage(Context context, String imageStorageUrl) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(imageStorageUrl);
        GlideApp.with(context).load(storageReference)
                .apply(new RequestOptions().transforms(new BitmapTransformation() {

                    @Override
                    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

                    }

                    @Override
                    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
                        CardImageLoader.openImageExtern(context, toTransform);
                        return toTransform;
                    }
                })).submit();
    }

    public static void openImageExtern(Context context, String imageStorageUrl) {
        GlideApp.with(context).load(imageStorageUrl)
                .apply(new RequestOptions().transforms(new BitmapTransformation() {

                    @Override
                    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

                    }

                    @Override
                    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
                        CardImageLoader.openImageExtern(context, toTransform);
                        return toTransform;
                    }
                })).submit();
    }

    public static void openImageExtern(Context context, ImageView imageView) {
        Bitmap bm = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        CardImageLoader.openImageExtern(context, bm);
    }

    public static void openImageExtern(Context context, Bitmap icon) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/*");
        Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        OutputStream outstream;
        try {
            outstream = context.getContentResolver().openOutputStream(uri);
            icon.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
            outstream.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("mimeType", "image/*");
        context.startActivity(Intent.createChooser(intent, "Öffnen mit:"));
    }
}
