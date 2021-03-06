package com.example.appsharer;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class UserFragment extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    List<App> appu = new ArrayList<>();
    RecyclerView recyclerview;
    // TODO: Rename and change types of parameters


    public UserFragment() {
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_applist, container, false);

        recyclerview = root.findViewById(R.id.app_list);
        PackageManager packageManager = getActivity().getApplicationContext().getPackageManager();
        List<ApplicationInfo> packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo packageInfo : packages) {
            if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                String name = String.valueOf(packageManager.getApplicationLabel(packageInfo));
                if (name.isEmpty()) {
                    name = packageInfo.packageName;
                }
                Drawable icon = packageManager.getApplicationIcon(packageInfo);
                String apkPath = packageInfo.sourceDir;
                appu.add(new App(name, apkPath, icon, packageInfo.packageName));
            }
        }

        Collections.sort(appu, new Comparator<App>() {
            @Override
            public int compare(App app, App appx) {
                return app.getAppName().toLowerCase().compareTo(appx.getAppName().toLowerCase());
            }
        });

        LinearLayoutManager LinearLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerview.setLayoutManager(LinearLayoutManager);
        recyclerview.setHasFixedSize(true);
        AppAdapter appsAdapter = new AppAdapter(this.getContext(), appu);
        recyclerview.setAdapter(appsAdapter);


        return root;
    }
}



