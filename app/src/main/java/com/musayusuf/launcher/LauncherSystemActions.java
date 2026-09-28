package com.musayusuf.launcher;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.widget.Toast;

public class LauncherSystemActions {

    public static void openSystemSettings(Context context) {
        openAction(context, Settings.ACTION_SETTINGS);
    }

    public static void openWifiSettings(Context context) {
        openAction(context, Settings.ACTION_WIFI_SETTINGS);
    }

    public static void openBluetoothSettings(Context context) {
        openAction(context, Settings.ACTION_BLUETOOTH_SETTINGS);
    }

    public static void openSoundSettings(Context context) {
        openAction(context, Settings.ACTION_SOUND_SETTINGS);
    }

    private static void openAction(Context context, String action) {

        if (context == null || action == null) return;

        try {

            Intent intent = new Intent(action);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        } catch (Exception e) {

            Toast.makeText(
                    context,
                    "Unable to open settings",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}
