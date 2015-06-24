package com.rajpal.books;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Message;
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
import java.util.Timer;

public class SomeService1 extends AbstractService {
    public static final int MSG_INCREMENT = 1;
    public static final int MSG_COUNTER = 2;

    private NotificationManager nm;
    private Timer timer = new Timer();
    private int counter = 0, incrementby = 1;

    public static final int UPDATE_PROGRESS = 8344;

    private File DESTINATION_PATH;
    private long downloaded = 0;
    HttpURLConnection connection;
    SharedPreferences sp;
    final int id = 1;
    NotificationManager mNotifyManager;
    NotificationCompat.Builder mBuilder;
    private Context mContext = this;

    @Override
    public void onStartService() {
        //   showNotification();
        ///   timer.scheduleAtFixedRate(new TimerTask(){ public void run() {onTimerTick();}}, 0, 250L);

        new downloadTask().execute();
    }

    @Override
    public void onStopService() {
//        if (timer != null) {timer.cancel();}
//        counter=0;
//        nm.cancel(getClass().getSimpleName().hashCode());
//        Log.i("MyService", "Service Stopped.");
    }

    @Override
    public void onReceiveMessage(Message msg) {
        if (msg.what == MSG_INCREMENT) {
            //   incrementby = msg.arg1;
            Log.e("---message---", "message received");
            connection.disconnect();
            stopForeground(true);
            mBuilder.setContentText("Downloading stoped")
                    // Removes the progress bar
                    .setProgress(0, 0, false).setTicker("stop Downloading...");
            mNotifyManager.notify(id, mBuilder.build());

        }
    }

//    private void showNotification() {
//     nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//
//        // In this sample, we'll use the same text for the ticker and the expanded notification
//        // Set the icon, scrolling text and timestamp
//        // The PendingIntent to launch our activity if the user selects this notification
//        // Set the info for the views that show in the notification panel.
//
////        String text = getString(R.string.service_started, getClass().getSimpleName());
//        String text = "service_started = SomeService1";
//        Notification notification = new Notification(R.mipmap.ic_launcher, text, System.currentTimeMillis());
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, ExampleActivity.class), 0);
//        notification.setLatestEventInfo(this, getClass().getSimpleName(), text, contentIntent);
//
//        // Send the notification.
//        // We use a layout id because it is a unique number.  We use it later to cancel.
//
//        nm.notify(getClass().getSimpleName().hashCode(), notification);
//    }

    private void onTimerTick() {
        //Log.i("TimerTick", "Timer doing work." + counter);

        try {
            counter += incrementby;

            // Send data as simple integer
            send(Message.obtain(null, MSG_COUNTER, counter, 0));
        } catch (Throwable t) { //you should always ultimately catch all exceptions in timer tasks.
            Log.e("TimerTick", "Timer Tick Failed.", t);
        }
    }


    class downloadTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            sp = getSharedPreferences("data", MODE_MULTI_PROCESS);
            mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder = new NotificationCompat.Builder(mContext);
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            mBuilder.setContentTitle("Picture Download")
                    .setContentText("Download in progress")
                    .setTicker("Starts Downloading")
                    .setSmallIcon(R.mipmap.ic_download)
                    .setLargeIcon(bm);
            startForeground(id, mBuilder.build());
        }

        @Override
        protected Void doInBackground(Void... params) {


            try {
                String urlToDownload = "http://s320.ve.vc/data/320/33447/273523/Bad_Baby_-_Gippy_Grewal_-_320Kbps_-_www.DjPunjab.Com.mp3";// intent.getStringExtra("url");

                URL url = new URL(urlToDownload);
                DESTINATION_PATH = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/hello.mp3");
                connection = (HttpURLConnection) url.openConnection();

                connection.setDoOutput(true);


                File file = new File(DESTINATION_PATH.getAbsolutePath());
                if (file.exists()) {
                    String lastModified = sp.getString("last", null);
                    downloaded = file.length();
                    connection.setRequestProperty("Range", "bytes=" + (file.length()) + "-");
                    connection.setRequestProperty("If-Range", lastModified);
                    connection.connect();
                } else {
                    connection.setRequestProperty("Range", "bytes=" + downloaded + "-");
                    connection.connect();

                    String lastModified = connection.getHeaderField("Last-Modified");
                    Log.d("--lastModi", lastModified);


                    SharedPreferences.Editor e = sp.edit();
                    e.putString("last", lastModified);
                    e.commit();
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


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            stopForeground(true);
            // When the loop is finished, updates the notification
            mBuilder.setContentText("Download complete")
                    // Removes the progress bar
                    .setProgress(0, 0, false).setTicker("Downloading completes");
            mNotifyManager.notify(id, mBuilder.build());
            super.onPostExecute(aVoid);
        }
    }
}