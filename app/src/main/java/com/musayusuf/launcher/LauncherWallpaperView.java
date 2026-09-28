package com.musayusuf.launcher;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class LauncherWallpaperView extends FrameLayout {

    private final ImageView imageView;
    private final View dimOverlay;

    public LauncherWallpaperView(Context context) {
        super(context);

        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        addView(
                imageView,
                new LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT
                )
        );

        dimOverlay = new View(context);
        dimOverlay.setBackgroundColor(Color.BLACK);
        dimOverlay.setAlpha(0f);

        addView(
                dimOverlay,
                new LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT
                )
        );
    }

    public void setWallpaper(int resourceId) {

        if (resourceId == 0) return;

        imageView.setImageResource(resourceId);
    }

    public void setDimAmount(float amount) {

        float clamped = Math.max(0f, Math.min(1f, amount));

        dimOverlay.setAlpha(clamped);
    }
}
