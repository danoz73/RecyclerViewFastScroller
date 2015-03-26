package xyz.danoz.recyclerviewfastscroller.sectionindicator.animation;

import android.view.View;

import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

/**
 * Utility class for animating the popup section indicator
 */
public class DefaultSectionIndicatorAlphaAnimator {

    private static final int ANIMATION_DURATION = 500;

    private final View mSectionIndicatorView;
    private float mTargetAlpha = 0;

    public DefaultSectionIndicatorAlphaAnimator(View sectionIndicatorView) {
        mSectionIndicatorView = sectionIndicatorView;
        ViewHelper.setAlpha(mSectionIndicatorView, 0);
    }

    public void animateTo(float target){
        if (target == mTargetAlpha) {
            return;
        }

        ObjectAnimator alphaAnimator =
                ObjectAnimator.ofFloat(mSectionIndicatorView, "alpha", mTargetAlpha, target);
        alphaAnimator.setDuration(ANIMATION_DURATION);
        alphaAnimator.start();
        mTargetAlpha = target;
    }
}
