package ch.FOW_Collection.presentation.shared;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.viewpager.widget.ViewPager;
/*
 * A ViewPager with working "wrap_content".
 * To use it, add "class" in xml:
 * <code>
 *  <view
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        class="ch.FOW_Collection.presentation.shared.WrapContentHeightViewPager"
        android:id="@+id/wrapContentHeightViewPager" />
 * </code>
 * source: https://pristalovpavel.wordpress.com/2014/12/26/doing-it-right-vertical-scrollview-with-viewpager-and-listview/
 */
public class WrapContentHeightViewPager extends ViewPager
{
    public WrapContentHeightViewPager(Context context)
    {
        super(context);
    }

    public WrapContentHeightViewPager(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = 0;
        for(int i = 0; i < getChildCount(); i++)
        {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

            int h = child.getMeasuredHeight();
            if(h > height) height = h;
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}