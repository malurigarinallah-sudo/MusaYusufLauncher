package com.musayusuf.launcher;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class LauncherFolderManager {

    private final Context context;
    private final FolderStorage folderStorage;
    private final HomeStorage homeStorage;

    public LauncherFolderManager(Context context) {
        this.context = context.getApplicationContext();
        folderStorage = new FolderStorage(this.context);
        homeStorage = new HomeStorage(this.context);
    }

    public LauncherFolder createFolderFromApps(
            int pageIndex,
            String firstPackage,
            String secondPackage
    ) {

        List<String> initial = new ArrayList<>();

        if (firstPackage != null) initial.add(firstPackage);
        if (secondPackage != null) initial.add(secondPackage);

        LauncherFolder folder = folderStorage.createFolder("Folder", initial);

        homeStorage.removeAppEntry(pageIndex, firstPackage);
        homeStorage.removeAppEntry(pageIndex, secondPackage);

        homeStorage.addFolderEntry(pageIndex, folder.getId());

        return folder;
    }

    public void addAppToFolder(String folderId, int pageIndex, String packageName) {

        LauncherFolder folder = folderStorage.getFolder(folderId);

        if (folder == null) return;

        folder.addPackage(packageName);

        folderStorage.saveFolder(folder);

        homeStorage.removeAppEntry(pageIndex, packageName);
    }

    public void deleteFolderKeepingApps(int pageIndex, LauncherFolder folder) {

        if (folder == null) return;

        for (String packageName : folder.getPackages()) {
            homeStorage.addAppEntry(pageIndex, packageName);
        }

        homeStorage.removeFolderEntry(pageIndex, folder.getId());

        folderStorage.deleteFolder(folder.getId());
    }

    public FolderStorage getFolderStorage() {
        return folderStorage;
    }

    public HomeStorage getHomeStorage() {
        return homeStorage;
    }
}
