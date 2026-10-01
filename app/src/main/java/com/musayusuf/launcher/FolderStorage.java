package com.musayusuf.launcher;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class FolderStorage {

    private static final String PREFS_NAME = "launcher_folders";
    private static final String SEPARATOR = "|";
    private static final String KEY_INDEX = "folder_index";

    private final SharedPreferences prefs;

    public FolderStorage(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public LauncherFolder createFolder(String name, List<String> initialPackages) {

        String id = "folder_" + System.currentTimeMillis();

        LauncherFolder folder = new LauncherFolder(id, name, new ArrayList<>());

        if (initialPackages != null) {
            for (String packageName : initialPackages) {
                folder.addPackage(packageName);
            }
        }

        saveFolder(folder);

        return folder;
    }

    public LauncherFolder getFolder(String folderId) {

        if (folderId == null) return null;

        String name = prefs.getString(nameKey(folderId), null);

        if (name == null) return null;

        String stored = prefs.getString(appsKey(folderId), "");

        List<String> packages = new ArrayList<>();

        if (!stored.isEmpty()) {

            String[] parts = stored.split("\\" + SEPARATOR);

            for (String part : parts) {
                if (part != null && !part.trim().isEmpty()) {
                    packages.add(part.trim());
                }
            }
        }

        return new LauncherFolder(folderId, name, packages);
    }

    public List<LauncherFolder> listFolders() {

        List<LauncherFolder> result = new ArrayList<>();

        for (String id : getIndex()) {

            LauncherFolder folder = getFolder(id);

            if (folder != null) {
                result.add(folder);
            }
        }

        return result;
    }

    public void saveFolder(LauncherFolder folder) {

        if (folder == null) return;

        StringBuilder builder = new StringBuilder();

        for (String packageName : folder.getPackages()) {

            if (builder.length() > 0) {
                builder.append(SEPARATOR);
            }

            builder.append(packageName);
        }

        prefs.edit()
                .putString(nameKey(folder.getId()), folder.getName())
                .putString(appsKey(folder.getId()), builder.toString())
                .apply();

        addToIndex(folder.getId());
    }

    public void deleteFolder(String folderId) {

        if (folderId == null) return;

        prefs.edit()
                .remove(nameKey(folderId))
                .remove(appsKey(folderId))
                .apply();

        removeFromIndex(folderId);
    }

    private Set<String> getIndex() {

        String stored = prefs.getString(KEY_INDEX, "");

        Set<String> ids = new LinkedHashSet<>();

        if (!stored.isEmpty()) {

            String[] parts = stored.split("\\" + SEPARATOR);

            for (String part : parts) {
                if (part != null && !part.trim().isEmpty()) {
                    ids.add(part.trim());
                }
            }
        }

        return ids;
    }

    private void addToIndex(String id) {

        Set<String> ids = getIndex();

        ids.add(id);

        saveIndex(ids);
    }

    private void removeFromIndex(String id) {

        Set<String> ids = getIndex();

        ids.remove(id);

        saveIndex(ids);
    }

    private void saveIndex(Set<String> ids) {

        StringBuilder builder = new StringBuilder();

        for (String id : ids) {

            if (builder.length() > 0) {
                builder.append(SEPARATOR);
            }

            builder.append(id);
        }

        prefs.edit().putString(KEY_INDEX, builder.toString()).apply();
    }

    private String nameKey(String folderId) {
        return "name_" + folderId;
    }

    private String appsKey(String folderId) {
        return "apps_" + folderId;
    }
}
