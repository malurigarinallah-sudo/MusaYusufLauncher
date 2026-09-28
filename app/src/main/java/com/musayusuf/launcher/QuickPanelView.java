package com.musayusuf.launcher;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class QuickPanelView extends LinearLayout {

    public interface Listener {
        void onClose();
        void onSettings();
        void onWallpaper();
    }

    private final Listener listener;

    public QuickPanelView(
            Context context,
            Listener listener
    ) {
        super(context);

        this.listener = listener;

        setOrientation(VERTICAL);
        setPadding(
                dp(20),
                dp(20),
                dp(20),
                dp(20)
        );

        setBackgroundColor(
                Color.argb(245, 7, 27, 58)
        );

        build();
    }

    private void build() {

        LinearLayout header =
                new LinearLayout(getContext());

        header.setOrientation(
                HORIZONTAL
        );

        header.setGravity(
                Gravity.CENTER_VERTICAL
        );

        TextView title =
                new TextView(getContext());

        title.setText("Quick Panel");
        title.setTextColor(Color.WHITE);
        title.setTextSize(24);

        header.addView(
                title,
                new LinearLayout.LayoutParams(
                        0,
                        LayoutParams.WRAP_CONTENT,
                        1f
                )
        );

        Button close =
                new Button(getContext());

        close.setText("Close");

        close.setOnClickListener(
                v -> {
                    if (listener != null) {
                        listener.onClose();
                    }
                }
        );

        header.addView(
                close,
                new LinearLayout.LayoutParams(
                        dp(90),
                        dp(50)
                )
        );

        addView(
                header,
                new LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT
                )
        );

        ScrollView scroll =
                new ScrollView(getContext());

        LinearLayout content =
                new LinearLayout(getContext());

        content.setOrientation(
                VERTICAL
        );

        content.setPadding(
                0,
                dp(15),
                0,
                dp(20)
        );

        addAction(
                content,
                "Launcher Settings",
                v -> {
                    if (listener != null) {
                        listener.onSettings();
                    }
                }
        );

        addAction(
                content,
                "Wallpapers",
                v -> {
                    if (listener != null) {
                        listener.onWallpaper();
                    }
                }
        );

        addAction(
                content,
                "System Settings",
                v -> LauncherSystemActions
                        .openSystemSettings(
                                getContext()
                        )
        );

        addAction(
                content,
                "Wi-Fi Settings",
                v -> LauncherSystemActions
                        .openWifiSettings(
                                getContext()
                        )
        );

        addAction(
                content,
                "Bluetooth Settings",
                v -> LauncherSystemActions
                        .openBluetoothSettings(
                                getContext()
                        )
        );

        addAction(
                content,
                "Sound Settings",
                v -> LauncherSystemActions
                        .openSoundSettings(
                                getContext()
                        )
        );

        scroll.addView(
                content,
                new ScrollView.LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT
                )
        );

        addView(
                scroll,
                new LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        0,
                        1f
                )
        );
    }

    private void addAction(
            LinearLayout parent,
            String text,
            OnClickListener listener
    ) {

        Button button =
                new Button(getContext());

        button.setText(text);
        button.setTextSize(15);
        button.setTextColor(Color.WHITE);

        GradientDrawable background =
                new GradientDrawable();

        background.setColor(
                Color.argb(
                        70,
                        255,
                        255,
                        255
                )
        );

        background.setCornerRadius(
                dp(16)
        );

        button.setBackground(
                background
        );

        button.setOnClickListener(
                listener
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        dp(58)
                );

        params.setMargins(
                0,
                dp(5),
                0,
                dp(5)
        );

        parent.addView(
                button,
                params
        );
    }

    public void refresh() {
        requestLayout();
        invalidate();
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
