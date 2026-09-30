package com.musayusuf.launcher;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class HomeStorage {

    private static final String PREFS_NAME = "home_items";
    private static final String KEY_PREFIX = "page_";
    private static final String SEPARATOR = "|";

    private static final String APP_PREFIX = "app:";
    private static final String FOLDER_PREFIX = "folder:";

    private final SharedPreferences prefs;

    public HomeStorage(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public List<String> getEntriesForPage(int pageIndex) {

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

    public void addAppEntry(int pageIndex, String packageName) {

        if (packageName == null || packageName.isEmpty()) return;

        addEntry(pageIndex, APP_PREFIX + packageName);
    }

    public void removeAppEntry(int pageIndex, String packageName) {

        if (packageName == null) return;

        removeEntry(pageIndex, APP_PREFIX + packageName);
    }

    public void addFolderEntry(int pageIndex, String folderId) {

        if (folderId == null || folderId.isEmpty()) return;

        addEntry(pageIndex, FOLDER_PREFIX + folderId);
    }

    public void removeFolderEntry(int pageIndex, String folderId) {

        if (folderId == null) return;

        removeEntry(pageIndex, FOLDER_PREFIX + folderId);
    }

    public static boolean isAppEntry(String entry) {
        return entry != null && entry.startsWith(APP_PREFIX);
    }

    public static boolean isFolderEntry(String entry) {
        return entry != null && entry.startsWith(FOLDER_PREFIX);
    }

    public static String packageFromEntry(String entry) {
        return entry.substring(APP_PREFIX.length());
    }

    public static String folderIdFromEntry(String entry) {
        return entry.substring(FOLDER_PREFIX.length());
    }

    private void addEntry(int pageIndex, String entry) {

        Set<String> current = new LinkedHashSet<>(getEntriesForPage(pageIndex));

        current.add(entry);

        saveEntries(pageIndex, current);
    }

    private void removeEntry(int pageIndex, String entry) {

        Set<String> current = new LinkedHashSet<>(getEntriesForPage(pageIndex));

        current.remove(entry);

        saveEntries(pageIndex, current);
    }

    private void saveEntries(int pageIndex, Set<String> entries) {

        StringBuilder builder = new StringBuilder();

        for (String entry : entries) {

            if (builder.length() > 0) {
                builder.append(SEPARATOR);
            }

            builder.append(entry);
        }

        prefs.edit()
                .putString(KEY_PREFIX + pageIndex, builder.toString())
                .apply();
    }
}
