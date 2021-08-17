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

public class SystemFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    List<App> apps = new ArrayList<>();
    RecyclerView recyclerview;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SystemFragment() {
    }

    public static SystemFragment newInstance(String param1, String param2) {
        SystemFragment fragment = new SystemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_system, container, false);

        recyclerview = root.findViewById(R.id.app_list);
        PackageManager packageManager = getActivity().getApplicationContext().getPackageManager();
        List<ApplicationInfo> packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo packageInfo : packages) {

            if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                String name = String.valueOf(packageManager.getApplicationLabel(packageInfo));
                if (name.isEmpty()) {
                    name = packageInfo.packageName;
                }
                Drawable icon = packageManager.getApplicationIcon(packageInfo);
                String apkPath = packageInfo.sourceDir;
                apps.add(new App(name, apkPath, icon,packageInfo.packageName));
            }


        }

        Collections.sort(apps, new Comparator<App>() {
            @Override
            public int compare(App app, App appx) {
                return app.getAppName().toLowerCase().compareTo(appx.getAppName().toLowerCase());
            }
        });

        LinearLayoutManager LinearLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerview.setLayoutManager(LinearLayoutManager);
        recyclerview.setHasFixedSize(true);
        AppAdapter appsAdapter = new AppAdapter(this.getContext(), apps);
        recyclerview.setAdapter(appsAdapter);


        return root;
    }
}
