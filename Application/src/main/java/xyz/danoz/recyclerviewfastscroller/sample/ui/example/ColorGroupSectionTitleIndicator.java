package xyz.danoz.recyclerviewfastscroller.sample.ui.example;

import android.content.Context;
import android.util.AttributeSet;

import xyz.danoz.recyclerviewfastscroller.sample.data.ColorGroup;
import xyz.danoz.recyclerviewfastscroller.sample.ui.scroller.sectionindicator.title.SectionTitleIndicator;

/**
 * Indicator for sections of type {@link ColorGroup}
 */
public class ColorGroupSectionTitleIndicator extends SectionTitleIndicator<ColorGroup> {

    public ColorGroupSectionTitleIndicator(Context context) {
        super(context);
    }

    public ColorGroupSectionTitleIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorGroupSectionTitleIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setSection(ColorGroup colorGroup) {
        // Example of using a single character
        setTitleText(colorGroup.getName().charAt(0) + "");

        // Example of using a longer string
        // setTitleText(colorGroup.getName());

        setIndicatorTextColor(colorGroup.getAsColor());
    }

}
