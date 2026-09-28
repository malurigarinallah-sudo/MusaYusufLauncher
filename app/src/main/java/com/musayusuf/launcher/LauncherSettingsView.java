package com.musayusuf.launcher;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

public class LauncherSettingsView extends LinearLayout {

    public interface Listener {
        void onClose();
        void onWallpaper();
        void onRefresh();
    }

    private interface SwitchListener {
        void onChanged(boolean checked);
    }

    private final Listener listener;
    private final LauncherPreferences preferences;

    public LauncherSettingsView(
            Context context,
            Listener listener
    ) {
        super(context);

        this.listener = listener;

        preferences = new LauncherPreferences(context);

        setOrientation(VERTICAL);

        setPadding(dp(20), dp(20), dp(20), dp(20));

        setBackgroundColor(Color.argb(245, 7, 27, 58));

        build();
    }

    private void build() {

        LinearLayout header = new LinearLayout(getContext());

        header.setOrientation(HORIZONTAL);
        header.setGravity(Gravity.CENTER_VERTICAL);

        TextView title = new TextView(getContext());

        title.setText("Settings");
        title.setTextColor(Color.WHITE);
        title.setTextSize(24);

        header.addView(title, new LayoutParams(
                0, LayoutParams.WRAP_CONTENT, 1f));

        Button close = new Button(getContext());

        close.setText("Close");

        close.setOnClickListener(v -> {
            if (listener != null) listener.onClose();
        });

        header.addView(close, new LayoutParams(dp(90), dp(50)));

        addView(header, new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        ScrollView scroll = new ScrollView(getContext());

        LinearLayout content = new LinearLayout(getContext());
        content.setOrientation(VERTICAL);
        content.setPadding(0, dp(15), 0, dp(20));

        addWallpaperButton(content);

        addSwitchRow(content, "Show Clock", preferences.isShowClock(),
                checked -> {
                    preferences.setShowClock(checked);
                    notifyRefresh();
                });

        addSwitchRow(content, "Show Date", preferences.isShowDate(),
                checked -> {
                    preferences.setShowDate(checked);
                    notifyRefresh();
                });

        addSwitchRow(content, "Show Dock", preferences.isShowDock(),
                checked -> {
                    preferences.setShowDock(checked);
                    notifyRefresh();
                });

        addDimSeekBar(content);

        scroll.addView(content, new ScrollView.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        addView(scroll, new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f));
    }

    private void addWallpaperButton(LinearLayout parent) {

        Button button = new Button(getContext());

        button.setText("Change Wallpaper");

        button.setOnClickListener(v -> {
            if (listener != null) listener.onWallpaper();
        });

        LayoutParams params =
                new LayoutParams(LayoutParams.MATCH_PARENT, dp(55));

        params.setMargins(0, dp(5), 0, dp(5));

        parent.addView(button, params);
    }

    private void addSwitchRow(
            LinearLayout parent,
            String label,
            boolean initialValue,
            SwitchListener listener
    ) {

        LinearLayout row = new LinearLayout(getContext());
        row.setOrientation(HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(dp(4), dp(10), dp(4), dp(10));

        TextView text = new TextView(getContext());
        text.setText(label);
        text.setTextColor(Color.WHITE);
        text.setTextSize(16);

        row.addView(text, new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f));

        Switch toggle = new Switch(getContext());
        toggle.setChecked(initialValue);

        toggle.setOnCheckedChangeListener(
                (buttonView, isChecked) -> listener.onChanged(isChecked)
        );

        row.addView(toggle, new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        parent.addView(row, new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    private void addDimSeekBar(LinearLayout parent) {

        TextView label = new TextView(getContext());
        label.setText("Wallpaper Dim");
        label.setTextColor(Color.WHITE);
        label.setTextSize(16);
        label.setPadding(dp(4), dp(10), dp(4), dp(4));

        parent.addView(label, new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        SeekBar seekBar = new SeekBar(getContext());
        seekBar.setMax(100);
        seekBar.setProgress((int) (preferences.getWallpaperDim() * 100));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(
                    SeekBar seekBar,
                    int progress,
                    boolean fromUser
            ) {

                if (fromUser) {
                    preferences.setWallpaperDim(progress / 100f);
                    notifyRefresh();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        parent.addView(seekBar, new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    private void notifyRefresh() {

        if (listener != null) {
            listener.onRefresh();
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
