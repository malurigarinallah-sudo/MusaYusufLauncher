package com.musayusuf.launcher;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ScrollView;

import java.util.List;

public class LauncherHomeView extends FrameLayout {

    public interface Listener {
        void onOpenFolder(LauncherFolder folder);
    }

    private final int pageIndex;
    private final HomeStorage storage;
    private final LauncherAppManager appManager;
    private final LauncherFolderManager folderManager;
    private final GridLayout grid;
    private final Listener listener;

    public LauncherHomeView(
            Context context,
            int pageIndex,
            Listener listener
    ) {
        super(context);

        this.pageIndex = pageIndex;
        this.listener = listener;

        storage = new HomeStorage(context);
        appManager = new LauncherAppManager(context);
        folderManager = new LauncherFolderManager(context);

        ScrollView scroll = new ScrollView(context);

        grid = new GridLayout(context);
        grid.setColumnCount(4);
        grid.setAlignmentMode(GridLayout.ALIGN_MARGINS);
        grid.setUseDefaultMargins(false);
        grid.setPadding(dp(6), dp(10), dp(6), dp(10));

        FrameLayout.LayoutParams gridParams = new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT
        );

        gridParams.gravity = Gravity.TOP;
        gridParams.topMargin = dp(140);

        scroll.addView(grid, gridParams);

        addView(
                scroll,
                new FrameLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT
                )
        );

        loadSavedEntries();
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void addApp(ApplicationInfo appInfo) {

        if (appInfo == null) return;

        storage.addAppEntry(pageIndex, appInfo.packageName);

        refresh();
    }

    private void loadSavedEntries() {

        grid.removeAllViews();

        PackageManager packageManager = getContext().getPackageManager();

        for (String entry : storage.getEntriesForPage(pageIndex)) {

            if (HomeStorage.isAppEntry(entry)) {

                String packageName = HomeStorage.packageFromEntry(entry);

                try {

                    ApplicationInfo appInfo =
                            packageManager.getApplicationInfo(packageName, 0);

                    addAppItem(appInfo);

                } catch (PackageManager.NameNotFoundException ignored) {
                    storage.removeAppEntry(pageIndex, packageName);
                }

            } else if (HomeStorage.isFolderEntry(entry)) {

                String folderId = HomeStorage.folderIdFromEntry(entry);

                LauncherFolder folder =
                        folderManager.getFolderStorage().getFolder(folderId);

                if (folder != null) {
                    addFolderItem(folder);
                } else {
                    storage.removeFolderEntry(pageIndex, folderId);
                }
            }
        }
    }

    private void addAppItem(ApplicationInfo appInfo) {

        LauncherItem item = new LauncherItem(getContext(), appInfo);

        item.setOnClickListener(v -> appManager.launchApp(appInfo));

        item.setOnLongClickListener(v -> {
            showAppOptions(appInfo);
            return true;
        });

        addToGrid(item);
    }

    private void addFolderItem(LauncherFolder folder) {

        FolderIconView folderView = new FolderIconView(getContext());

        folderView.bind(getContext(), folder);

        folderView.setOnClickListener(v -> {
            if (listener != null) listener.onOpenFolder(folder);
        });

        folderView.setOnLongClickListener(v -> {
            showFolderOptions(folder);
            return true;
        });

        addToGrid(folderView);
    }

    private void addToGrid(View view) {

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0;
        params.height = GridLayout.LayoutParams.WRAP_CONTENT;
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
        params.setGravity(Gravity.FILL_HORIZONTAL);
        params.setMargins(dp(4), dp(6), dp(4), dp(6));

        grid.addView(view, params);
    }

    private void showAppOptions(ApplicationInfo appInfo) {

        final List<LauncherFolder> folders = folderManager.listAllFolders();

        String[] baseOptions = {"Open", "Remove from Home", "Move to Folder"};

        new AlertDialog.Builder(getContext())
                .setTitle(appManager.getAppLabel(appInfo))
                .setItems(baseOptions, (dialog, which) -> {

                    if (which == 0) {

                        appManager.launchApp(appInfo);

                    } else if (which == 1) {

                        storage.removeAppEntry(pageIndex, appInfo.packageName);
                        refresh();

                    } else {

                        showMoveToFolderDialog(appInfo, folders);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showMoveToFolderDialog(
            ApplicationInfo appInfo,
            List<LauncherFolder> folders
    ) {

        String[] names = new String[folders.size() + 1];

        for (int i = 0; i < folders.size(); i++) {
            names[i] = folders.get(i).getName();
        }

        names[folders.size()] = "New Folder";

        new AlertDialog.Builder(getContext())
                .setTitle("Move to Folder")
                .setItems(names, (dialog, which) -> {

                    if (which == folders.size()) {

                        folderManager.createFolderWithApp(
                                pageIndex,
                                appInfo.packageName
                        );

                    } else {

                        folderManager.moveAppToFolder(
                                pageIndex,
                                appInfo.packageName,
                                folders.get(which).getId()
                        );
                    }

                    refresh();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showFolderOptions(LauncherFolder folder) {

        String[] options = {"Open", "Delete Folder (keep apps)"};

        new AlertDialog.Builder(getContext())
                .setTitle(folder.getName())
                .setItems(options, (dialog, which) -> {

                    if (which == 0) {

                        if (listener != null) listener.onOpenFolder(folder);

                    } else {

                        folderManager.deleteFolderKeepingApps(pageIndex, folder);
                        refresh();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    public void refresh() {
        loadSavedEntries();
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
