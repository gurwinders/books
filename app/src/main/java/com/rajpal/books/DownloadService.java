package com.rajpal.books;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class DownloadService extends IntentService {
    public static final int UPDATE_PROGRESS = 8344;

    private File DESTINATION_PATH;
    private long downloaded = 0;

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String urlToDownload = intent.getStringExtra("url");
        final int id = 1;
        final NotificationManager mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        mBuilder.setContentTitle("Picture Download")
                .setContentText("Download in progress")
                .setTicker("Starts Downloading")
                .setSmallIcon(R.mipmap.ic_download)
                .setLargeIcon(bm);
        startForeground(id, mBuilder.build());
        try {

            URL url = new URL(urlToDownload);
            DESTINATION_PATH = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/hello.mp3");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoInput(true);
            connection.setDoOutput(true);


// Resume download.

//          if(ISSUE_DOWNLOAD_STATUS.intValue()==ECMConstant.ECM_DOWNLOADING){
            File file = new File(DESTINATION_PATH.getAbsolutePath());
            if (file.exists()) {
                downloaded = file.length();
                connection.setRequestProperty("Range", "bytes=" + (file.length()) + "-");
                //   connection.setRequestProperty("If-Range", lastModified);

              //  connection.setRequestProperty("If-Range", strLastModified);

                connection.connect();

            } else {
                connection.setRequestProperty("Range", "bytes=" + downloaded + "-");
                connection.connect();
                // Initial download.
                String lastModified = connection.getHeaderField("Last-Modified");
                Log.d("--lastModi", lastModified);

            }
            Map<String, List<String>> map = connection.getHeaderFields();
            Log.d("AsyncDownloadFile", "header fields: " + map.toString());

            int fileLength = connection.getContentLength();
            // download the file
            InputStream input = new BufferedInputStream(connection.getInputStream());
//            OutputStream output = new FileOutputStream("/sdcard/BarcodeScanner-debug.jpg");

            OutputStream output = new FileOutputStream(DESTINATION_PATH.getAbsolutePath());
            byte data[] = new byte[1024];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;


                output.write(data, 0, count);

                int incr = (int) (total * 100 / fileLength);

                mBuilder.setProgress(100, incr, false);
                // Displays the progress bar for the first time.
                mNotifyManager.notify(id, mBuilder.build());
           //     Log.e("--bytes--", "total:" + total + "incr" + incr);
            }

            output.flush();
            output.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        try {
//            URL url = new URL(urlToDownload);
//            URLConnection connection = url.openConnection();
//            connection.connect();
//            // this will be useful so that you can show a typical 0-100% progress bar
//            int fileLength = connection.getContentLength();
//
//            // download the file
//            InputStream input = new BufferedInputStream(connection.getInputStream());
//            OutputStream output = new FileOutputStream("/sdcard/BarcodeScanner-debug.jpg");
//
//            byte data[] = new byte[1024];
//            long total = 0;
//            int count;
//            while ((count = input.read(data)) != -1) {
//                total += count;
//
//
//                output.write(data, 0, count);
//
//                int incr = (int) (total * 100 / fileLength);
//
//                mBuilder.setProgress(100, incr, false);
//                // Displays the progress bar for the first time.
//                mNotifyManager.notify(id, mBuilder.build());
//
//            }
//
//            output.flush();
//            output.close();
//            input.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        stopForeground(true);
        // When the loop is finished, updates the notification
        mBuilder.setContentText("Download complete")
                // Removes the progress bar

                .setProgress(0, 0, false);
        mNotifyManager.notify(id, mBuilder.build());


    }

//    public void test(){
//        // Setup connection.
//        URL url = new URL(strUrl[0]);
//        URLConnection connection = null;
//        try {
//            connection = url.openConnection();
//
//        downloaded = Integer.parseInt(strUrl[3]);
//        if (downloaded == 0) {
//            connection.connect();
//            strLastModified = connection.getHeaderField("Last-Modified");
//            fileLength = connection.getContentLength();
//            mDownloadFileLength = fileLength;
//        }
//        else {
//            connection.setRequestProperty("Range", "bytes=" + downloaded + "-");
//            connection.setRequestProperty("If-Range", strLastModified);
//            connection.connect();
//            fileLength = mDownloadFileLength;
//            Log.d("AsyncDownloadFile",
//                    "new download seek: " + downloaded +
//                            "; lengthFile: " + fileLength);
//        }
//        map = connection.getHeaderFields();
//        Log.d("AsyncDownloadFile", "header fields: " + map.toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}

////06-23 17:54:14.297  17752-17766/com.rajpal.books D/AsyncDownloadFile﹕ header fields: {null=[HTTP/1.1 206 Partial Content], Accept-Ranges=[bytes], Connection=[close], Content-Length=[4012106], Content-Range=[bytes 2022534-6034639/6034640], Content-Type=[audio/mpeg], Date=[Tue, 23 Jun 2015 12:21:35 GMT], ETag=["ca0033-5c14d0-5188d31513af2"], Last-Modified=[Mon, 15 Jun 2015 11:59:40 GMT], Server=[Apache/2.2.15 (CentOS)], X-Android-Received-Millis=[1435062254289], X-Android-Response-Source=[NETWORK 206], X-Android-Selected-Transport=[http/1.1], X-Android-Sent-Millis=[1435062254098]}
