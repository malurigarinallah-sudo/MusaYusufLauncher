package com.musayusuf.launcher;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

public class LauncherPageManager {

    private static final int PAGE_COUNT = 2;

    private final List<LauncherHomeView> pages = new ArrayList<>();

    private int currentIndex = 0;

    public LauncherPageManager(Context context) {

        Context appContext = context.getApplicationContext();

        for (int i = 0; i < PAGE_COUNT; i++) {
            pages.add(new LauncherHomeView(appContext, i));
        }
    }

    public void attachCurrentPage(FrameLayout parent) {

        if (parent == null) return;

        parent.removeAllViews();

        LauncherHomeView page = getCurrentPage();

        if (page == null) return;

        View currentParent = (View) page.getParent();

        if (currentParent instanceof FrameLayout) {
            ((FrameLayout) currentParent).removeView(page);
        }

        parent.addView(
                page,
                new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT
                )
        );
    }

    public LauncherHomeView getCurrentPage() {

        if (currentIndex < 0 || currentIndex >= pages.size()) {
            return null;
        }

        return pages.get(currentIndex);
    }

    public boolean nextPage() {

        if (currentIndex >= pages.size() - 1) {
            return false;
        }

        currentIndex++;

        return true;
    }

    public boolean previousPage() {

        if (currentIndex <= 0) {
            return false;
        }

        currentIndex--;

        return true;
    }

    public boolean goToPage(int index) {

        if (index < 0 || index >= pages.size()) {
            return false;
        }

        currentIndex = index;

        return true;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public int getPageCount() {
        return pages.size();
    }

    public void refreshAllPages() {

        for (LauncherHomeView page : pages) {
            page.refresh();
        }
    }
}
