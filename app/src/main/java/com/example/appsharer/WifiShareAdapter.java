package com.example.appsharer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class WifiShareAdapter extends RecyclerView.Adapter<WifiShareAdapter.AppViewHolder>  {
    Context context;
    List<App> apps;
    List<App> appShare;
    OnDataClickListener listener;

    public WifiShareAdapter(Context context, List<App> apps, OnDataClickListener listener) {
        this.context = context;
        this.apps = apps;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AppViewHolder(LayoutInflater.from(context).inflate(R.layout.app_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {
        holder.appName.setText(apps.get(position).getAppName());
        holder.appIcon.setImageDrawable(apps.get(position).getIcon());
        holder.appPackage.setText(apps.get(position).getAppPackage());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


if(holder.appName.getCurrentTextColor()==context.getResources().getColor(R.color.teal_200))
{
    holder.appName.setTextColor(context.getResources().getColor(R.color.black));
    listener.onDataClick(apps.get(position),false);

}
    else{
    holder.appName.setTextColor(context.getResources().getColor(R.color.teal_200));
    listener.onDataClick(apps.get(position),true);

            }}
        });
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public static class AppViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView appIcon;
        TextView appName,appPackage;

        public AppViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.app_row);
            appIcon = itemView.findViewById(R.id.app_icon);
            appName = itemView.findViewById(R.id.app_name);
            appPackage=itemView.findViewById(R.id.app_package);
        }
    }
}
