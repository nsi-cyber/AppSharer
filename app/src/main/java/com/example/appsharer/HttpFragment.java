package com.example.appsharer;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
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

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

import static android.content.Context.WIFI_SERVICE;

public class HttpFragment extends Fragment implements OnDataClickListener{
    private static final String TAG = "MainActivity";
    private HttpServer mHttpd;
    private TextView  mDownloadInfo;
    List<App> appShare= new ArrayList<>();
    Button buttonGo,buttonAll;
    RecyclerView recyclerview;
    List<App> appu = new ArrayList<>();
    OnDataClickListener p;
    // TODO: Rename and change types of parameters


    public HttpFragment() {
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
        View root = inflater.inflate(R.layout.fragment_http, container, false);

        TextView tv = (TextView) root.findViewById(R.id.server_info);
        StringBuilder serverInfo = new StringBuilder().append("HTTP Sever Address: http://").
                append(getWifiIpAddress()).append(":8082\n");
        tv.setText(serverInfo);




        Button b=root.findViewById(R.id.button2);
        Button b2=root.findViewById(R.id.button);
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
        WifiShareAdapter appsAdapter = new WifiShareAdapter(this.getContext(), appu,this);
        recyclerview.setAdapter(appsAdapter);

        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mHttpd = new HttpServer(8082,appu);
                try {
                    mHttpd.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(b.getText().equals("Start server")){
                    b.setText("Disconnect server");
                mHttpd = new HttpServer(8082,appShare);
                try {
                    mHttpd.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                System.exit(1);
                }
            }
        });


        return root;
    }
    public String getWifiIpAddress() {
        WifiManager wifiManager = (WifiManager) getActivity().getSystemService(WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        return android.text.format.Formatter.formatIpAddress(info.getIpAddress());
    }

    @Override
    public void onDataClick(App data,boolean h) {
        if(h)
            appShare.add(data);
    else
            appShare.remove(data);
            }
    }

