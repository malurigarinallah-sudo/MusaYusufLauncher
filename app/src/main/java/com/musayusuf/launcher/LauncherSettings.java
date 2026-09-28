package com.musayusuf.launcher;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class LauncherSettings {

    private static final String PREFS_NAME = "launcher_settings";
    private static final String KEY_DRAWER_COLUMNS = "drawer_columns";
    private static final String KEY_ICON_SHAPE = "icon_shape";
    private static final String KEY_HIDDEN_APPS = "hidden_apps";

    private final SharedPreferences prefs;

    public LauncherSettings(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public int getDrawerColumns() {
        return prefs.getInt(KEY_DRAWER_COLUMNS, 4);
    }

    public void setDrawerColumns(int columns) {
        prefs.edit().putInt(KEY_DRAWER_COLUMNS, columns).apply();
    }

    public String getIconShape() {
        return prefs.getString(KEY_ICON_SHAPE, "rounded");
    }

    public void setIconShape(String shape) {
        prefs.edit().putString(KEY_ICON_SHAPE, shape).apply();
    }

    public Set<String> getHiddenApps() {
        return prefs.getStringSet(KEY_HIDDEN_APPS, new HashSet<>());
    }

    public void setHiddenApps(Set<String> hidden) {
        prefs.edit().putStringSet(KEY_HIDDEN_APPS, hidden).apply();
    }

    public boolean isHidden(String packageName) {
        return getHiddenApps().contains(packageName);
    }

    public void setHidden(String packageName, boolean hidden) {

        Set<String> set = new HashSet<>(getHiddenApps());

        if (hidden) {
            set.add(packageName);
        } else {
            set.remove(packageName);
        }

        setHiddenApps(set);
    }
}
