package xyz.danoz.recyclerviewfastscroller.sample.data;

import android.graphics.Color;

/**
 * Dummy dataset of N {@link ColorData} objects
 */
public class ColorDataSet {

    private static final int DATA_SET_SIZE = 100;
    private static final float NUM_HUES = 359;
    private static final ColorData[] mColors = new ColorData[DATA_SET_SIZE];

    public ColorDataSet() {
        this(new ColorGroupCalculator());
    }

    private ColorDataSet(ColorGroupCalculator colorGroupCalculator) {
        for (int i = 0; i < mColors.length; i++) {
            mColors[i] = generateNewColor(i, mColors.length, colorGroupCalculator);
        }
    }

    private ColorData generateNewColor(int position, int numColors, ColorGroupCalculator colorGroupCalculator) {
        float positionBasedHue = position * (NUM_HUES / numColors);
        int color = Color.HSVToColor(new float[] {positionBasedHue, 1, 1});
        return new ColorData(color, colorGroupCalculator.getColorGroup(positionBasedHue));
    }

    public int getSize() {
        return mColors.length;
    }

    public ColorData get(int position) {
        return mColors[position];
    }

}
