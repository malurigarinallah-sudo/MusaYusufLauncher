package com.musayusuf.launcher;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class FolderContentsView extends LinearLayout {

    public interface Listener {
        void onClose();
        void onChanged();
    }

    private final LauncherFolder folder;
    private final FolderStorage folderStorage;
    private final LauncherAppManager appManager;
    private final Listener listener;

    private TextView titleView;
    private GridLayout grid;

    public FolderContentsView(
            Context context,
            LauncherFolder folder,
            Listener listener
    ) {
        super(context);

        this.folder = folder;
        this.listener = listener;

        folderStorage = new FolderStorage(context);
        appManager = new LauncherAppManager(context);

        setOrientation(VERTICAL);
        setPadding(dp(20), dp(20), dp(20), dp(20));
        setBackgroundColor(LauncherTheme.panelBackground(context));

        build();
    }

    private void build() {

        LinearLayout header = new LinearLayout(getContext());
        header.setOrientation(HORIZONTAL);
        header.setGravity(Gravity.CENTER_VERTICAL);

        titleView = new TextView(getContext());
        titleView.setText(folder.getName());
        titleView.setTextColor(LauncherTheme.textColor(getContext()));
        titleView.setTextSize(22);

        titleView.setOnClickListener(v -> showRenameDialog());

        header.addView(titleView, new LayoutParams(
                0, LayoutParams.WRAP_CONTENT, 1f));

        Button close = new Button(getContext());
        close.setText("Close");

        close.setOnClickListener(v -> {
            if (listener != null) listener.onClose();
        });

        header.addView(close, new LayoutParams(dp(90), dp(50)));

        addView(header, new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        TextView hint = new TextView(getContext());
        hint.setText("Tap name to rename. Long-press an app to remove it.");
        hint.setTextColor(LauncherTheme.secondaryTextColor(getContext()));
        hint.setTextSize(12);
        hint.setPadding(0, dp(6), 0, dp(10));

        addView(hint, new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        ScrollView scroll = new ScrollView(getContext());

        grid = new GridLayout(getContext());
        grid.setColumnCount(4);
        grid.setAlignmentMode(GridLayout.ALIGN_MARGINS);
        grid.setUseDefaultMargins(false);

        refreshGrid();

        scroll.addView(grid, new ScrollView.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        addView(scroll, new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f));
    }

    private void refreshGrid() {

        grid.removeAllViews();

        PackageManager packageManager = getContext().getPackageManager();

        for (String packageName : folder.getPackages()) {

            ApplicationInfo appInfo;

            try {
                appInfo = packageManager.getApplicationInfo(packageName, 0);
            } catch (PackageManager.NameNotFoundException e) {
                continue;
            }

            LauncherItem item = new LauncherItem(getContext(), appInfo);

            item.setOnClickListener(v -> appManager.launchApp(appInfo));

            item.setOnLongClickListener(v -> {

                folder.removePackage(packageName);
                folderStorage.saveFolder(folder);

                refreshGrid();

                if (listener != null) listener.onChanged();

                return true;
            });

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.setGravity(Gravity.FILL_HORIZONTAL);
            params.setMargins(dp(4), dp(5), dp(4), dp(5));

            grid.addView(item, params);
        }
    }

    private void showRenameDialog() {

        final EditText input = new EditText(getContext());
        input.setText(folder.getName());

        new AlertDialog.Builder(getContext())
                .setTitle("Rename Folder")
                .setView(input)
                .setPositiveButton("Save", (dialog, which) -> {

                    folder.setName(input.getText().toString());
                    folderStorage.saveFolder(folder);

                    titleView.setText(folder.getName());

                    if (listener != null) listener.onChanged();
                })
                .setNegativeButton("Cancel", null)
                .show();
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
