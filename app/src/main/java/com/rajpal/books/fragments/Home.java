package com.rajpal.books.fragments;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rajpal.books.DownloadService;
import com.rajpal.books.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by asus on 22-06-15.
 */
public class Home extends Fragment {


    private Button start;
    private Button pause;
//    private ServiceManager service;
    private File DESTINATION_PATH;
    private long downloaded = 0;
    HttpURLConnection connection;
    SharedPreferences sp;
    final int id = 1;
    NotificationManager mNotifyManager;
    NotificationCompat.Builder mBuilder;
    AsyncTask<Void, Void, Void> downloadTask;
    private RandomAccessFile outFile;
    // Tracker tracker;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.test_fragment, container, false);
        TextView tv = (TextView) rootView.findViewById(R.id.textView);
        start = (Button) rootView.findViewById(R.id.btn_start);
        pause = (Button) rootView.findViewById(R.id.btn_pause);
//        Typeface tf= Typeface.createFromAsset(getActivity().getAssets(), "Akaash.ttf");
//        tv.setTypeface(tf);
        tv.setText("hello");
        initToolbar(rootView);
// Get tracker.
        //   tracker = ((MyApplication) getActivity().getApplication()).getTracker();

// Enable Advertising Features.
        //    tracker.enableAdvertisingIdCollection(true);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // All subsequent hits will be send with screen name = "main screen"
//                tracker.setScreenName("Home screen");
//
//                tracker.send(new HitBuilders.EventBuilder()
//                        .setCategory("UX")
//                        .setAction("click")
//                        .setLabel("submit")
//                        .build());
            }
        });


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadTask = new downloadTask();
                downloadTask.execute();
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                sendMessageToService(00);
//
                // downloadTask.cancel(true);
                connection.disconnect();
                try {
                    outFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.e("---message---", "message sent to service");
            }
        });

        //  download();
        //test updates


