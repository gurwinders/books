package com.rajpal.books.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.LikeView;
import com.facebook.share.widget.ShareDialog;
import com.rajpal.books.R;

import java.util.Arrays;

/**
 * Created by asus on 26-06-15.
 */
public class Facebook extends Fragment {

    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    private LikeView likeView;
    private LinearLayout btnLoginToLike;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.facebook, container, false);
//        FacebookSdk.sdkInitialize(getActivity());
//        ShareLinkContent content = new ShareLinkContent.Builder()
//                .setContentUrl(Uri.parse("https://developers.facebook.com"))
//                .build();
        // To maintain FB Login session
//        uiHelper = new UiLifecycleHelper(this, null);
//        uiHelper.onCreate(savedInstanceState);

        // FacebookSdk.sdkInitialize(getActivity());
        callbackManager = CallbackManager.Factory.create();
        Button shareButton = (Button) rootView.findViewById(R.id.fb_share_button);
        shareDialog = new ShareDialog(this);
        // this part is optional
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Log.d("---facebook--", "share result  " + result.toString());
            }

            @Override
            public void onCancel() {
                Log.d("---facebook--", "share Canceled  ");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("---facebook--", "share result error " + error.toString());
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse("https://developers.facebook.com"))
                        .build();

                ShareDialog.show(getActivity(), content);
            }
        });
        LikeView likeView = (LikeView) rootView.findViewById(R.id.likeView);
        likeView.setLikeViewStyle(LikeView.Style.STANDARD);
        likeView.setAuxiliaryViewPosition(LikeView.AuxiliaryViewPosition.INLINE);

        likeView.setObjectIdAndType(
                "http://facebook.com/tumarega",
                LikeView.ObjectType.OPEN_GRAPH);
//likeView.regi
//        ShareButton shareButton = (ShareButton) rootView.findViewById(R.id.fb_share_button);
//        shareButton.setShareContent(content);
//        shareButton.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
//            @Override
//            public void onSuccess(Sharer.Result result) {
//
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//
//            }
//        });

        //sendButton.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {

//        initInstances(rootView);
//        initCallbackManager();
        return rootView;
    }

    private void initInstances(View rootView) {
        btnLoginToLike = (LinearLayout) rootView.findViewById(R.id.btnLoginToLike);
        likeView = (LikeView) rootView.findViewById(R.id.likeView);
        likeView.setLikeViewStyle(LikeView.Style.STANDARD);
        likeView.setAuxiliaryViewPosition(LikeView.AuxiliaryViewPosition.INLINE);
        likeView.setObjectIdAndType(
                "http://facebook.com/tumarega",
                LikeView.ObjectType.OPEN_GRAPH);
        btnLoginToLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile"));
            }
        });
    }

    private void initCallbackManager() {

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                refreshButtonsState();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {

            }
        });
    }

    private void refreshButtonsState() {
        if (!isLoggedIn()) {
            btnLoginToLike.setVisibility(View.VISIBLE);
            likeView.setVisibility(View.GONE);
        } else {
            btnLoginToLike.setVisibility(View.GONE);
            likeView.setVisibility(View.VISIBLE);
        }
    }

    public boolean isLoggedIn() {
        return AccessToken.getCurrentAccessToken() != null;
    }


    @Override
    public void onActivityResult(final int requestCode, final int resultCode,
                                 final Intent data) {

        callbackManager.onActivityResult(requestCode, resultCode, data);
        Log.e("onactivity", "result code " + requestCode + " data " + data.toString());
        super.onActivityResult(requestCode, resultCode, data);

    }
}
