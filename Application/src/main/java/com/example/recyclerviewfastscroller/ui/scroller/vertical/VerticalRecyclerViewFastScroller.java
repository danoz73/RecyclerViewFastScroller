package com.example.recyclerviewfastscroller.ui.scroller.vertical;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.android.recyclerview.R;
import com.example.recyclerviewfastscroller.ui.scroller.AbsRecyclerViewFastScroller;
import com.example.recyclerviewfastscroller.ui.scroller.RecyclerViewScroller;

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

    @Override @NonNull
    public OnScrollListener getOnScrollListener() {
        if (mOnScrollListener == null) {
            mOnScrollListener = new OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int lastFullyVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition();

                    float scrollProgress = calculateScrollProgressThroughList(recyclerView, lastFullyVisiblePosition);
                    moveHandleToPosition(scrollProgress);
                }

                /**
                 * @param recyclerView recycler that experiences scroll event
                 * @param lastFullyVisiblePosition the last fully visible item
                 * @return the progress through the recycler view list content
                 */
                private float calculateScrollProgressThroughList(RecyclerView recyclerView,
                                                                 int lastFullyVisiblePosition) {
                    View visibleChild = recyclerView.getChildAt(0);
                    ViewHolder holder = recyclerView.getChildViewHolder(visibleChild);
                    int itemHeight = holder.itemView.getHeight();
                    int recyclerHeight = recyclerView.getHeight();
                    int itemsInWindow = recyclerHeight / itemHeight;

                    int numItemsInList = recyclerView.getAdapter().getItemCount();
                    int numScrollableSectionsInList = numItemsInList - itemsInWindow;
                    int indexOfLastFullyVisibleItemInFirstSection = numItemsInList - numScrollableSectionsInList - 1;

                    int currentSection = lastFullyVisiblePosition - indexOfLastFullyVisibleItemInFirstSection;

                    return (float) currentSection / numScrollableSectionsInList;
                }
            };
        }
        return mOnScrollListener;
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
