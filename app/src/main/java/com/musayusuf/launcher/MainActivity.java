package com.musayusuf.launcher;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

    private LauncherRootView rootView;
    private LauncherMainController mainController;
    private LauncherAppLifecycle appLifecycle;

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {
        super.onCreate(savedInstanceState);

        setupWindow();

        rootView =
                new LauncherRootView(this);

        setContentView(rootView);

        mainController =
                new LauncherMainController(
                        this,
                        rootView
                );

        mainController.initialize();

        appLifecycle =
                new LauncherAppLifecycle(
                        this,
                        rootView.getPageManager()
                );

        mainController.start();
        appLifecycle.start();

        rootView.showHome();
    }

    private void setupWindow() {

        requestWindowFeature(
                Window.FEATURE_NO_TITLE
        );

        Window window = getWindow();

        window.addFlags(
                WindowManager.LayoutParams
                        .FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
        );

        window.setStatusBarColor(
                getResources().getColor(
                        R.color.navy
                )
        );

        window.setNavigationBarColor(
                getResources().getColor(
                        R.color.navy
                )
        );

        if (android.os.Build.VERSION.SDK_INT >= 23) {

            window.getDecorView()
                    .setSystemUiVisibility(0);
        }
    }

    public void goHome() {

        if (rootView != null) {
            rootView.showHome();
        }
    }

    public void openAppDrawer() {

        if (rootView != null) {
            rootView.showAppDrawer();
        }
    }

    public void openSettings() {

        if (rootView != null) {
            rootView.showSettings();
        }
    }

    public void openQuickPanel() {

        if (rootView != null) {
            rootView.showQuickPanel();
        }
    }

    public void openWallpapers() {

        if (rootView != null) {
            rootView.showWallpaperGallery();
        }
    }

    public void refreshLauncher() {

        if (mainController != null) {
            mainController.refresh();
        }

        if (rootView != null) {
            rootView.refresh();
        }

        if (appLifecycle != null) {
            appLifecycle.refresh();
        }
    }

    @Override
    protected void onResume() {

        super.onResume();

        if (mainController != null) {
            mainController.start();
        }

        if (appLifecycle != null) {
            appLifecycle.start();
        }

        if (rootView != null) {
            rootView.refresh();
        }
    }

    @Override
    protected void onPause() {

        if (mainController != null) {
            mainController.stop();
        }

        if (appLifecycle != null) {
            appLifecycle.stop();
        }

        super.onPause();
    }

    @Override
    public void onBackPressed() {

        if (rootView != null &&
                rootView.handleBack()) {
            return;
        }

        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {

        if (appLifecycle != null) {
            appLifecycle.destroy();
            appLifecycle = null;
        }

        if (mainController != null) {
            mainController.destroy();
            mainController = null;
        }

        rootView = null;

        super.onDestroy();
    }
}
