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

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AppViewHolder> {
    Context context;
    List<App> apps;

    public AppAdapter(Context context, List<App> apps) {
        this.context = context;
        this.apps = apps;
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
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(context
                        , BuildConfig.APPLICATION_ID + ".provider", new File(apps.get(position).getApkPath())));
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                shareIntent.setType("*/*");
                context.startActivity(Intent.createChooser(shareIntent, "Share the app with"));
            }
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
        TextView appName;

        public AppViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.app_row);
            appIcon = itemView.findViewById(R.id.app_icon);
            appName = itemView.findViewById(R.id.app_name);
        }
    }
}
