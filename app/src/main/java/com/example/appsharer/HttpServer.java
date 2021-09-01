package com.example.appsharer;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.FileProvider;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.util.Streams;
import org.nanohttpd.fileupload.NanoFileUpload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
//
//import fi.iki.elonen.NanoFileUpload;
import fi.iki.elonen.NanoHTTPD;

/**
 * Created by wangmingxing on 2017/6/20.
 */

public class HttpServer extends NanoHTTPD {
    private static final String TAG = "HttpServer";
    private NanoFileUpload mFileUpload;
    private OnStatusUpdateListener mStatusUpdateListener;
    List<App> appShare;
    List<App> apps = new ArrayList<>();
    String appnames;
    String blabla;
    boolean a=true;
    interface OnStatusUpdateListener {
        void onUploadingProgressUpdate(int progress);
        void onUploadingFile(File file, boolean done);
        void onDownloadingFile(File file, boolean done);
    }

    class DownloadResponse extends Response {
        private File downloadFile;

        DownloadResponse(File downloadFile, InputStream stream) {
            super(Response.Status.OK, "application/octet-stream", stream, downloadFile.length());
            this.downloadFile = downloadFile;
        }

        @Override
        protected void send(OutputStream outputStream) {
            super.send(outputStream);
            if (mStatusUpdateListener != null) {
                mStatusUpdateListener.onDownloadingFile(downloadFile, true);
            }
        }
    }

    public HttpServer(int port,  List<App> appShare) {
        super(port);
        this.appShare = appShare;

        mFileUpload = new NanoFileUpload(new DiskFileItemFactory());
        mFileUpload.setProgressListener(new ProgressListener() {
            int progress = 0;
            @Override
            public void update(long pBytesRead, long pContentLength, int pItems) {
                //Log.d(TAG, pBytesRead + " bytes has been read, totol " + pContentLength + " bytes");
                if (mStatusUpdateListener != null) {
                    int p = (int) (pBytesRead * 100 / pContentLength);
                    if (p != progress) {
                        progress = p;
                        mStatusUpdateListener.onUploadingProgressUpdate(progress);
                    }
                }
            }
        });
    }



    @Override
    public Response serve(IHTTPSession session) {

        String uri = session.getUri();
        Method method = session.getMethod();
        Map<String, String> header = session.getHeaders();
      blabla=header.get("host")+"/"+appnames;
        if(blabla.equals(header.get("referer"))){
            a=false;
        }
        if(!uri.equals("/")&&!uri.equals("/favicon.ico")){
            appnames=uri.replace("/","");
            if(appnames.contains("%20"))
                appnames=appnames.replace("%20", " ");
            blabla=header.get("host")+"/"+appnames;
          //  if(blabla.equals(header.get("referer"))){
                a=false;
           // }
        }
        Map<String, String> parms = session.getParms();
        String answer = "Success!";
        Log.d(TAG, "uri=" + uri);
        Log.d(TAG, "method=" + method);
        Log.d(TAG, "header=" + header);
        Log.d(TAG, "params=" + parms);
//        try {
//            appnames=header.get("referer");
//            if(appnames.length()>2) {
//                String[] parts = appnames.split(":");
//                String[] parts2 = parts[1].split("/");
//                appnames = parts2[1];
//                if(appnames.length()>2){
//                    a = false;
//                if(appnames.contains("%20"))
//                    appnames=appnames.replace("%20", " ");
//
//            }}
//        }
//        catch (Exception e)
//        {
//Log.d(TAG,"Hehheee");
//        }
        if(!uri.equals("/")||!uri.equals("/favicon.ico")){
        appnames=uri.replace("/","");
        if(appnames.contains("%20"))
            appnames=appnames.replace("%20", " ");

        }
        if(blabla.equals(header.get("referer"))){
            a=false;
        }

int arrayC=0;









        if (method.equals(Method.GET)) {
            if (a) {
                answer = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; " +
                        "charset=utf-8\"><title> HTTP File Browser</title>";
                for (App file : appShare) {
                    answer += "<a href=\"" + file.getAppName()
                            + "\" alt = \"\">" + file.getAppName()
                            + "</a><br>";
                }
                answer += "</head></html>";
                if(blabla.equals(header.get("referer"))){
                    a=false;
                }

            } else {
                for (int i = 0; i < appShare.size(); i++) {
                    if (appShare.get(i).getAppName().equals(appnames))
                        arrayC = i;
                }
                answer = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; " +
                        "charset=utf-8\"><title> Download " + appShare.get(arrayC).getAppName() + "</title>";
                // serve file download
                InputStream inputStream;
                Response response = null;

                if (mStatusUpdateListener != null) {
                    mStatusUpdateListener.onDownloadingFile(
                            new File(appShare.get(arrayC).getApkPath()), false);
                }

                try {
                    inputStream = new FileInputStream(

                            new File(appShare.get(arrayC).getApkPath()));
                    response = new DownloadResponse(

                            new File(appShare.get(arrayC).getApkPath()), inputStream);
                } catch (Exception e) {
                    e.printStackTrace();
                }            response.addHeader(
                        "Content-Disposition", "attachment; filename=" + appShare.get(arrayC).getAppName()+".apk");
                return response;


            }


//        if (method.equals(Method.GET)) {
//            // for file browse and download
            //
//            File rootFile = Environment.getExternalStorageDirectory();
//            uri = uri.replace(rootFile.getAbsolutePath(), "");
//            rootFile = new File(rootFile + uri);
//            if (!rootFile.exists()) {
//                return newFixedLengthResponse("Error! No such file or dirctory");
//            }
//
//            if (a) {
//                  answer = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; " +
//                    "charset=utf-8\"><title> HTTP File Browser</title>";
//            for (App file : appShare) {
//                answer += "<a href=\"" + file.getAppName()
//                        + "\" alt = \"\">" + file.getAppName()
//                        + "</a><br>";
//            }
//            answer += "</head></html>";
//a=false;
//
//            } else {
//                // serve file download
//                InputStream inputStream;
//                Response response = null;
//
//                if (mStatusUpdateListener != null) {
//                    mStatusUpdateListener.onDownloadingFile(rootFile, false);
//                }
//
//                try {
//                    inputStream = new FileInputStream(rootFile);
//                    response = new DownloadResponse(rootFile, inputStream);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                if (response != null) {
//                    response.addHeader(
//                            "Content-Disposition", "attachment; filename=" + rootFile.getName());
//                    return response;
//                } else {
//                    return newFixedLengthResponse("Error downloading file!");
//                }
//            }
//        }
        }
        return newFixedLengthResponse(answer);
    }


    public void setOnStatusUpdateListener(OnStatusUpdateListener listener) {
        mStatusUpdateListener = listener;
    }
}