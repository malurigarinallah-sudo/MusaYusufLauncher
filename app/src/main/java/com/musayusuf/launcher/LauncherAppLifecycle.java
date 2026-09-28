package com.musayusuf.launcher;

import android.content.Context;

public class LauncherAppLifecycle {

    private final LauncherPageManager pageManager;
    private final LauncherAppChangeManager appChangeManager;

    public LauncherAppLifecycle(
            Context context,
            LauncherPageManager pageManager
    ) {
        this.pageManager = pageManager;

        appChangeManager = new LauncherAppChangeManager(
                context,
                this::refresh
        );
    }

    public void start() {
        appChangeManager.start();
    }

    public void stop() {
        appChangeManager.stop();
    }

    public void refresh() {

        if (pageManager != null) {
            pageManager.refreshAllPages();
        }
    }

    public void destroy() {
        appChangeManager.stop();
    }
}
