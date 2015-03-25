package xyz.danoz.recyclerviewfastscroller.calculation.progress;

import xyz.danoz.recyclerviewfastscroller.calculation.VerticalScrollBoundsProvider;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;

/**
 * Calculates scroll progress for a {@link RecyclerView} with a {@link LinearLayoutManager}
 */
public class VerticalLinearLayoutManagerScrollProgressCalculator extends VerticalScrollProgressCalculator {

    public VerticalLinearLayoutManagerScrollProgressCalculator(VerticalScrollBoundsProvider scrollBoundsProvider) {
        super(scrollBoundsProvider);
    }

    /**
     * @param recyclerView recycler that experiences a scroll event
     * @return the progress through the recycler view list content
     */
    @Override
    public float calculateScrollProgress(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int lastFullyVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition();

        View visibleChild = recyclerView.getChildAt(0);
        if (visibleChild == null) {
            return 0;
        }
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
}
