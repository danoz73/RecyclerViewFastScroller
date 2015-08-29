package xyz.danoz.recyclerviewfastscroller;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SectionIndexer;

import xyz.danoz.recyclerviewfastscroller.calculation.progress.ScrollProgressCalculator;
import xyz.danoz.recyclerviewfastscroller.calculation.progress.TouchableScrollProgressCalculator;
import xyz.danoz.recyclerviewfastscroller.sectionindicator.SectionIndicator;

/**
 * Defines a basic widget that will allow for fast scrolling a RecyclerView using the basic paradigm of
 * a handle and a bar.
 *
 * TODO: More specifics and better support for effectively extending this base class
 */
public abstract class AbsRecyclerViewFastScroller extends FrameLayout implements RecyclerViewScroller {

    private static final int[] STYLEABLE = R.styleable.AbsRecyclerViewFastScroller;
    /** The long bar along which a handle travels */
    protected final View mBar;
    /** The handle that signifies the user's progress in the list */
    protected final View mHandle;

    private boolean mFastScrollAlwaysVisible;
    private boolean mIsVisible;
    private long mFastScrollTimeout;
    private long mEventTime;
    private Handler mVisibilityHandler;
    private Runnable mVisibilityRunner;

    /* TODO:
     *      Consider making RecyclerView final and should be passed in using a custom attribute
     *      This could allow for some type checking on the section indicator wrt the adapter of the RecyclerView
    */
    private RecyclerView mRecyclerView;
    private SectionIndicator mSectionIndicator;

    /** If I had my druthers, AbsRecyclerViewFastScroller would implement this as an interface, but Android has made
     * {@link OnScrollListener} an abstract class instead of an interface. Hmmm */
    protected OnScrollListener mOnScrollListener;

    public AbsRecyclerViewFastScroller(Context context) {
        this(context, null, 0);
    }

    public AbsRecyclerViewFastScroller(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbsRecyclerViewFastScroller(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray attributes = getContext().getTheme().obtainStyledAttributes(attrs, STYLEABLE, 0, 0);

        mFastScrollAlwaysVisible = true;
        mFastScrollTimeout = 10000;

        try {
            int layoutResource = attributes.getResourceId(R.styleable.AbsRecyclerViewFastScroller_rfs_fast_scroller_layout,
                    getLayoutResourceId());
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(layoutResource, this, true);

            mBar = findViewById(R.id.scroll_bar);
            mHandle = findViewById(R.id.scroll_handle);

            Drawable barDrawable = attributes.getDrawable(R.styleable.AbsRecyclerViewFastScroller_rfs_barBackground);
            int barColor = attributes.getColor(R.styleable.AbsRecyclerViewFastScroller_rfs_barColor, Color.GRAY);
            applyCustomAttributesToView(mBar, barDrawable, barColor);

            Drawable handleDrawable = attributes.getDrawable(R.styleable.AbsRecyclerViewFastScroller_rfs_handleBackground);
            int handleColor = attributes.getColor(R.styleable.AbsRecyclerViewFastScroller_rfs_handleColor, Color.GRAY);
            applyCustomAttributesToView(mHandle, handleDrawable, handleColor);
        } finally {
            attributes.recycle();
        }

        setOnTouchListener(new FastScrollerTouchListener(this));
    }

    private void applyCustomAttributesToView(View view, Drawable drawable, int color) {
        if (drawable != null) {
            setViewBackground(view, drawable);
        } else {
            view.setBackgroundColor(color);
        }
    }

    /**
     * Provides the ability to programmatically set the color of the fast scroller's handle
     * @param color for the handle to be
     */
    public void setHandleColor(int color) {
        mHandle.setBackgroundColor(color);
    }

    /**
     * Provides the ability to programmatically set the background drawable of the fast scroller's handle
     * @param drawable for the handle's background
     */
    public void setHandleBackground(Drawable drawable) {
        setViewBackground(mHandle, drawable);
    }

    /**
     * Provides the ability to programmatically set the color of the fast scroller's bar
     * @param color for the bar to be
     */
    public void setBarColor(int color) {
        mBar.setBackgroundColor(color);
    }

    /**
     * Provides the ability to programmatically set the background drawable of the fast scroller's bar
     * @param drawable for the bar's background
     */
    public void setBarBackground(Drawable drawable) {
        setViewBackground(mBar, drawable);
    }

    @TargetApi(VERSION_CODES.JELLY_BEAN)
    private void setViewBackground(View view, Drawable background) {
        if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
            view.setBackground(background);
        } else {
            //noinspection deprecation
            view.setBackgroundDrawable(background);
        }
    }

