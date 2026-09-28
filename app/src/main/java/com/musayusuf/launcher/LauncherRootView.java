package com.musayusuf.launcher;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Button;

public class LauncherRootView extends FrameLayout {

    private final LauncherPageManager pageManager;
    private final LauncherOverlayManager overlayManager;

    private final FrameLayout wallpaperLayer;
    private final FrameLayout contentLayer;
    private final FrameLayout systemLayer;
    private final FrameLayout overlayLayer;

    private LauncherWallpaperView wallpaperView;
    private LauncherClockView clockView;
    private ModernDockBar dockBar;

    private float downX;
    private float downY;

    private boolean gestureIntercepting = false;
    private boolean homeVisible = true;

    public LauncherRootView(Context context) {
        super(context);

        setBackgroundColor(Color.BLACK);
        setClickable(true);
        setFocusable(true);

        pageManager = new LauncherPageManager(context);

        wallpaperLayer = new FrameLayout(context);
        contentLayer = new FrameLayout(context);
        systemLayer = new FrameLayout(context);
        overlayLayer = new FrameLayout(context);

        addView(wallpaperLayer, createMatchParams());
        addView(contentLayer, createMatchParams());
        addView(systemLayer, createMatchParams());
        addView(overlayLayer, createMatchParams());

        overlayLayer.setVisibility(GONE);

        overlayManager = new LauncherOverlayManager(
                context,
                overlayLayer
        );

        attachCurrentHomePage();
    }

