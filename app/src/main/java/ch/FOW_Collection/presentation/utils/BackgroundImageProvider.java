package ch.FOW_Collection.presentation.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import ch.FOW_Collection.R;

import java.util.Arrays;

public class BackgroundImageProvider {

    static int[] backgrounds = {R.drawable.fow_logo, R.drawable.fow_logo, R.drawable.fow_logo,
            R.drawable.fow_logo, R.drawable.fow_logo, R.drawable.fow_logo,
            R.drawable.fow_logo, R.drawable.fow_logo, R.drawable.fow_logo,
            R.drawable.fow_logo, R.drawable.fow_logo, R.drawable.fow_logo,
            R.drawable.fow_logo, R.drawable.fow_logo};

    static {
        Arrays.sort(backgrounds);
    }

    public static Drawable getBackgroundImage(Context res, int position) {
        return res.getDrawable(backgrounds[position % backgrounds.length]);
    }
}
