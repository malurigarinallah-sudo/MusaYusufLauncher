package com.musayusuf.launcher;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class FolderIconView extends LinearLayout {

    private final GridLayout previewGrid;
    private final TextView labelView;

    public FolderIconView(Context context) {
        super(context);

        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        setPadding(dp(4), dp(6), dp(4), dp(6));

        GradientDrawable background = new GradientDrawable();
        background.setColor(LauncherTheme.buttonBackground(context));
        background.setCornerRadius(dp(16));

        previewGrid = new GridLayout(context);
        previewGrid.setColumnCount(2);
        previewGrid.setRowCount(2);
        previewGrid.setBackground(background);
        previewGrid.setPadding(dp(6), dp(6), dp(6), dp(6));

        addView(previewGrid, new LayoutParams(dp(52), dp(52)));

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
    }

    public void bind(Context context, LauncherFolder folder) {

        if (folder == null) return;

        labelView.setText(folder.getName());

        previewGrid.removeAllViews();

        PackageManager packageManager = context.getPackageManager();

        List<String> packages = folder.getPackages();

        int count = Math.min(4, packages.size());

        for (int i = 0; i < count; i++) {

            ImageView iconView = new ImageView(context);

            try {

                ApplicationInfo appInfo =
                        packageManager.getApplicationInfo(packages.get(i), 0);

                Drawable icon = appInfo.loadIcon(packageManager);

                iconView.setImageDrawable(icon);

            } catch (Exception ignored) {
            }

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();

            params.width = dp(18);
            params.height = dp(18);

            params.setMargins(dp(2), dp(2), dp(2), dp(2));

            previewGrid.addView(iconView, params);
        }
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