//        service = new ServiceManager(getActivity(), SomeService1.class, new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                switch (msg.what) {
//                    case SomeService1.MSG_COUNTER:
//                        // Receive counter value from service 1 and send it to service 2
////                        textValue1.setText("Counter @ Service1: " + msg.arg1);
////                        try {
////                            service2.send(Message.obtain(null, SomeService2.MSG_VALUE, msg.arg1, 0));
////                        } catch (RemoteException e) {
////                        }
//                        break;
//
//                    default:
//                        super.handleMessage(msg);
//                }
//            }
//        });
//
//        Intent ii = new Intent(getActivity(), Mservice.class);
//        getActivity().startService(ii);
        return rootView;
    }

    private void initToolbar(View rootView) {
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);


        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_menu_white);
        activity.getSupportActionBar().setTitle("new fragment");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public interface onMessageListiner {
        public void onMessage(boolean isStart);
    }

    public void download() {

        Intent intent = new Intent(getActivity(), DownloadService.class);
        String url = "http://s320.ve.vc/data/320/33447/273523/Bad_Baby_-_Gippy_Grewal_-_320Kbps_-_www.DjPunjab.Com.mp3";
//        "http://api.androidhive.info/progressdialog/hive.jpg"
        intent.putExtra("url", url);
        getActivity().startService(intent);
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        try {
//            service.unbind();
//
//        } catch (Throwable t) {
//            Log.e("MainActivity", "Failed to unbind from the service", t);
//        }
//    }

//    private View.OnClickListener btnStartListener = new View.OnClickListener() {
//        public void onClick(View v) {
//            service.start();
//
//        }
//    };
//    private View.OnClickListener btnStopListener = new View.OnClickListener() {
//        public void onClick(View v) {
//            service.stop();
//
//        }
//    };

//    private void sendMessageToService(int intvaluetosend) {
//        try {
//            service.send(Message.obtain(null, SomeService1.MSG_INCREMENT, intvaluetosend, 0));
//
//        } catch (RemoteException e) {
//        }
//    }


    class downloadTask extends AsyncTask<Void, Void, Void> {
        InputStream input;
        private int fileLength;
        OutputStream output;
        private boolean mDownloading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            sp = getActivity().getSharedPreferences("data", Context.MODE_MULTI_PROCESS);
            mNotifyManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder = new NotificationCompat.Builder(getActivity());
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            mBuilder.setContentTitle("Picture Download")
                    .setContentText("Download in progress")
                    .setTicker("Starts Downloading")
                    .setSmallIcon(R.mipmap.ic_download)
                    .setLargeIcon(bm);

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
                    mDownloading = true;
                    String lastModified = sp.getString("last", null);
                    downloaded = sp.getLong("bytes", 0);
//                    fileLength = sp.getInt("lenght", 0);
                    long mdownloaded = file.length();
                    connection.setRequestProperty("Range", "bytes=" + (file.length()) + "-");
                    connection.setRequestProperty("If-Range", lastModified);
                    fileLength = (int) mdownloaded + connection.getContentLength();
                    connection.connect();
                    Log.d("--lastModi---sp", lastModified + " Resume from: " + downloaded + " == " + mdownloaded + " from file");
                } else {
                    mDownloading = true;
                    connection.setRequestProperty("Range", "bytes=" + downloaded + "-");
                    connection.connect();
                    fileLength = connection.getContentLength();
                    String lastModified = connection.getHeaderField("Last-Modified");
                    Log.d("--lastModi", lastModified);
                    SharedPreferences.Editor e = sp.edit();
                    e.putString("last", lastModified);
                    e.putInt("lenght", fileLength);
                    e.commit();
                }
                Map<String, List<String>> map = connection.getHeaderFields();
                Log.d("AsyncDownloadFile", "header fields: " + map.toString());


                Log.d("---download--file", "Content length:" + fileLength);
                // download the file
                //      InputStream input = new BufferedInputStream(connection.getInputStream());
//            OutputStream output = new FileOutputStream("/sdcard/BarcodeScanner-debug.jpg");


                // Setup streams and buffers.
                if (mDownloading) {
                    output = new FileOutputStream(DESTINATION_PATH.getAbsolutePath(), true);
                } else {
                    output = new FileOutputStream(DESTINATION_PATH.getAbsolutePath());
                }
                input = new BufferedInputStream(connection.getInputStream(), 8192);
                //    outFile = new RandomAccessFile(DESTINATION_PATH.getAbsolutePath(), "rw");
//                if (downloaded > 0) {
////                    outFile.seek(downloaded);
//                    long moveto = outFile.length();
//                    outFile.seek(moveto);
//                    Log.e("---move to","--: "+moveto);
//                }
                byte data[] = new byte[1024];

                // Download file.
                int count = 0;
                while ((count = input.read(data)) != -1) {
                    downloaded += count;
                    output.write(data, 0, count);
                    int incr = (int) (downloaded * 100 / fileLength);
                    mBuilder.setProgress(100, incr, false);
                    // Displays the progress bar for the first time.
                    mNotifyManager.notify(id, mBuilder.build());
                    //     Log.e("--bytes--", "total:" + total + "incr" + incr);
                    Log.d("--read--", "bytes: " + count + "  data ln:" + data.length + " data str" + data.toString() + " Downloaded: " + downloaded);

                }


//                if (input.available() <= 0) {
//                    input.close();
//                    Log.e("---inpust=---","Closed");
//                }
//                for (int i = 0; (count = input.read(data, 0, 1024)) != -1; i++) {
//                    outFile.write(data, 0, count);
//                    downloaded += count;
//
//                    if (downloaded >= fileLength) {
//                        break;
//                    }
//
//                    Log.d("--read--", "bytes: " + count + "  data ln:" + data.length + " data str" + data.toString() + " Downloaded: " + downloaded);
//
//                    // Display progress.
//                    //     Log.d("AsyncDownloadFile", "bytes: " + downloaded);
//                    // if ((i % 10) == 0) {
//                    int incr = (int) (downloaded * 100 / fileLength);
//                    // }
//                    mBuilder.setProgress(100, incr, false);
//                    // Displays the progress bar for the first time.
//                    mNotifyManager.notify(id, mBuilder.build());
//                    //     Log.e("--bytes--", "total:" + total + "incr" + incr);
////                    if (mFlagDisableAsyncTask) {
////                        downloaded = 0;
////                        break;
////                    }
//                }
                Log.d("--read--", "bytes: " + count + "  out--" + "  file size:" + file.length());
                // Close streams.

                //   outFile.close();
                input.close();

//                OutputStream output = new FileOutputStream(DESTINATION_PATH.getAbsolutePath(),true);
//                byte data[] = new byte[1024];
//                long total = 0;
//                int count;
//                while ((count = input.read(data)) != -1) {
//                    total += count;
//                    output.write(data, 0, count);
//                    int incr = (int) (total * 100 / fileLength);
//                    mBuilder.setProgress(100, incr, false);
//                    // Displays the progress bar for the first time.
//                    mNotifyManager.notify(id, mBuilder.build());
//                    //     Log.e("--bytes--", "total:" + total + "incr" + incr);
//                }
                output.flush();
                output.close();
//                input.close();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("--exeption=-", " ex:" + e);
                SharedPreferences.Editor ed = sp.edit();
                ed.putLong("bytes", downloaded);
                ed.commit();
                try {
                    input.close();
                    output.flush();
                    output.close();
                    //   outFile.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null) {
                    connection.disconnect();
                }
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
        protected void onCancelled() {
            connection.disconnect();
            try {
                outFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            SharedPreferences.Editor e = sp.edit();
            e.putLong("bytes", downloaded);
            e.commit();
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            // When the loop is finished, updates the notification
            mBuilder.setContentText("Download complete")
                    // Removes the progress bar
                    .setProgress(0, 0, false).setTicker("Downloading completes");
            mNotifyManager.notify(id, mBuilder.build());
            super.onPostExecute(aVoid);
        }
    }
}
