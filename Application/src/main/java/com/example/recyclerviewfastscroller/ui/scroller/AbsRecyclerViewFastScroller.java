package com.example.recyclerviewfastscroller.ui.scroller;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.example.android.recyclerview.R;

/**
 * Defines a basic widget that will allow for fast scrolling a RecyclerView using the basic paradigm of
 * a handle and a bar.
 *
 * TODO: More specifics and better support for effectively extending this base class
 */
public abstract class AbsRecyclerViewFastScroller extends FrameLayout implements RecyclerViewScroller {

    /** The long bar along which a handle travels */
    protected View mBar;
    /** The handle that signifies the user's progress in the list */
    protected View mHandle;

    private RecyclerView mRecyclerView;

    /**
     * If I had my druthers, AbsFastScroller would implement this as an interface, but an AOSP engineer decided
     * to make {@link OnScrollListener} an abstract class instead of an interface. Hmmm.
     */
    protected OnScrollListener mOnScrollListener;

    public AbsRecyclerViewFastScroller(Context context) {
        this(context, null, 0);
    }

    public AbsRecyclerViewFastScroller(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbsRecyclerViewFastScroller(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(getLayoutResourceId(), this, true);

        mBar = findViewById(R.id.scroll_bar);
        mHandle = findViewById(R.id.scroll_handle);
        applyCustomAttributes(attrs, mBar, mHandle);

        setOnTouchListener(new FastScrollerTouchListener(this));
    }

    /**
     * Method for applying custom attributes to a Scroller
     * @param scrollBar can have a custom color and resource
     * @param scrollHandle can have a custom color or resource
     */
    protected void applyCustomAttributes(AttributeSet attrs, View scrollBar, View scrollHandle) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs,R.styleable.VerticalRecyclerViewFastScroller, 0, 0);

        try {
            Drawable barDrawable = a.getDrawable(R.styleable.VerticalRecyclerViewFastScroller_barBackground);
            int barColor = a.getColor(R.styleable.VerticalRecyclerViewFastScroller_barColor, Color.GRAY);
            applyCustomAttributesToView(scrollBar, barDrawable, barColor);

            Drawable handleDrawable = a.getDrawable(R.styleable.VerticalRecyclerViewFastScroller_handleBackground);
            int handleColor = a.getColor(R.styleable.VerticalRecyclerViewFastScroller_handleColor, Color.GRAY);
            applyCustomAttributesToView(scrollHandle, handleDrawable, handleColor);
        } finally {
            a.recycle();
        }
    }

    private void applyCustomAttributesToView(View view, Drawable drawable, int color) {
        if (drawable != null) {
            if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
                view.setBackground(drawable);
            } else {
                //noinspection deprecation
                view.setBackgroundDrawable(drawable);
            }
        } else {
            view.setBackgroundColor(color);
        }
    }

    @Override
    public void setRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    @Override
    public void scrollTo(float scrollProgress) {
        int position = getPositionFromScrollProgress(scrollProgress);
        mRecyclerView.scrollToPosition(position);
    }

    private int getPositionFromScrollProgress(float scrollProgress) {
        return (int) (mRecyclerView.getAdapter().getItemCount() * scrollProgress);
    }

    /**
     * Classes that extend AbsFastScroller must implement their own {@link OnScrollListener} to respond to scroll
     * events when the {@link #mRecyclerView} is scrolled NOT using the fast scroller.
     * @return an implementation for responding to scroll events from the {@link #mRecyclerView}
     */
    @NonNull
    public abstract OnScrollListener getOnScrollListener();

    /**
     * Moves the handle of the scroller by specific progress amount
     * @param scrollProgress fraction by which to move scroller [0 to 1]
     */
    public abstract void moveHandleToPosition(float scrollProgress);

    /**
     * Takes a touch event and determines how much scroll progress this translates into
     * @param event touch event received by the layout
     * @return scroll progress, or fraction by which list is scrolled [0 to 1]
     */
    public abstract float convertTouchEventToScrollProgress(MotionEvent event);

    /**
     * Define a layout resource for your implementation of AbsFastScroller
     * Currently must contain a handle view (R.id.scroll_handle) and a bar (R.id.scroll_bar)
     * @return a resource id corresponding to the chosen layout.
     */
    protected abstract int getLayoutResourceId();

}
