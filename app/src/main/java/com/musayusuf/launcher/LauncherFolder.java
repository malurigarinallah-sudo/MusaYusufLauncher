package com.musayusuf.launcher;

import java.util.ArrayList;
import java.util.List;

public class LauncherFolder {

    private final String id;
    private String name;
    private final List<String> packages;

    public LauncherFolder(String id, String name, List<String> packages) {
        this.id = id;
        this.name = name == null ? "Folder" : name;
        this.packages = packages == null ? new ArrayList<>() : packages;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null || name.trim().isEmpty() ? "Folder" : name.trim();
    }

    public List<String> getPackages() {
        return packages;
    }

    public void addPackage(String packageName) {
        if (packageName != null && !packages.contains(packageName)) {
            packages.add(packageName);
        }
    }

    public void removePackage(String packageName) {
        packages.remove(packageName);
    }

    public boolean isEmpty() {
        return packages.isEmpty();
    }
}
