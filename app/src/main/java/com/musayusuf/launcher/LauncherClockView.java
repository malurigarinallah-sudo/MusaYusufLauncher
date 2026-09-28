package com.musayusuf.launcher;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LauncherClockView extends LinearLayout {

    private final TextView timeView;
    private final TextView dateView;

    private final Handler handler = new Handler(Looper.getMainLooper());

    private boolean showDate = true;
    private boolean running = false;

    private final Runnable tick = new Runnable() {

        @Override
        public void run() {

            updateTime();

            if (running) {
                handler.postDelayed(this, 1000);
            }
        }
    };

    public LauncherClockView(Context context) {
        super(context);

        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);

        timeView = new TextView(context);
        timeView.setTextColor(Color.WHITE);
        timeView.setTextSize(46);
        timeView.setGravity(Gravity.CENTER);

        dateView = new TextView(context);
        dateView.setTextColor(Color.WHITE);
        dateView.setTextSize(15);
        dateView.setGravity(Gravity.CENTER);
        dateView.setAlpha(0.85f);

        addView(timeView, new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        addView(dateView, new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

        updateTime();
    }

    public void setShowDate(boolean showDate) {

        this.showDate = showDate;

        dateView.setVisibility(showDate ? VISIBLE : GONE);
    }

    private void updateTime() {

        SimpleDateFormat timeFormat =
                new SimpleDateFormat("HH:mm", Locale.getDefault());

        SimpleDateFormat dateFormat =
                new SimpleDateFormat("EEEE, d MMMM", Locale.getDefault());

        Date now = new Date();

        timeView.setText(timeFormat.format(now));
        dateView.setText(dateFormat.format(now));

        dateView.setVisibility(showDate ? VISIBLE : GONE);
    }

    public void start() {

        if (running) return;

        running = true;

        handler.post(tick);
    }

    public void stop() {

        running = false;

        handler.removeCallbacks(tick);
    }
}
