package com.rajpal.books.fragments;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.vending.billing.IInAppBillingService;
import com.rajpal.books.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by asus on 23-06-15.
 */
public class Downloads extends Fragment {
    IInAppBillingService mService;
    ServiceConnection mServiceConn;
    Bundle querySkus;
    String sku = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.downloads, container, false);

        initToolbar(rootView);

        mServiceConn = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
                mService = null;
            }

            @Override
            public void onServiceConnected(ComponentName name,
                                           IBinder service) {
                mService = IInAppBillingService.Stub.asInterface(service);
            }
        };

        Button buy = (Button) getActivity().findViewById(R.id.btn_buy);
        Button list = (Button) getActivity().findViewById(R.id.btn_list);
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> skuList = new ArrayList<String>();
                skuList.add("premiumUpgrade");
                skuList.add("gas");
                querySkus = new Bundle();
             //   querySkus.putStringArrayList(“ITEM_ID_LIST”, skuList);

            }
        });
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Bundle buyIntentBundle = mService.getBuyIntent(3, getActivity().getPackageName(),
                            sku, "inapp", "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        getActivity().bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);


        return rootView;
    }

    class getDetails extends AsyncTask<Void, Void, Void> {
        Bundle skuDetails;
        private String mGasPrice;
        private String mPremiumUpgradePrice;

        @Override
        protected Void doInBackground(Void... params) {
            try {
                skuDetails = mService.getSkuDetails(3,
                        getActivity().getPackageName(), "inapp", querySkus);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                int response = skuDetails.getInt("RESPONSE_CODE");
                if (response == 0) {
                    ArrayList<String> responseList
                            = skuDetails.getStringArrayList("DETAILS_LIST");

                    for (String thisResponse : responseList) {


                        JSONObject object = new JSONObject(thisResponse);
                        sku = object.getString("productId");
                        String price = object.getString("price");
                        if (sku.equals("premiumUpgrade")) {
                            mPremiumUpgradePrice = price;
                        } else if (sku.equals("gas")) {
                            mGasPrice = price;
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            super.onPostExecute(aVoid);
        }
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
        if (mService != null) {
            getActivity().unbindService(mServiceConn);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001) {
            int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
            String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
            String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");

            if (resultCode == Activity.RESULT_OK) {
                try {
                    JSONObject jo = new JSONObject(purchaseData);
                    String sku = jo.getString("productId");
                  //  alert("You have bought the " + sku + ". Excellent choice,adventurer !");
                } catch (JSONException e) {
              //      alert("Failed to parse purchase data.");
                    e.printStackTrace();
                }
            }
        }
    }
}
