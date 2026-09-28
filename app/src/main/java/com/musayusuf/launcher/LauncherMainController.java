package com.musayusuf.launcher;

import android.content.Context;
import android.view.View;

public final class LauncherMainController {

    private final Context context;
    private final LauncherRootView rootView;
    private final LauncherPreferences preferences;

    private LauncherWallpaperView wallpaperView;
    private LauncherClockView clockView;
    private ModernDockBar dockBar;

    public LauncherMainController(
            Context context,
            LauncherRootView rootView
    ) {
        this.context = context;
        this.rootView = rootView;
        this.preferences =
                new LauncherPreferences(context);
    }

    public void initialize() {

        setupWallpaper();
        setupClock();
        setupDock();

        attachToRoot();
    }

    private void setupWallpaper() {

        wallpaperView =
                new LauncherWallpaperView(context);

        int resource =
                LauncherWallpaperResources.getResource(
                        context,
                        preferences.getWallpaper()
                );

        if (resource != 0) {
            wallpaperView.setWallpaper(resource);
        }

        wallpaperView.setDimAmount(
                preferences.getWallpaperDim()
        );
    }

    private void setupClock() {

        clockView =
                new LauncherClockView(context);

        clockView.setShowDate(
                preferences.isShowDate()
        );

        clockView.setVisibility(
                preferences.isShowClock()
                        ? View.VISIBLE
                        : View.GONE
        );
    }

    private void setupDock() {

        dockBar =
                new ModernDockBar(context);

        dockBar.setVisibility(
                preferences.isShowDock()
                        ? View.VISIBLE
                        : View.GONE
        );
    }

    private void attachToRoot() {

        if (rootView == null) return;

        rootView.setLauncherWallpaper(
                wallpaperView
        );

        rootView.setLauncherClock(
                clockView
        );

        rootView.setLauncherDock(
                dockBar
        );
    }

    public void start() {

        if (clockView != null) {
            clockView.start();
        }
    }

    public void stop() {

        if (clockView != null) {
            clockView.stop();
        }
    }

    public void refresh() {

        refreshWallpaper();
        refreshClock();
        refreshDock();
    }

    private void refreshWallpaper() {

        if (wallpaperView == null) return;

        int resource =
                LauncherWallpaperResources.getResource(
                        context,
                        preferences.getWallpaper()
                );

        if (resource != 0) {
            wallpaperView.setWallpaper(resource);
        }

        wallpaperView.setDimAmount(
                preferences.getWallpaperDim()
        );
    }

    private void refreshClock() {

        if (clockView == null) return;

        clockView.setShowDate(
                preferences.isShowDate()
        );

        clockView.setVisibility(
                preferences.isShowClock()
                        ? View.VISIBLE
                        : View.GONE
        );
    }

    private void refreshDock() {

        if (dockBar == null) return;

        dockBar.setVisibility(
                preferences.isShowDock()
                        ? View.VISIBLE
                        : View.GONE
        );

        dockBar.refresh();
    }

    public LauncherWallpaperView getWallpaperView() {
        return wallpaperView;
    }

    public LauncherClockView getClockView() {
        return clockView;
    }

    public ModernDockBar getDockBar() {
        return dockBar;
    }

    public void destroy() {

        if (clockView != null) {
            clockView.stop();
        }

        if (dockBar != null) {
            dockBar.clear();
        }

        wallpaperView = null;
        clockView = null;
        dockBar = null;
    }
}
