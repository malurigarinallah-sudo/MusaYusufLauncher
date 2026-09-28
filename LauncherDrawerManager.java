package com.musayusuf.launcher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LauncherDrawerManager {

    private final Context context;
    private final PackageManager packageManager;
    private final LauncherSettings settings;

    public LauncherDrawerManager(Context context) {
        this.context = context.getApplicationContext();
        this.packageManager = this.context.getPackageManager();
        this.settings = new LauncherSettings(this.context);
    }

    public List<ApplicationInfo> getApps(LauncherDrawerState state) {

        List<ApplicationInfo> result = new ArrayList<>();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos =
                packageManager.queryIntentActivities(intent, 0);

        if (resolveInfos == null) {
            return result;
        }

        String query = state == null ? "" : state.getQuery();

        LauncherAppFilter filter =
                state == null ? LauncherAppFilter.ALL : state.getFilter();

        for (ResolveInfo info : resolveInfos) {

            if (info == null || info.activityInfo == null) continue;

            ApplicationInfo appInfo = info.activityInfo.applicationInfo;

            if (appInfo == null) continue;

            if (context.getPackageName().equals(appInfo.packageName)) {
                continue;
            }

            if (settings.isHidden(appInfo.packageName)) {
                continue;
            }

            boolean isSystemApp =
                    (appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;

            if (filter == LauncherAppFilter.USER && isSystemApp) {
                continue;
            }

            if (filter == LauncherAppFilter.SYSTEM && !isSystemApp) {
                continue;
            }

            String label =
                    String.valueOf(appInfo.loadLabel(packageManager));

            if (!LauncherSearchManager.matches(label, query)) {
                continue;
            }

            if (!containsPackage(result, appInfo.packageName)) {
                result.add(appInfo);
            }
        }

        sortApps(result, state == null ? "A-Z" : state.getSortMode());

        return result;
    }

    private boolean containsPackage(
            List<ApplicationInfo> list,
            String packageName
    ) {

        for (ApplicationInfo info : list) {
            if (info.packageName.equals(packageName)) {
                return true;
            }
        }

        return false;
    }

    private void sortApps(
            List<ApplicationInfo> apps,
            String sortMode
    ) {

        final boolean descending = "Z-A".equals(sortMode);

        Collections.sort(
                apps,
                new Comparator<ApplicationInfo>() {

                    @Override
                    public int compare(
                            ApplicationInfo a,
                            ApplicationInfo b
                    ) {

                        String labelA =
                                String.valueOf(a.loadLabel(packageManager));

                        String labelB =
                                String.valueOf(b.loadLabel(packageManager));

                        int result = labelA.compareToIgnoreCase(labelB);

                        return descending ? -result : result;
                    }
                }
        );
    }
}
