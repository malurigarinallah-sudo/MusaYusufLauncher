package com.musayusuf.launcher;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.DisplayMetrics;
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

        try {

            Bitmap bitmap = decodeScaledBitmap(resourceId);

            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                imageView.setImageResource(resourceId);
            }

        } catch (OutOfMemoryError error) {

            imageView.setImageDrawable(null);
        }
    }

    private Bitmap decodeScaledBitmap(int resourceId) {

        Resources resources = getResources();

        DisplayMetrics metrics = resources.getDisplayMetrics();

        int targetWidth = metrics.widthPixels;
        int targetHeight = metrics.heightPixels;

        if (targetWidth <= 0) targetWidth = 1080;
        if (targetHeight <= 0) targetHeight = 1920;

        BitmapFactory.Options boundsOptions = new BitmapFactory.Options();
        boundsOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeResource(resources, resourceId, boundsOptions);

        int sampleSize = calculateSampleSize(
                boundsOptions.outWidth,
                boundsOptions.outHeight,
                targetWidth,
                targetHeight
        );

        BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
        decodeOptions.inSampleSize = sampleSize;
        decodeOptions.inPreferredConfig = Bitmap.Config.RGB_565;

        return BitmapFactory.decodeResource(resources, resourceId, decodeOptions);
    }

    private int calculateSampleSize(
            int sourceWidth,
            int sourceHeight,
            int targetWidth,
            int targetHeight
    ) {

        int sampleSize = 1;

        if (sourceWidth <= 0 || sourceHeight <= 0) {
            return sampleSize;
        }

        while ((sourceWidth / sampleSize) > targetWidth * 2
                || (sourceHeight / sampleSize) > targetHeight * 2) {
            sampleSize *= 2;
        }

        return sampleSize;
    }

    public void setDimAmount(float amount) {

        float clamped = Math.max(0f, Math.min(1f, amount));

        dimOverlay.setAlpha(clamped);
    }
}
