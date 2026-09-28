package com.musayusuf.launcher;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ScrollView;

public class LauncherHomeView extends FrameLayout {

    private final int pageIndex;
    private final HomeStorage storage;
    private final LauncherAppManager appManager;
    private final AppGrid appGrid;

    public LauncherHomeView(Context context, int pageIndex) {
        super(context);

        this.pageIndex = pageIndex;

        storage = new HomeStorage(context);
        appManager = new LauncherAppManager(context);

        ScrollView scroll = new ScrollView(context);

        appGrid = new AppGrid(context);

        appGrid.setLongPressListener(
                (appInfo, item) -> showRemoveDialog(appInfo)
        );

        FrameLayout.LayoutParams gridParams = new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
        );

        gridParams.gravity = Gravity.TOP;
        gridParams.topMargin = dp(140);

        scroll.addView(appGrid, gridParams);

        addView(
                scroll,
                new FrameLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT
                )
        );

        loadSavedApps();
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void addApp(ApplicationInfo appInfo) {

        if (appInfo == null) return;

        storage.addPackageToPage(pageIndex, appInfo.packageName);

        appGrid.addApp(appInfo);
    }

    private void loadSavedApps() {

        appGrid.clearApps();

        PackageManager packageManager = getContext().getPackageManager();

        for (String packageName : storage.getPackagesForPage(pageIndex)) {

            try {

                ApplicationInfo appInfo =
                        packageManager.getApplicationInfo(packageName, 0);

                appGrid.addApp(appInfo);

            } catch (PackageManager.NameNotFoundException ignored) {
                storage.removePackageFromPage(pageIndex, packageName);
            }
        }
    }

    private void showRemoveDialog(ApplicationInfo appInfo) {

        if (appInfo == null) return;

        new AlertDialog.Builder(getContext())
                .setTitle(appManager.getAppLabel(appInfo))
                .setItems(
                        new String[]{"Open", "Remove from Home"},
                        (dialog, which) -> {

                            if (which == 0) {
                                appManager.launchApp(appInfo);
                            } else {
                                storage.removePackageFromPage(
                                        pageIndex,
                                        appInfo.packageName
                                );
                                refresh();
                            }
                        }
                )
                .setNegativeButton("Cancel", null)
                .show();
    }

    public void refresh() {
        loadSavedApps();
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
