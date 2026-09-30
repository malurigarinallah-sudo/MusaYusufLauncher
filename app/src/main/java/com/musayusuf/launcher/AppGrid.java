package com.musayusuf.launcher;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.view.Gravity;
import android.widget.GridLayout;

public class AppGrid extends GridLayout {

    public interface LongPressListener {
        void onLongPress(ApplicationInfo appInfo, LauncherItem item);
    }

    private final LauncherAppManager appManager;

    private LongPressListener longPressListener;

    public AppGrid(Context context) {
        super(context);

        appManager = new LauncherAppManager(context);

        setColumnCount(4);
        setAlignmentMode(ALIGN_MARGINS);
        setUseDefaultMargins(false);

        setPadding(dp(6), dp(10), dp(6), dp(10));
    }

    public void setLongPressListener(LongPressListener listener) {
        this.longPressListener = listener;
    }

    public void setColumns(int columns) {
        setColumnCount(Math.max(3, Math.min(6, columns)));
    }

    public void addApp(ApplicationInfo appInfo) {

        if (appInfo == null) return;

        final LauncherItem item = new LauncherItem(getContext(), appInfo);

        item.setOnClickListener(v -> appManager.launchApp(appInfo));

        item.setOnLongClickListener(v -> {

            if (longPressListener != null) {
                longPressListener.onLongPress(appInfo, item);
            }

            return true;
        });

        LayoutParams params = new LayoutParams();

        params.width = 0;
        params.height = LayoutParams.WRAP_CONTENT;
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.setGravity(Gravity.FILL_HORIZONTAL);
        params.setMargins(dp(4), dp(6), dp(4), dp(6));

        addView(item, params);
    }

    public void clearApps() {
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
