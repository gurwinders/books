package com.rajpal.books.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rajpal.books.DownloadService;
import com.rajpal.books.R;

/**
 * Created by asus on 22-06-15.
 */
public class Home extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.test_fragment, container, false);
        TextView tv = (TextView) rootView.findViewById(R.id.textView);

        tv.setText("new fragment");
        initToolbar(rootView);
      //  download();
       //test updates
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

    public void download() {

        Intent intent = new Intent(getActivity(), DownloadService.class);
        String url = "http://s320.ve.vc/data/320/33447/273523/Bad_Baby_-_Gippy_Grewal_-_320Kbps_-_www.DjPunjab.Com.mp3";
//        "http://api.androidhive.info/progressdialog/hive.jpg"
        intent.putExtra("url", url);
        getActivity().startService(intent);
    }


}
