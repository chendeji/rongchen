package com.chendeji.rongchen.common.util;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;

/**
 * Created by chendeji on 3/7/15.
 */
public class AnimationUtil {

    public static Animation getTransAnima(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta, long duration,
                                          Animation.AnimationListener listener) {
        Animation tranAnima = new TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta);
        tranAnima.setFillAfter(true);
        tranAnima.setDuration(duration);
        tranAnima.setAnimationListener(listener);
        return tranAnima;
    }

    public static Animation getRotaAnima(long duration, float fromDegrees, float toDegrees, int pivotXType, float pivotXValue,
                                         int pivotYType, float pivotYValue) {
        Animation rotateAnima = new RotateAnimation(fromDegrees, toDegrees, pivotXType, pivotXValue, pivotYType, pivotYValue);
        rotateAnima.setDuration(duration);
        rotateAnima.setFillAfter(true);
        return rotateAnima;
    }

    public static Animation getAlphaAnima(long duration, float fromAlpha,
                                          float toAlpha) {
        Animation alphaAnima = new AlphaAnimation(fromAlpha, toAlpha);
        alphaAnima.setDuration(duration);
        return alphaAnima;
    }

}
