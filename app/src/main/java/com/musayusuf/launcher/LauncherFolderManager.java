package com.musayusuf.launcher;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class LauncherFolderManager {

    private final FolderStorage folderStorage;
    private final HomeStorage homeStorage;

    public LauncherFolderManager(Context context) {
        Context appContext = context.getApplicationContext();
        folderStorage = new FolderStorage(appContext);
        homeStorage = new HomeStorage(appContext);
    }

    public LauncherFolder createFolderWithApp(int pageIndex, String packageName) {

        List<String> initial = new ArrayList<>();

        if (packageName != null) initial.add(packageName);

        LauncherFolder folder = folderStorage.createFolder("Folder", initial);

        homeStorage.removeAppEntry(pageIndex, packageName);

        homeStorage.addFolderEntry(pageIndex, folder.getId());

        return folder;
    }

    public void moveAppToFolder(int pageIndex, String packageName, String folderId) {

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

    public List<LauncherFolder> listAllFolders() {
        return folderStorage.listFolders();
    }

    public FolderStorage getFolderStorage() {
        return folderStorage;
    }

    public HomeStorage getHomeStorage() {
        return homeStorage;
    }
}
