package com.example.recyclerviewfastscroller.ui.scroller.vertical;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.example.android.recyclerview.R;
import com.example.recyclerviewfastscroller.ui.scroller.AbsRecyclerViewFastScroller;
import com.example.recyclerviewfastscroller.ui.scroller.RecyclerViewScroller;
import com.example.recyclerviewfastscroller.ui.scroller.calculation.VerticalScrollBoundsProvider;
import com.example.recyclerviewfastscroller.ui.scroller.calculation.position.VerticalScreenPositionCalculator;
import com.example.recyclerviewfastscroller.ui.scroller.calculation.progress.TouchableScrollProgressCalculator;
import com.example.recyclerviewfastscroller.ui.scroller.calculation.progress.VerticalLinearLayoutManagerScrollProgressCalculator;
import com.example.recyclerviewfastscroller.ui.scroller.calculation.progress.VerticalScrollProgressCalculator;

/**
 * Widget used to fast-scroll a vertical {@link RecyclerView}.
 * Currently assumes the use of a {@link LinearLayoutManager}
 */
public class VerticalRecyclerViewFastScroller extends AbsRecyclerViewFastScroller implements RecyclerViewScroller {

    private VerticalScrollProgressCalculator mScrollProgressCalculator;
    private VerticalScreenPositionCalculator mScreenPositionCalculator;

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
    protected int getLayoutResourceId() {
        return R.layout.vertical_recycler_fast_scroller_layout;
    }

    @Override
    protected TouchableScrollProgressCalculator getScrollProgressCalculator() {
        return mScrollProgressCalculator;
    }

    @Override
    public void moveHandleToPosition(float scrollProgress) {
        mHandle.setY(mScreenPositionCalculator.getYPositionFromScrollProgress(scrollProgress));
    }

    @Override
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mScrollProgressCalculator == null || mScreenPositionCalculator == null) {
            VerticalScrollBoundsProvider boundsProvider =
                    new VerticalScrollBoundsProvider(mBar.getY(), mBar.getY() + mBar.getHeight() - mHandle.getHeight());
            mScrollProgressCalculator = new VerticalLinearLayoutManagerScrollProgressCalculator(boundsProvider);
            mScreenPositionCalculator = new VerticalScreenPositionCalculator(boundsProvider);
        }
    }

}