    private FrameLayout.LayoutParams createMatchParams() {
        return new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
        );
    }

    private void attachCurrentHomePage() {
        pageManager.attachCurrentPage(contentLayer);
        homeVisible = true;
    }

    public void setLauncherWallpaper(
            LauncherWallpaperView wallpaper
    ) {
        wallpaperLayer.removeAllViews();

        wallpaperView = wallpaper;

        if (wallpaperView != null) {
            wallpaperLayer.addView(
                    wallpaperView,
                    createMatchParams()
            );
        }
    }

    public void setLauncherClock(
            LauncherClockView clock
    ) {
        clockView = clock;
        rebuildSystemLayer();
    }

    public void setLauncherDock(
            ModernDockBar dock
    ) {
        dockBar = dock;
        rebuildSystemLayer();
    }

    private void rebuildSystemLayer() {

        systemLayer.removeAllViews();

        if (clockView != null) {

            FrameLayout.LayoutParams clockParams =
                    new FrameLayout.LayoutParams(
                            LayoutParams.MATCH_PARENT,
                            LayoutParams.WRAP_CONTENT
                    );

            clockParams.gravity =
                    android.view.Gravity.TOP |
                    android.view.Gravity.CENTER_HORIZONTAL;

            clockParams.topMargin = dp(25);

            systemLayer.addView(
                    clockView,
                    clockParams
            );
        }

        if (dockBar != null) {

            FrameLayout.LayoutParams dockParams =
                    new FrameLayout.LayoutParams(
                            LayoutParams.MATCH_PARENT,
                            dp(72)
                    );

            dockParams.gravity =
                    android.view.Gravity.BOTTOM |
                    android.view.Gravity.CENTER_HORIZONTAL;

            dockParams.leftMargin = dp(16);
            dockParams.rightMargin = dp(16);
            dockParams.bottomMargin = dp(18);

            systemLayer.addView(
                    dockBar,
                    dockParams
            );
        }
    }

    public void showHome() {

        hideOverlay();

        attachCurrentHomePage();

        contentLayer.setVisibility(VISIBLE);
        systemLayer.setVisibility(VISIBLE);

        homeVisible = true;

        refresh();
    }

    public void nextPage() {

        if (pageManager.nextPage()) {
            attachCurrentHomePage();
            refresh();
        }
    }

    public void previousPage() {

        if (pageManager.previousPage()) {
            attachCurrentHomePage();
            refresh();
        }
    }

    public void goToPage(int index) {

        if (pageManager.goToPage(index)) {
            attachCurrentHomePage();
            refresh();
        }
    }

    public void showAppDrawer() {

        AppDrawer drawer =
                new AppDrawer(
                        getContext(),
                        new AppDrawer.Listener() {

                            @Override
                            public void onClose() {
                                hideOverlay();
                            }

                            @Override
                            public void onAddToHome(
                                    ApplicationInfo app
                            ) {

                                if (app == null) return;

                                LauncherHomeView home =
                                        pageManager.getCurrentPage();

                                if (home != null) {
                                    home.addApp(app);
                                }

                                hideOverlay();
                                showHome();
                            }

                            @Override
                            public void onAddToDock(
                                    ApplicationInfo app
                            ) {
                                hideOverlay();
                            }
                        }
                );

        showOverlay(drawer, false);
    }

    public void showQuickPanel() {

        QuickPanelView panel =
                new QuickPanelView(
                        getContext(),
                        new QuickPanelView.Listener() {

                            @Override
                            public void onClose() {
                                hideOverlay();
                            }

                            @Override
                            public void onSettings() {
                                showSettings();
                            }

                            @Override
                            public void onWallpaper() {
                                showWallpaperGallery();
                            }
                        }
                );

        showOverlay(panel, true);
    }

    public void showSettings() {

        LauncherSettingsView settings =
                new LauncherSettingsView(
                        getContext(),
                        new LauncherSettingsView.Listener() {

                            @Override
                            public void onClose() {
                                hideOverlay();
                            }

                            @Override
                            public void onWallpaper() {
                                showWallpaperGallery();
                            }

                            @Override
                            public void onRefresh() {
                                refresh();
                            }
                        }
                );

        showOverlay(settings, true);
    }

    public void showWallpaperGallery() {

        String[] wallpapers =
                LauncherWallpaperResources
                        .getAvailable(getContext());

        if (wallpapers.length == 0) return;

        final LauncherPreferences preferences =
                new LauncherPreferences(getContext());

        LinearLayout panel =
                new LinearLayout(getContext());

        panel.setOrientation(
                LinearLayout.VERTICAL
        );

        panel.setPadding(
                dp(20),
                dp(20),
                dp(20),
                dp(20)
        );

        panel.setBackgroundColor(
                Color.argb(245, 7, 27, 58)
        );

        TextView title =
                new TextView(getContext());

        title.setText("Wallpapers");
        title.setTextColor(Color.WHITE);
        title.setTextSize(22);
        title.setGravity(
                android.view.Gravity.CENTER
        );

        panel.addView(
                title,
                new LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT
                )
        );

        ScrollView scroll =
                new ScrollView(getContext());

        LinearLayout list =
                new LinearLayout(getContext());

        list.setOrientation(
                LinearLayout.VERTICAL
        );

        for (String wallpaper : wallpapers) {

            final String selectedWallpaper =
                    wallpaper;

            Button button =
                    new Button(getContext());

            button.setText(
                    formatWallpaperName(wallpaper)
            );

            button.setOnClickListener(v -> {

                preferences.setWallpaper(
                        selectedWallpaper
                );

                refresh();

                hideOverlay();

                showHome();
            });

            list.addView(
                    button,
                    new LinearLayout.LayoutParams(
                            LayoutParams.MATCH_PARENT,
                            dp(55)
                    )
            );
        }

        scroll.addView(
                list,
                new ScrollView.LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT
                )
        );

        panel.addView(
                scroll,
                new LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        0,
                        1f
                )
        );

        Button close =
                new Button(getContext());

        close.setText("Close");

        close.setOnClickListener(
                v -> hideOverlay()
        );

        panel.addView(
                close,
                new LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        dp(55)
                )
        );

        showOverlay(panel, true);
    }

    private String formatWallpaperName(
            String name
    ) {

        if (name == null) {
            return "Wallpaper";
        }

        String result =
                name.replace("_", " ");

        if (result.length() == 0) {
            return "Wallpaper";
        }

        return result.substring(0, 1).toUpperCase()
                + result.substring(1);
    }

    private void showOverlay(
            View view,
            boolean dim
    ) {

        if (view == null) return;

        contentLayer.setVisibility(VISIBLE);

        if (dim) {
            overlayManager.showWithDim(view);
        } else {
            overlayManager.show(view);
        }
    }

    public void hideOverlay() {

        overlayManager.hide();

        if (homeVisible) {
            contentLayer.setVisibility(VISIBLE);
            systemLayer.setVisibility(VISIBLE);
        }
    }

    public boolean isOverlayVisible() {
        return overlayManager.isVisible();
    }

    public void refresh() {

        LauncherHomeView currentPage =
                pageManager.getCurrentPage();

        if (currentPage != null) {
            currentPage.refresh();
        }

        pageManager.refreshAllPages();

        LauncherPreferences preferences =
                new LauncherPreferences(getContext());

        if (wallpaperView != null) {

            int resource =
                    LauncherWallpaperResources.getResource(
                            getContext(),
                            preferences.getWallpaper()
                    );

            if (resource != 0) {
                wallpaperView.setWallpaper(resource);
            }

            wallpaperView.setDimAmount(
                    preferences.getWallpaperDim()
            );
        }

        if (clockView != null) {

            clockView.setShowDate(
                    preferences.isShowDate()
            );

            clockView.setVisibility(
                    preferences.isShowClock()
                            ? VISIBLE
                            : GONE
            );
        }

        if (dockBar != null) {

            dockBar.setVisibility(
                    preferences.isShowDock()
                            ? VISIBLE
                            : GONE
            );

            dockBar.refresh();
        }
    }

    public LauncherPageManager getPageManager() {
        return pageManager;
    }

    public LauncherOverlayManager getOverlayManager() {
        return overlayManager;
    }

    public boolean handleBack() {

        if (overlayManager.isVisible()) {
            hideOverlay();
            return true;
        }

        if (!homeVisible) {
            showHome();
            return true;
        }

        return false;
    }

    /*
     * IMPORTANT:
     * We no longer override dispatchTouchEvent().
     * This allows buttons, apps and settings controls
     * to receive normal taps.
     *
     * Swipe gestures are detected only after the
     * finger moves beyond the threshold.
     */

    @Override
    public boolean onInterceptTouchEvent(
            MotionEvent event
    ) {

        if (overlayManager.isVisible()) {
            return false;
        }

        switch (event.getActionMasked()) {

            case MotionEvent.ACTION_DOWN:

                downX = event.getX();
                downY = event.getY();
                gestureIntercepting = false;

                return false;

            case MotionEvent.ACTION_MOVE:

                float moveX =
                        event.getX() - downX;

                float moveY =
                        event.getY() - downY;

                float absX =
                        Math.abs(moveX);

                float absY =
                        Math.abs(moveY);

                int threshold = dp(80);

                if ((absX > threshold ||
                        absY > threshold) &&
                        absX != absY) {

                    gestureIntercepting = true;

                    return true;
                }

                return false;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                return gestureIntercepting;
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(
            MotionEvent event
    ) {

        if (overlayManager.isVisible()) {
            return false;
        }

        if (event.getActionMasked() ==
                MotionEvent.ACTION_UP) {

            float upX = event.getX();
            float upY = event.getY();

            float deltaX = upX - downX;
            float deltaY = upY - downY;

            float absX = Math.abs(deltaX);
            float absY = Math.abs(deltaY);

            int threshold = dp(80);

            if (absY > threshold &&
                    absY > absX) {

                if (deltaY < 0) {
                    showAppDrawer();
                } else {
                    showQuickPanel();
                }

                gestureIntercepting = false;

                return true;
            }

            if (absX > threshold &&
                    absX > absY) {

                if (deltaX < 0) {
                    nextPage();
                } else {
                    previousPage();
                }

                gestureIntercepting = false;

                return true;
            }

            gestureIntercepting = false;
        }

        return true;
    }

    private int dp(int value) {

        return (int) (
                value *
                getResources()
                        .getDisplayMetrics()
                        .density
        );
    }
}
