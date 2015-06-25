package com.rajpal.books.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rajpal.books.R;
import com.rajpal.books.util.IabHelper;
import com.rajpal.books.util.IabResult;

/**
 * Created by asus on 25-06-15.
 */
public class InApp extends Fragment {
    private IabHelper mHelper;
    private String TAG = "----inapp---";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.inapp, container, false);

        initToolbar(rootView);

        String base64EncodedPublicKey = AppConstants.lience;

//        // compute your public key and store it in base64EncodedPublicKey
//        mHelper = new IabHelper(mActivity(), base64EncodedPublicKey);
//        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
//            public void onIabSetupFinished(IabResult result) {
//                if (!result.isSuccess()) {
//                    // Oh noes, there was a problem.
//                    Log.d(TAG, "Problem setting up In-app Billing: " + result);
//                } else {
//                    Log.d(TAG, "Hooray, IAB is fully set up!" + result);
//                }
//            }
//        });


        Log.d(TAG, "Creating IAB helper.");
        mHelper = new IabHelper(getActivity(), base64EncodedPublicKey);

        // enable debug logging (for a production application, you should set
        // this to false).
        mHelper.enableDebugLogging(true);

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        Log.d(TAG, "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    Toast.makeText(getActivity(),
                            "Problem setting up in-app billing: " + result,
                            Toast.LENGTH_LONG).show();
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null)
                    return;

                // IAP is fully set up. Now, let's get an inventory of stuff we
                // own.
                Log.d(TAG, "Setup successful. Querying inventory.");
        //        mHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });

        return rootView;
    }

    private void initToolbar(View rootView) {
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);

        AppCompatActivity activity = mActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_menu_white);
        activity.setTitle("Text test Activity");
    }

    public AppCompatActivity mActivity() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        return activity;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }
}