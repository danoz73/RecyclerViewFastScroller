package xyz.danoz.recyclerviewfastscroller.sample.ui.scroller.calculation.position;

import xyz.danoz.recyclerviewfastscroller.sample.ui.scroller.calculation.VerticalScrollBoundsProvider;

/**
 * Calculates the correct vertical Y position for a view based on scroll progress and given bounds
 */
public class VerticalScreenPositionCalculator {

    private final VerticalScrollBoundsProvider mVerticalScrollBoundsProvider;

    public VerticalScreenPositionCalculator(VerticalScrollBoundsProvider scrollBoundsProvider) {
        mVerticalScrollBoundsProvider = scrollBoundsProvider;
    }

    public float getYPositionFromScrollProgress(float scrollProgress) {
        return Math.max(
                mVerticalScrollBoundsProvider.getMinimumScrollY(),
                Math.min(
                        scrollProgress * mVerticalScrollBoundsProvider.getMaximumScrollY(),
                        mVerticalScrollBoundsProvider.getMaximumScrollY()
                )
        );
    }

}
