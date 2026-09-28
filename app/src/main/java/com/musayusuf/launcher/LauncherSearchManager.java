package com.musayusuf.launcher;

public class LauncherSearchManager {

    public static boolean matches(String label, String query) {

        if (query == null || query.trim().isEmpty()) {
            return true;
        }

        if (label == null) {
            return false;
        }

        return label.toLowerCase().contains(
                query.trim().toLowerCase()
        );
    }
}
