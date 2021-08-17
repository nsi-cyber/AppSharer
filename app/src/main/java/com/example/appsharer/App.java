package com.example.appsharer;

import android.graphics.drawable.Drawable;

public class App {
    String AppName, ApkPath,AppPackage;

    Drawable icon;



    public App(String appName, String apkPath, Drawable icon, String appPackage) {
        AppName = appName;
        ApkPath = apkPath;
AppPackage=appPackage;
        this.icon = icon;
    }

    public String getAppPackage() {
        return AppPackage;
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
