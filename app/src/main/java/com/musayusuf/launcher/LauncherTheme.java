package com.musayusuf.launcher;

import android.content.Context;
import android.graphics.Color;

public class LauncherTheme {

    public static boolean isDark(Context context) {
        return new LauncherPreferences(context).isDarkTheme();
    }

    public static int panelBackground(Context context) {
        return isDark(context)
                ? Color.argb(245, 7, 27, 58)
                : Color.argb(245, 245, 247, 252);
    }

    public static int textColor(Context context) {
        return isDark(context) ? Color.WHITE : Color.rgb(15, 23, 42);
    }

    public static int secondaryTextColor(Context context) {
        return isDark(context) ? Color.LTGRAY : Color.rgb(90, 98, 110);
    }

    public static int buttonBackground(Context context) {
        return isDark(context)
                ? Color.argb(70, 255, 255, 255)
                : Color.argb(35, 15, 23, 42);
    }

    public static int dockBackground(Context context) {
        return isDark(context)
                ? Color.argb(90, 255, 255, 255)
                : Color.argb(160, 255, 255, 255);
    }

    public static int searchBoxBackground(Context context) {
        return isDark(context)
                ? Color.argb(90, 255, 255, 255)
                : Color.argb(60, 15, 23, 42);
    }
}
