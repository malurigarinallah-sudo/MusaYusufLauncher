package com.musayusuf.launcher;

import android.content.Context;
import android.content.SharedPreferences;

public class LauncherPreferences {

    private static final String PREFS_NAME = "launcher_prefs";

    private static final String KEY_WALLPAPER = "wallpaper";
    private static final String KEY_WALLPAPER_DIM = "wallpaper_dim";
    private static final String KEY_SHOW_CLOCK = "show_clock";
    private static final String KEY_SHOW_DATE = "show_date";
    private static final String KEY_SHOW_DOCK = "show_dock";
    private static final String KEY_DARK_THEME = "dark_theme";

    private final SharedPreferences prefs;

    public LauncherPreferences(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public String getWallpaper() {
        return prefs.getString(KEY_WALLPAPER, "musa_yusuf_01");
    }

    public void setWallpaper(String name) {
        prefs.edit().putString(KEY_WALLPAPER, name).apply();
    }

    public float getWallpaperDim() {
        return prefs.getFloat(KEY_WALLPAPER_DIM, 0f);
    }

    public void setWallpaperDim(float dim) {
        prefs.edit().putFloat(KEY_WALLPAPER_DIM, dim).apply();
    }

    public boolean isShowClock() {
        return prefs.getBoolean(KEY_SHOW_CLOCK, true);
    }

    public void setShowClock(boolean show) {
        prefs.edit().putBoolean(KEY_SHOW_CLOCK, show).apply();
    }

    public boolean isShowDate() {
        return prefs.getBoolean(KEY_SHOW_DATE, true);
    }

    public void setShowDate(boolean show) {
        prefs.edit().putBoolean(KEY_SHOW_DATE, show).apply();
    }

    public boolean isShowDock() {
        return prefs.getBoolean(KEY_SHOW_DOCK, true);
    }

    public void setShowDock(boolean show) {
        prefs.edit().putBoolean(KEY_SHOW_DOCK, show).apply();
    }

    public boolean isDarkTheme() {
        return prefs.getBoolean(KEY_DARK_THEME, true);
    }

    public void setDarkTheme(boolean dark) {
        prefs.edit().putBoolean(KEY_DARK_THEME, dark).apply();
    }
}
