package com.musayusuf.launcher;

public class LauncherDrawerState {

    private String query = "";
    private String sortMode = "A-Z";
    private LauncherAppFilter filter = LauncherAppFilter.ALL;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query == null ? "" : query;
    }

    public String getSortMode() {
        return sortMode;
    }

    public void setSortMode(String sortMode) {
        this.sortMode = sortMode == null ? "A-Z" : sortMode;
    }

    public LauncherAppFilter getFilter() {
        return filter;
    }

    public void setFilter(LauncherAppFilter filter) {
        this.filter = filter == null ? LauncherAppFilter.ALL : filter;
    }
}
