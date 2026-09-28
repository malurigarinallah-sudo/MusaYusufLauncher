package com.musayusuf.launcher;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.FrameLayout;

public class LauncherOverlayManager {

    private final FrameLayout overlayLayer;

    private View currentView;
    private boolean visible = false;

    public LauncherOverlayManager(
            Context context,
            FrameLayout overlayLayer
    ) {
        this.overlayLayer = overlayLayer;
    }

    public void show(View view) {
        display(view, false);
    }

    public void showWithDim(View view) {
        display(view, true);
    }

    private void display(View view, boolean dim) {

        if (view == null || overlayLayer == null) return;

        overlayLayer.removeAllViews();

        overlayLayer.setBackgroundColor(
                dim ? Color.argb(140, 0, 0, 0) : Color.TRANSPARENT
        );

        currentView = view;

        overlayLayer.addView(
                view,
                new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                )
        );

        overlayLayer.setVisibility(View.VISIBLE);

        visible = true;
    }

    public void hide() {

        if (overlayLayer != null) {
            overlayLayer.setVisibility(View.GONE);
            overlayLayer.removeAllViews();
        }

        currentView = null;
        visible = false;
    }

    public boolean isVisible() {
        return visible;
    }

    public View getCurrentView() {
        return currentView;
    }
}
