package com.musayusuf.launcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;

public class LauncherAppChangeManager {

    public interface Listener {
        void onAppsChanged();
    }

    private final Context context;
    private final Listener listener;

    private final BroadcastReceiver receiver =
            new BroadcastReceiver() {
                @Override
                public void onReceive(
                        Context context,
                        Intent intent
                ) {
                    if (intent == null) return;

                    String action = intent.getAction();

                    if (Intent.ACTION_PACKAGE_ADDED.equals(action)
                            || Intent.ACTION_PACKAGE_REMOVED.equals(action)
                            || Intent.ACTION_PACKAGE_REPLACED.equals(action)
                            || Intent.ACTION_PACKAGE_CHANGED.equals(action)) {

                        if (listener != null) {
                            listener.onAppsChanged();
                        }
                    }
                }
            };

    private boolean registered = false;

    public LauncherAppChangeManager(
            Context context,
            Listener listener
    ) {
        this.context =
                context.getApplicationContext();

        this.listener = listener;
    }

    public void start() {

        if (registered) return;

        IntentFilter filter =
                new IntentFilter();

        filter.addAction(
                Intent.ACTION_PACKAGE_ADDED
        );

        filter.addAction(
                Intent.ACTION_PACKAGE_REMOVED
        );

        filter.addAction(
                Intent.ACTION_PACKAGE_REPLACED
        );

        filter.addAction(
                Intent.ACTION_PACKAGE_CHANGED
        );

        filter.addDataScheme("package");

        try {

            if (Build.VERSION.SDK_INT >= 33) {

                context.registerReceiver(
                        receiver,
                        filter,
                        Context.RECEIVER_NOT_EXPORTED
                );

            } else {

                context.registerReceiver(
                        receiver,
                        filter
                );
            }

            registered = true;

        } catch (Exception ignored) {
            registered = false;
        }
    }

    public void stop() {

        if (!registered) return;

        try {
            context.unregisterReceiver(
                    receiver
            );
        } catch (Exception ignored) {
        }

        registered = false;
    }

    public boolean isRunning() {
        return registered;
    }
}
