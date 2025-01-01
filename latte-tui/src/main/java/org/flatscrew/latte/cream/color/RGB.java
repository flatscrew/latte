package org.flatscrew.latte.cream.color;

import org.hsluv.HUSLColorConverter;

public record RGB(float r, float g, float b) {

    public static RGB black() {
        return new RGB(0, 0, 0);
    }

    public static RGB fromHexString(String hexValue) {
        String hex = hexValue.replace("#", "").trim();
        float factor = 1.0f / 255.0f;

        if (hex.length() == 3) {
            hex = String.format("%c%c%c%c%c%c",
                    hex.charAt(0), hex.charAt(0),
                    hex.charAt(1), hex.charAt(1),
                    hex.charAt(2), hex.charAt(2));
            factor = 1.0f / 15.0f;
        } else if (hex.length() != 6) {
            throw new NumberFormatException("color: %s is not a hex-color".formatted(hexValue));
        }

        try {
            return new RGB(
                    Integer.parseInt(hex.substring(0, 2), 16) * factor,
                    Integer.parseInt(hex.substring(2, 4), 16) * factor,
                    Integer.parseInt(hex.substring(4, 6), 16) * factor
            );
        } catch (NumberFormatException e) {
            throw new NumberFormatException("color: %s is not a hex-color".formatted(hexValue));
        }
    }

    public ANSI256Color toANSI256Color() {
        // Convert float values in range 0-1 to 0-255 range
        float rScaled = r * 255.0f;
        float gScaled = g * 255.0f;
        float bScaled = b * 255.0f;

        // Value to color index converter (0-5)
        int rIndex = valueToColorIndex(rScaled);
        int gIndex = valueToColorIndex(gScaled);
        int bIndex = valueToColorIndex(bScaled);

        // Calculate the 0-based color index (16..231)
        int colorIndex = 36 * rIndex + 6 * gIndex + bIndex; // 0..215

        // Calculate actual RGB values for the color cube index
        int[] indexToColorValue = {0, 0x5f, 0x87, 0xaf, 0xd7, 0xff};
        float cr = indexToColorValue[rIndex] / 255.0f;
        float cg = indexToColorValue[gIndex] / 255.0f;
        float cb = indexToColorValue[bIndex] / 255.0f;

        // Calculate the nearest gray index (232..255)
        float average = (rIndex + gIndex + bIndex) / 3.0f;
        int grayIndex;
        if (average > 238) {
            grayIndex = 23;
        } else {
            grayIndex = (int)((average - 3) / 10); // 0..23
        }
        float grayValue = (8 + 10 * grayIndex) / 255.0f;

        // Compare distances to determine whether to use color or grayscale
        float colorDist = distanceHSLuv(new RGB(cr, cg, cb));
        float grayDist = distanceHSLuv(new RGB(grayValue, grayValue, grayValue));

        if (colorDist <= grayDist) {
            return new ANSI256Color(16 + colorIndex);
        }
        return new ANSI256Color(232 + grayIndex);
    }

    private int valueToColorIndex(float value) {
        if (value < 48) {
            return 0;
        }
        if (value < 115) {
            return 1;
        }
        return Math.min(5, (int)((value - 35) / 40));
    }

    public float distanceHSLuv(RGB other) {
        // Convert both colors to HSLuv and calculate distance
        double[] hsluv1 = HUSLColorConverter.rgbToHsluv(new double[]{r, g, b});
        double[] hsluv2 = HUSLColorConverter.rgbToHsluv(new double[]{other.r, other.g, other.b});

        // Use the same distance calculation as the Go version
        double dH = (hsluv1[0] - hsluv2[0]) / 100.0;
        double dS = hsluv1[1] - hsluv2[1];
        double dL = hsluv1[2] - hsluv2[2];

        return (float)Math.sqrt(dH * dH + dS * dS + dL * dL);
    }

    public HSL toHSL() {
        float min = Math.min(Math.min(r, g), b);
        float max = Math.max(Math.max(r, g), b);

        float l = (max + min) / 2;
        float h, s;

        if (min == max) {
            s = 0;
            h = 0;
        } else {
            if (l < 0.5f) {
                s = (max - min) / (max + min);
            } else {
                s = (max - min) / (2.0f - max - min);
            }

            if (max == r) {
                h = (g - b) / (max - min);
            } else if (max == g) {
                h = 2.0f + (b - r) / (max - min);
            } else {
                h = 4.0f + (r - g) / (max - min);
            }

            h *= 60;

            if (h < 0) {
                h += 360;
            }
        }
        return new HSL(h, s, l);
    }

    @Override
    public String toString() {
        return "%f,%f,%f".formatted(r, g, b);
    }
}
