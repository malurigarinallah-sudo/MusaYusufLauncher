package com.musayusuf.launcher;

import android.content.Context;

public class LauncherWallpaperResources {

    private static final String[] WALLPAPER_NAMES = {
            "musa_yusuf_01",
            "musa_yusuf_02",
            "musa_yusuf_03",
            "musa_yusuf_04",
            "musa_yusuf_poster"
    };

    public static String[] getAvailable(Context context) {
        return WALLPAPER_NAMES.clone();
    }

    public static int getResource(Context context, String name) {

        if (name == null || context == null) return 0;

        return context.getResources().getIdentifier(
                name,
                "drawable",
                context.getPackageName()
        );
    }
}
