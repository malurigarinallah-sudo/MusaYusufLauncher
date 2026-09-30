package com.musayusuf.launcher;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public class AppDrawer extends LinearLayout {

    public interface Listener {
        void onClose();

        void onAddToHome(
                ApplicationInfo app
        );

        void onAddToDock(
                ApplicationInfo app
        );
    }

    private final LauncherAppManager appManager;
    private final LauncherDrawerManager drawerManager;
    private final LauncherDrawerState drawerState;
    private final LauncherSettings settings;
    private final Listener listener;

    private EditText searchBox;
    private Spinner sortSpinner;
    private Spinner filterSpinner;
    private GridLayout appGrid;
    private TextView countText;

    public AppDrawer(
            Context context,
            Listener listener
    ) {
        super(context);

        this.listener = listener;

        appManager =
                new LauncherAppManager(context);

        drawerManager =
                new LauncherDrawerManager(context);

        drawerState =
                new LauncherDrawerState();

        settings =
                new LauncherSettings(context);

        setOrientation(VERTICAL);

        setPadding(
                dp(14),
                dp(14),
                dp(14),
                dp(14)
        );

        setBackgroundColor(
                LauncherTheme.panelBackground(context)
        );

        build();

        refreshApps();
    }

    private void build() {

        LinearLayout topBar =
                new LinearLayout(getContext());

        topBar.setOrientation(
                HORIZONTAL
        );

        topBar.setGravity(
                Gravity.CENTER_VERTICAL
        );

        TextView title =
                new TextView(getContext());

        title.setText("All Apps");
        title.setTextColor(LauncherTheme.textColor(getContext()));
        title.setTextSize(22);

        topBar.addView(
                title,
                new LinearLayout.LayoutParams(
                        0,
                        LayoutParams.WRAP_CONTENT,
                        1f
                )
        );

        Button close =
                new Button(getContext());

        close.setText("Close");
        close.setTextColor(LauncherTheme.textColor(getContext()));

        close.setOnClickListener(
                v -> {

                    if (listener != null) {
                        listener.onClose();
                    }
                }
        );

        topBar.addView(
                close,
                new LinearLayout.LayoutParams(
                        dp(90),
                        dp(50)
                )
        );

        addView(
                topBar,
                new LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT
                )
        );

        searchBox =
                new EditText(getContext());

        searchBox.setHint(
                "Search apps..."
        );

        searchBox.setHintTextColor(
                LauncherTheme.secondaryTextColor(getContext())
        );

        searchBox.setTextColor(
                LauncherTheme.textColor(getContext())
        );

        searchBox.setSingleLine(true);

        GradientDrawable searchBackground =
                new GradientDrawable();

        searchBackground.setColor(
                LauncherTheme.searchBoxBackground(getContext())
        );

        searchBackground.setCornerRadius(
                dp(18)
        );

        searchBox.setBackground(
                searchBackground
        );

        searchBox.setPadding(
                dp(16),
                0,
                dp(16),
                0
        );

        searchBox.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence s,
                            int start,
                            int count,
                            int after
                    ) {
                    }

                    @Override
                    public void onTextChanged(
                            CharSequence s,
                            int start,
                            int before,
                            int count
                    ) {

                        drawerState.setQuery(
                                s == null
                                        ? ""
                                        : s.toString()
                        );

                        refreshApps();
                    }

                    @Override
                    public void afterTextChanged(
                            Editable s
                    ) {
                    }
                }
        );

        addView(
                searchBox,
                new LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        dp(52)
                )
        );

        LinearLayout filters =
                new LinearLayout(getContext());

        filters.setOrientation(
                HORIZONTAL
        );

        filters.setGravity(
                Gravity.CENTER_VERTICAL
        );

        sortSpinner =
                createSpinner(
                        new String[]{
                                "A-Z",
                                "Z-A"
                        }
                );

        filterSpinner =
                createSpinner(
                        new String[]{
                                "All",
                                "User Apps",
                                "System Apps"
                        }
                );

        filters.addView(
                sortSpinner,
                new LinearLayout.LayoutParams(
                        0,
                        dp(50),
                        1f
                )
        );

        filters.addView(
                filterSpinner,
                new LinearLayout.LayoutParams(
                        0,
                        dp(50),
                        1f
                )
        );

        sortSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            AdapterView<?> parent,
                            View view,
                            int position,
                            long id
                    ) {

                        if (position == 0) {
                            drawerState.setSortMode(
                                    "A-Z"
                            );
                        } else {
                            drawerState.setSortMode(
                                    "Z-A"
                            );
                        }

                        refreshApps();
                    }

                    @Override
                    public void onNothingSelected(
                            AdapterView<?> parent
                    ) {
                    }
                }
        );

        filterSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            AdapterView<?> parent,
                            View view,
                            int position,
                            long id
                    ) {

                        if (position == 0) {

                            drawerState.setFilter(
                                    LauncherAppFilter.ALL
                            );

                        } else if (position == 1) {

                            drawerState.setFilter(
                                    LauncherAppFilter.USER
                            );

                        } else {

                            drawerState.setFilter(
                                    LauncherAppFilter.SYSTEM
                            );
                        }

                        refreshApps();
                    }

                    @Override
                    public void onNothingSelected(
                            AdapterView<?> parent
                    ) {
                    }
                }
        );

        addView(
                filters,
                new LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        dp(52)
                )
        );

        countText =
                new TextView(getContext());

        countText.setTextColor(
                LauncherTheme.secondaryTextColor(getContext())
        );

        countText.setTextSize(13);

        countText.setPadding(
                dp(6),
                dp(4),
                dp(6),
                dp(4)
        );

        addView(
                countText,
                new LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        dp(32)
                )
        );

        ScrollView scroll =
                new ScrollView(getContext());

        appGrid =
                new GridLayout(getContext());

        appGrid.setColumnCount(
                settings.getDrawerColumns()
        );

        appGrid.setAlignmentMode(
                GridLayout.ALIGN_MARGINS
        );

        appGrid.setUseDefaultMargins(
                false
        );

        appGrid.setPadding(
                dp(4),
                dp(8),
                dp(4),
                dp(80)
        );

        scroll.addView(
                appGrid,
                new ScrollView.LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT
                )
        );

        addView(
                scroll,
                new LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        0,
                        1f
                )
        );
    }

    private Spinner createSpinner(
            String[] items
    ) {

        Spinner spinner =
                new Spinner(getContext());

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getContext(),
                        android.R.layout.simple_spinner_item,
                        items
                );

        adapter.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item
        );

        spinner.setAdapter(adapter);

        return spinner;
    }

    private void refreshApps() {

        if (appGrid == null) return;

        appGrid.removeAllViews();

        List<ApplicationInfo> apps =
                drawerManager.getApps(
                        drawerState
                );

        if (apps == null) {

            countText.setText(
                    "0 apps"
            );

            return;
        }

        countText.setText(
                apps.size() + " apps"
        );

        int columns =
                Math.max(
                        3,
                        Math.min(
                                6,
                                settings.getDrawerColumns()
                        )
                );

        appGrid.setColumnCount(
                columns
        );

        for (ApplicationInfo app : apps) {

            if (app == null) continue;

            addAppItem(app);
        }
    }

    private void addAppItem(
            ApplicationInfo app
    ) {

        LauncherItem item =
                new LauncherItem(
                        getContext(),
                        app
                );

        item.setOnClickListener(
                v -> appManager.launchApp(app)
        );

        item.setOnLongClickListener(
                v -> {

                    showAppOptions(app);

                    return true;
                }
        );

        GridLayout.LayoutParams params =
                new GridLayout.LayoutParams();

        params.width = 0;

        params.height =
                GridLayout.LayoutParams.WRAP_CONTENT;

        params.columnSpec =
                GridLayout.spec(
                        GridLayout.UNDEFINED,
                        1f
                );

        params.setGravity(
                Gravity.FILL_HORIZONTAL
        );

        params.setMargins(
                dp(4),
                dp(5),
                dp(4),
                dp(5)
        );

        appGrid.addView(
                item,
                params
        );
    }

    private void showAppOptions(
            ApplicationInfo app
    ) {

        String[] options = {
                "Open",
                "Add to Home",
                "Add to Dock",
                "App Info",
                "Uninstall"
        };

        new android.app.AlertDialog.Builder(
                getContext()
        )
                .setTitle(
                        appManager.getAppLabel(app)
                )
                .setItems(
                        options,
                        (dialog, which) -> {

                            switch (which) {

                                case 0:
                                    appManager.launchApp(
                                            app
                                    );
                                    break;

                                case 1:

                                    if (listener != null) {
                                        listener.onAddToHome(
                                                app
                                        );
                                    }

                                    break;

                                case 2:

                                    if (listener != null) {
                                        listener.onAddToDock(
                                                app
                                        );
                                    }

                                    break;

                                case 3:

                                    appManager.openAppInfo(
                                            app
                                    );

                                    break;

                                case 4:

                                    appManager.uninstallApp(
                                            app
                                    );

                                    break;
                            }
                        }
                )
                .setNegativeButton(
                        "Cancel",
                        null
                )
                .show();
    }

    public void refresh() {
        refreshApps();
    }

    public void clearSearch() {

        if (searchBox != null) {
            searchBox.setText("");
        }

        drawerState.setQuery("");

        refreshApps();
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
