package xyz.danoz.recyclerviewfastscroller;

import xyz.danoz.recyclerviewfastscroller.sectionindicator.SectionIndicator;

import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * Touch listener that will move a {@link AbsRecyclerViewFastScroller}'s handle to a specified offset along the scroll bar
 */
class FastScrollerTouchListener implements OnTouchListener {

    private final AbsRecyclerViewFastScroller mFastScroller;

    /**
     * @param fastScroller {@link xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller} for this listener to scroll
     */
    public FastScrollerTouchListener(AbsRecyclerViewFastScroller fastScroller) {
        mFastScroller = fastScroller;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        SectionIndicator sectionIndicator = mFastScroller.getSectionIndicator();
        showOrHideIndicator(sectionIndicator, event);

        float scrollProgress = mFastScroller.getScrollProgress(event);
        mFastScroller.scrollTo(scrollProgress, true);
        mFastScroller.moveHandleToPosition(scrollProgress);
        return true;
    }

    private void showOrHideIndicator(@Nullable SectionIndicator sectionIndicator, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mFastScroller.notifyScrollState(true);
                if (sectionIndicator != null)
                    sectionIndicator.animateAlpha(1f);
                return;
            case MotionEvent.ACTION_UP:
                mFastScroller.notifyScrollState(false);
                if (sectionIndicator != null)
                    sectionIndicator.animateAlpha(0f);
        }
    }
}
