package com.musayusuf.launcher;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LauncherItem extends LinearLayout {

    private final ApplicationInfo appInfo;
    private final ImageView iconView;
    private final TextView labelView;

    public LauncherItem(Context context, ApplicationInfo appInfo) {
        super(context);

        this.appInfo = appInfo;

        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);

        setPadding(dp(4), dp(6), dp(4), dp(6));

        iconView = new ImageView(context);

        addView(iconView, new LayoutParams(dp(52), dp(52)));

        labelView = new TextView(context);

        labelView.setTextColor(Color.WHITE);
        labelView.setTextSize(11);
        labelView.setGravity(Gravity.CENTER);
        labelView.setMaxLines(1);
        labelView.setEllipsize(TextUtils.TruncateAt.END);

        LayoutParams labelParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );

        labelParams.topMargin = dp(4);

        addView(labelView, labelParams);

        bind(context, appInfo);
    }

    private void bind(Context context, ApplicationInfo appInfo) {

        if (appInfo == null) return;

        PackageManager packageManager = context.getPackageManager();

        try {
            Drawable icon = appInfo.loadIcon(packageManager);
            iconView.setImageDrawable(icon);
        } catch (Exception ignored) {
        }

        labelView.setText(appInfo.loadLabel(packageManager));
    }

    public ApplicationInfo getAppInfo() {
        return appInfo;
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
