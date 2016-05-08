package org.dalol.slidewidget;

import android.graphics.Color;

/**
 * @author Filippo Engidashet <filippo.eng@gmail.com>
 * @version 1.0.0
 * @since 5/8/2016
 */
public class ColorUtils {

    public final static int getHSVColor(float maxVal, float minVal, float value) {
        float color[] = new float[]{120f - ((120f * ((maxVal - minVal) - (value - minVal))) / (maxVal - minVal)), 1f, 1f};
        return Color.HSVToColor(color);
    }
}
