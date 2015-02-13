package com.example.recyclerviewfastscroller.ui.scroller.vertical;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.example.android.recyclerview.R;
import com.example.recyclerviewfastscroller.ui.scroller.AbsRecyclerViewFastScroller;
import com.example.recyclerviewfastscroller.ui.scroller.RecyclerViewScroller;
import com.example.recyclerviewfastscroller.ui.scroller.progresscalculation.LinearLayoutManagerScrollProgressCalculator;
import com.example.recyclerviewfastscroller.ui.scroller.progresscalculation.ScrollProgressCalculator;

/**
 * Widget used to scroll a {@link RecyclerView}
 */
public class VerticalRecyclerViewFastScroller extends AbsRecyclerViewFastScroller implements RecyclerViewScroller {

    private static final float UNINITIALIZED = -999;
    private float mMinimumScrollY = UNINITIALIZED;
    private float mMaximumScrollY = UNINITIALIZED;

    public VerticalRecyclerViewFastScroller(Context context) {
        this(context, null);
    }

    public VerticalRecyclerViewFastScroller(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalRecyclerViewFastScroller(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void moveHandleToPosition(float scrollProgress) {
        float handleY = scrollProgress * getMaximumScrollY();
        mHandle.setY(Math.max(getMinimumScrollY(), Math.min(handleY, getMaximumScrollY())));
    }

    @Override
    public float convertTouchEventToScrollProgress(MotionEvent event) {
        float y = event.getY();

        if (y <= getMinimumScrollY()) {
            return 0;
        } else if (y >= getMaximumScrollY()) {
            return 1;
        } else {
            return y / getMaximumScrollY();
        }
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.vertical_recycler_fast_scroller_layout;
    }

    @Override
    protected ScrollProgressCalculator getScrollProgressCalculator() {
        if (mScrollProgressCalculator == null) {
            mScrollProgressCalculator = new LinearLayoutManagerScrollProgressCalculator();
        }
        return mScrollProgressCalculator;
    }

    private float getMinimumScrollY() {
        if (mMinimumScrollY == UNINITIALIZED) {
            mMinimumScrollY = mBar.getY();
        }
        return mMinimumScrollY;
    }

    private float getMaximumScrollY() {
        if (mMaximumScrollY == UNINITIALIZED) {
            mMaximumScrollY = mBar.getY() + mBar.getHeight() - mHandle.getHeight();
        }
        return mMaximumScrollY;
    }

}
