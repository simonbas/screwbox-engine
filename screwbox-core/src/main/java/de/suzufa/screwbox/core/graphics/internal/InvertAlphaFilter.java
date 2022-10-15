package de.suzufa.screwbox.core.graphics.internal;

import java.awt.Color;
import java.awt.image.RGBImageFilter;

public class InvertAlphaFilter extends RGBImageFilter {

    @Override
    public int filterRGB(int x, int y, int rgb) {
        Color currentColor = new Color(rgb, true);
        int alpha = currentColor.getAlpha();
        return new Color(0, 0, 0, 255 - alpha).getRGB();
    }

}
