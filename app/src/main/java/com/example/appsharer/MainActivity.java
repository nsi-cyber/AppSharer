package com.example.appsharer;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<App> apps = new ArrayList<>();
    RecyclerView recyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerview = findViewById(R.id.app_list);
        PackageManager packageManager = getApplicationContext().getPackageManager();
        List<ApplicationInfo> packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo packageInfo : packages) {
            String name = String.valueOf(packageManager.getApplicationLabel(packageInfo));
            if (name.isEmpty()) {
                name = packageInfo.packageName;
            }
            Drawable icon = packageManager.getApplicationIcon(packageInfo);
            String apkPath = packageInfo.sourceDir;
            apps.add(new App(name, apkPath, icon));
        }

        Collections.sort(apps, new Comparator<App>() {
            @Override
            public int compare(App app, App appx) {
                return app.getAppName().toLowerCase().compareTo(appx.getAppName().toLowerCase());
            }
        });

        LinearLayoutManager LinearLayoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(LinearLayoutManager);
        recyclerview.setHasFixedSize(true);
        AppAdapter appAdapter = new AppAdapter(this, apps);
        recyclerview.setAdapter(appAdapter);

    }
}