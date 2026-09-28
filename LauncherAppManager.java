package com.musayusuf.launcher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.widget.Toast;

public class LauncherAppManager {

    private final Context context;
    private final PackageManager packageManager;

    public LauncherAppManager(Context context) {
        this.context = context.getApplicationContext();
        this.packageManager = this.context.getPackageManager();
    }

    public void launchApp(ApplicationInfo app) {

        if (app == null) return;

        try {

            Intent intent =
                    packageManager.getLaunchIntentForPackage(app.packageName);

            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }

        } catch (Exception e) {

            Toast.makeText(
                    context,
                    "Unable to open app",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    public CharSequence getAppLabel(ApplicationInfo app) {

        if (app == null) return "";

        return app.loadLabel(packageManager);
    }

    public void openAppInfo(ApplicationInfo app) {

        if (app == null) return;

        try {

            Intent intent =
                    new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);

            intent.setData(
                    Uri.parse("package:" + app.packageName)
            );

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent);

        } catch (Exception ignored) {
        }
    }

    public void uninstallApp(ApplicationInfo app) {

        if (app == null) return;

        try {

            Intent intent = new Intent(Intent.ACTION_DELETE);

            intent.setData(
                    Uri.parse("package:" + app.packageName)
            );

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent);

        } catch (Exception ignored) {
        }
    }
}
