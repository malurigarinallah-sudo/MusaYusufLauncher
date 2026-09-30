package com.musayusuf.launcher;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LauncherItem extends LinearLayout {

    private final ApplicationInfo appInfo;
    private final FrameLayout iconContainer;
    private final ImageView iconView;
    private final TextView labelView;

    public LauncherItem(Context context, ApplicationInfo appInfo) {
        super(context);

        this.appInfo = appInfo;

        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);

        setPadding(dp(4), dp(6), dp(4), dp(6));

        iconContainer = new FrameLayout(context);

        iconView = new ImageView(context);
        iconView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        iconContainer.addView(
                iconView,
                new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                )
        );

        addView(iconContainer, new LayoutParams(dp(52), dp(52)));

        applyIconShape(context);

        labelView = new TextView(context);

        labelView.setTextColor(LauncherTheme.textColor(context));
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

    private void applyIconShape(Context context) {

        String shape = new LauncherSettings(context).getIconShape();

        if ("square".equals(shape)) {
            iconContainer.setClipToOutline(false);
            iconContainer.setOutlineProvider(null);
            return;
        }

        final boolean circle = "circle".equals(shape);

        iconContainer.setClipToOutline(true);

        iconContainer.setOutlineProvider(new ViewOutlineProvider() {

            @Override
            public void getOutline(View view, Outline outline) {

                if (circle) {

                    outline.setOval(
                            0, 0,
                            view.getWidth(),
                            view.getHeight()
                    );

                } else {

                    outline.setRoundRect(
                            0, 0,
                            view.getWidth(),
                            view.getHeight(),
                            dp(14)
                    );
                }
            }
        });
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
