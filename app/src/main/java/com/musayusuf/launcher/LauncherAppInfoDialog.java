package com.musayusuf.launcher;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;

public class LauncherAppInfoDialog {

    public interface Listener {
        void onOpen(ApplicationInfo appInfo);
        void onAddToHome(ApplicationInfo appInfo);
        void onAddToDock(ApplicationInfo appInfo);
        void onAppInfo(ApplicationInfo appInfo);
        void onUninstall(ApplicationInfo appInfo);
    }

    public static void show(
            Context context,
            ApplicationInfo appInfo,
            CharSequence title,
            Listener listener
    ) {

        if (context == null || appInfo == null) return;

        String[] options = {
                "Open",
                "Add to Home",
                "Add to Dock",
                "App Info",
                "Uninstall"
        };

        new AlertDialog.Builder(context)
                .setTitle(title)
                .setItems(
                        options,
                        (dialog, which) -> {

                            if (listener == null) return;

                            switch (which) {

                                case 0:
                                    listener.onOpen(appInfo);
                                    break;

                                case 1:
                                    listener.onAddToHome(appInfo);
                                    break;

                                case 2:
                                    listener.onAddToDock(appInfo);
                                    break;

                                case 3:
                                    listener.onAppInfo(appInfo);
                                    break;

                                case 4:
                                    listener.onUninstall(appInfo);
                                    break;
                            }
                        }
                )
                .setNegativeButton("Cancel", null)
                .show();
    }
}
