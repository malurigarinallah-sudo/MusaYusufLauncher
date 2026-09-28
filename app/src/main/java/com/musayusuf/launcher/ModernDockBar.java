package com.musayusuf.launcher;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class ModernDockBar extends LinearLayout {

    private static final String[] DEFAULT_PACKAGES = {
            "com.android.dialer",
            "com.android.messaging",
            "com.android.camera",
            "com.android.chrome"
    };

    private final LauncherAppManager appManager;

    public ModernDockBar(Context context) {
        super(context);

        appManager = new LauncherAppManager(context);

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);

        GradientDrawable background = new GradientDrawable();

        background.setColor(Color.argb(90, 255, 255, 255));
        background.setCornerRadius(dp(28));

        setBackground(background);

        setPadding(dp(10), dp(6), dp(10), dp(6));

        buildDock();
    }

    private void buildDock() {

        removeAllViews();

        PackageManager packageManager = getContext().getPackageManager();

        List<ApplicationInfo> apps = new ArrayList<>();

        for (String packageName : DEFAULT_PACKAGES) {

            try {
                apps.add(packageManager.getApplicationInfo(packageName, 0));
            } catch (PackageManager.NameNotFoundException ignored) {
            }
        }

        for (ApplicationInfo appInfo : apps) {

            LauncherItem item = new LauncherItem(getContext(), appInfo);

            item.setOnClickListener(v -> appManager.launchApp(appInfo));

            LayoutParams params =
                    new LayoutParams(dp(60), LayoutParams.WRAP_CONTENT);

            params.setMargins(dp(4), 0, dp(4), 0);

            addView(item, params);
        }
    }

    public void refresh() {
        buildDock();
    }

    public void clear() {
        removeAllViews();
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
