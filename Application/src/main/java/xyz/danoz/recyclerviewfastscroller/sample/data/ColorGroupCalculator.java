package xyz.danoz.recyclerviewfastscroller.sample.data;

import android.support.annotation.NonNull;

/**
 * Allow for finding a color group based on a hue
 */
class ColorGroupCalculator {

    @NonNull
    public ColorGroup getColorGroup(float hue) {
        for (ColorGroup group : ColorGroup.values()) {
            if (group.containsHue((int) hue)) {
                return group;
            }
        }

        throw new NullPointerException("Could not classify hue into Color Group!");
    }
}
