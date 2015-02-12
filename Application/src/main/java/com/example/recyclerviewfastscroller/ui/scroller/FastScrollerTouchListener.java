package com.example.recyclerviewfastscroller.ui.scroller;

import com.example.recyclerviewfastscroller.ui.scroller.vertical.VerticalRecyclerViewFastScroller;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * Touch listener that will move a {@link AbsRecyclerViewFastScroller}'s handle to a specified offset along the scroll bar
 */
class FastScrollerTouchListener implements OnTouchListener {

    private final AbsRecyclerViewFastScroller mFastScroller;

    /**
     * @param fastScroller {@link VerticalRecyclerViewFastScroller} for this listener to scroll
     */
    public FastScrollerTouchListener(AbsRecyclerViewFastScroller fastScroller) {
        mFastScroller = fastScroller;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float scrollProgress = mFastScroller.convertTouchEventToScrollProgress(event);
        mFastScroller.scrollTo(scrollProgress);
        mFastScroller.moveHandleToPosition(scrollProgress);
        return true;
    }

}
