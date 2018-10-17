package ch.FOW_Collection.presentation.utils;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.widget.Button;

public class DrawableHelpers {


    public static void setDrawableTint(Button button, int color) {
        for (Drawable drawable : button.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            }
        }
    }
}
