package com.musayusuf.launcher;

import android.app.role.RoleManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;

public class LauncherDefaultHomeHelper {

    public static boolean isDefaultLauncher(Context context) {

        if (context == null) return false;

        try {

            PackageManager packageManager = context.getPackageManager();

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);

            ResolveInfo resolveInfo = packageManager.resolveActivity(
                    intent,
                    PackageManager.MATCH_DEFAULT_ONLY
            );

            return resolveInfo != null
                    && resolveInfo.activityInfo != null
                    && context.getPackageName().equals(
                            resolveInfo.activityInfo.packageName
                    );

        } catch (Exception e) {
            return false;
        }
    }

    public static void requestDefaultLauncher(Context context) {

        if (context == null) return;

        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                RoleManager roleManager = (RoleManager)
                        context.getSystemService(Context.ROLE_SERVICE);

                if (roleManager != null
                        && roleManager.isRoleAvailable(RoleManager.ROLE_HOME)
                        && !roleManager.isRoleHeld(RoleManager.ROLE_HOME)) {

                    Intent roleIntent = roleManager.createRequestRoleIntent(
                            RoleManager.ROLE_HOME
                    );

                    roleIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    context.startActivity(roleIntent);

                    return;
                }
            }

            Intent fallbackIntent = new Intent(Intent.ACTION_MAIN);
            fallbackIntent.addCategory(Intent.CATEGORY_HOME);
            fallbackIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(fallbackIntent);

        } catch (Exception ignored) {
        }
    }
}
