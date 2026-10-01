package com.musayusuf.launcher;

import android.app.WallpaperManager;
import android.content.Context;
import android.widget.Toast;

import java.io.IOException;

public class LauncherSystemWallpaper {

    public static void apply(Context context, int resourceId) {

        if (context == null || resourceId == 0) return;

        try {

            WallpaperManager manager =
                    WallpaperManager.getInstance(context);

            manager.setResource(resourceId);

            Toast.makeText(
                    context,
                    "Wallpaper na waya an canza shi",
                    Toast.LENGTH_SHORT
            ).show();

        } catch (IOException e) {

            Toast.makeText(
                    context,
                    "An kasa saita wallpaper na waya",
                    Toast.LENGTH_SHORT
            ).show();

        } catch (SecurityException e) {

            Toast.makeText(
                    context,
                    "Ba a bada izini ba don saita wallpaper",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}