    @Override
    public void setRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        addOnTouchListener();
    }

    public void setSectionIndicator(SectionIndicator sectionIndicator) {
        mSectionIndicator = sectionIndicator;
    }

    @Nullable
    public SectionIndicator getSectionIndicator() {
        return mSectionIndicator;
    }

    @Override
    public void scrollTo(float scrollProgress, boolean fromTouch) {
        int position = getPositionFromScrollProgress(scrollProgress);
        mRecyclerView.scrollToPosition(position);

        updateSectionIndicator(position, scrollProgress);
    }

    private void updateSectionIndicator(int position, float scrollProgress) {
        if (mSectionIndicator != null) {
            mSectionIndicator.setProgress(scrollProgress);
            if (mRecyclerView.getAdapter() instanceof SectionIndexer) {
                SectionIndexer indexer = ((SectionIndexer) mRecyclerView.getAdapter());
                int section = indexer.getSectionForPosition(position);
                Object[] sections = indexer.getSections();
                mSectionIndicator.setSection(sections[section]);
            }
        }
    }

    private int getPositionFromScrollProgress(float scrollProgress) {
        return (int) (mRecyclerView.getAdapter().getItemCount() * scrollProgress);
    }

    /**
     * Detect if we touch the recyclerview. Make visible if we did and set to be invisible.
     */
    private void addOnTouchListener() {
        mHandle.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        if (!mFastScrollAlwaysVisible) {
                            mEventTime = System.currentTimeMillis();
                            setVisible();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (!mFastScrollAlwaysVisible) {
                            mEventTime = System.currentTimeMillis();
                        }
                        break;
                }
                return false;
            }
        });

        mRecyclerView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        if (!mFastScrollAlwaysVisible) {
                            mEventTime = System.currentTimeMillis();
                            setVisible();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (!mFastScrollAlwaysVisible) {
                            mEventTime = System.currentTimeMillis();
                        }
                        break;
                }
                return false;
            }
        });
    }

    /**
     * Classes that extend AbsFastScroller must implement their own {@link OnScrollListener} to respond to scroll
     * events when the {@link #mRecyclerView} is scrolled NOT using the fast scroller.
     * @return an implementation for responding to scroll events from the {@link #mRecyclerView}
     */
    @NonNull
    public OnScrollListener getOnScrollListener() {
        if (mOnScrollListener == null) {
            mOnScrollListener = new OnScrollListener() {
                @Override
                public void onScrolled(final RecyclerView recyclerView, int dx, int dy) {
                    if (!mFastScrollAlwaysVisible) {
                        mEventTime = System.currentTimeMillis();
                        if (dx != 0 && !mFastScrollAlwaysVisible) {
                            setVisible();
                        }
                    }

                    float scrollProgress = 0;
                    ScrollProgressCalculator scrollProgressCalculator = getScrollProgressCalculator();
                    if (scrollProgressCalculator != null) {
                        scrollProgress = scrollProgressCalculator.calculateScrollProgress(recyclerView);
                    }
                    moveHandleToPosition(scrollProgress);
                }
            };
        }
        return mOnScrollListener;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (getScrollProgressCalculator() == null) {
            onCreateScrollProgressCalculator();
        }

        // synchronize the handle position to the RecyclerView
        float scrollProgress = getScrollProgressCalculator().calculateScrollProgress(mRecyclerView);
        moveHandleToPosition(scrollProgress);
    }

    /**
     * Sub classes have to override this method and create the ScrollProgressCalculator instance in this method.
     */
    protected abstract void onCreateScrollProgressCalculator();

    /**
     * Takes a touch event and determines how much scroll progress this translates into
     * @param event touch event received by the layout
     * @return scroll progress, or fraction by which list is scrolled [0 to 1]
     */
    public float getScrollProgress(MotionEvent event) {
        ScrollProgressCalculator scrollProgressCalculator = getScrollProgressCalculator();
        if (scrollProgressCalculator != null) {
            return getScrollProgressCalculator().calculateScrollProgress(event);
        }
        return 0;
    }

    /**
     * Define a layout resource for your implementation of AbsFastScroller
     * Currently must contain a handle view (R.id.scroll_handle) and a bar (R.id.scroll_bar)
     * @return a resource id corresponding to the chosen layout.
     */
    protected abstract int getLayoutResourceId();

    /**
     * Define a ScrollProgressCalculator for your implementation of AbsFastScroller
     * @return a chosen implementation of {@link ScrollProgressCalculator}
     */
    @Nullable
    protected abstract TouchableScrollProgressCalculator getScrollProgressCalculator();

    /**
     * Moves the handle of the scroller by specific progress amount
     * @param scrollProgress fraction by which to move scroller [0 to 1]
     */
    public abstract void moveHandleToPosition(float scrollProgress);

    /**
     * Set whether to always show or use the AbsListView behaviour.
     * @param scrollAlwaysVisible
     */
    public void setScrollAlwaysVisible(boolean scrollAlwaysVisible) {
        mFastScrollAlwaysVisible = scrollAlwaysVisible;
        if (!mFastScrollAlwaysVisible) {
            setInvisible();
            if (mVisibilityHandler == null) {
                mVisibilityHandler = new Handler();
            }
            mVisibilityRunner = new Runnable() {
                public void run() {
                    long end = mEventTime + mFastScrollTimeout;
                    long time = System.currentTimeMillis();
                    if (end < time && mIsVisible) {
                        setInvisible();
                    }
                    try {
                        mVisibilityHandler.postDelayed(this, 333);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            mVisibilityHandler.post(mVisibilityRunner);
        }
    }

    /**
     * Set timeout value.
     * @param desiredTimeout
     */
    public void setTimeout(long desiredTimeout) {
        mFastScrollTimeout = desiredTimeout;
        if (mFastScrollTimeout < 3000) {
            mFastScrollTimeout = 3000;
        }
    }

    /**
     *
     */
    public long getTimeout() {
        return mFastScrollTimeout;
    }

    /**
     * Get scroll visibility status.
     * @return scroll visibility status
     */
    public boolean getScrollAlwaysVisible() {
        return mFastScrollAlwaysVisible;
    }

    /**
     * Set bar and handle to be invisible
     */
    private void setInvisible() {
        mBar.setVisibility(View.INVISIBLE);
        mHandle.setVisibility(View.INVISIBLE);
        mIsVisible = false;
    }

    /**
     * Set bar and handle to be visible
     */
    private void setVisible() {
        mBar.setVisibility(View.VISIBLE);
        mHandle.setVisibility(View.VISIBLE);
        mIsVisible = true;
    }
}