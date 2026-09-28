package com.musayusuf.launcher;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class HomeItemStorage {

    private static final String PREFS_NAME = "home_items";
    private static final String KEY_PREFIX = "page_";
    private static final String SEPARATOR = "|";

    private final SharedPreferences prefs;

    public HomeItemStorage(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public List<String> getPackagesForPage(int pageIndex) {

        String stored = prefs.getString(KEY_PREFIX + pageIndex, "");

        List<String> result = new ArrayList<>();

        if (stored == null || stored.isEmpty()) {
            return result;
        }

        String[] parts = stored.split("\\" + SEPARATOR);

        for (String part : parts) {
            if (part != null && !part.trim().isEmpty()) {
                result.add(part.trim());
            }
        }

        return result;
    }

    public void addPackageToPage(int pageIndex, String packageName) {

        if (packageName == null || packageName.isEmpty()) return;

        Set<String> current = new LinkedHashSet<>(getPackagesForPage(pageIndex));

        current.add(packageName);

        savePage(pageIndex, current);
    }

    public void removePackageFromPage(int pageIndex, String packageName) {

        Set<String> current = new LinkedHashSet<>(getPackagesForPage(pageIndex));

        current.remove(packageName);

        savePage(pageIndex, current);
    }

    private void savePage(int pageIndex, Set<String> packages) {

        StringBuilder builder = new StringBuilder();

        for (String packageName : packages) {

            if (builder.length() > 0) {
                builder.append(SEPARATOR);
            }

            builder.append(packageName);
        }

        prefs.edit()
                .putString(KEY_PREFIX + pageIndex, builder.toString())
                .apply();
    }
}
