package com.example.appsharer;

import android.graphics.drawable.Drawable;

public class App {
    String AppName, ApkPath;

    Drawable icon;

    public App(String appName, String apkPath, Drawable icon) {
        AppName = appName;
        ApkPath = apkPath;

        this.icon = icon;
    }

    public String getAppName() {
        return AppName;
    }


    public String getApkPath() {
        return ApkPath;
    }


    public Drawable getIcon() {
        return icon;
    }


}
