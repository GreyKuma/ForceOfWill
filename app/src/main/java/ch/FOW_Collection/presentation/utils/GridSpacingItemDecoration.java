package ch.FOW_Collection.presentation.utils;

import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import ch.FOW_Collection.presentation.shared.GridAutofitLayoutManager;

/**
 * Created by Android Studio
 * User: Ailurus(ailurus@foxmail.com)
 * Date: 2015-10-28
 * Time: 15:20
 * Source: https://gist.github.com/liangzhitao/e57df3c3232ee446d464
 */
public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private int spanCount;

    public int getSpanCount() {
        return this.spanCount;
    }

    public void setSpanCount(int spanCount) {
        this.spanCount = spanCount;
    }

    private int spacing;
    private boolean includeEdge;
    private int headerNum;

    public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge, int headerNum) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
        this.headerNum = headerNum;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view) - headerNum; // item position

        // get spanCount from GridAutofitLayoutManager if present
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (GridAutofitLayoutManager.class.equals(layoutManager.getClass())) {
            spanCount = ((GridAutofitLayoutManager) layoutManager).getSpanCount();
        }

        if (position >= 0) {
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left =
                        spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing -
                        (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        } else {
            outRect.left = 0;
            outRect.right = 0;
            outRect.top = 0;
            outRect.bottom = 0;
        }
    }
}
