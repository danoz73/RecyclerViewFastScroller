package xyz.danoz.recyclerviewfastscroller.sample.data;

import android.support.annotation.NonNull;

/**
 * Data object representing a color
 */
public class ColorData {

    private final int mColorInt;
    private final ColorGroup mColorGroup;

    public ColorData(int colorInt, @NonNull ColorGroup colorGroup) {
        mColorInt = colorInt;
        mColorGroup = colorGroup;
    }

    public int getIntValue() {
        return mColorInt;
    }

    public ColorGroup getColorGroup() {
        return mColorGroup;
    }

    @Override
    public String toString() {
        return String.format("#%06X", (0xFFFFFF & mColorInt));
    }
}
