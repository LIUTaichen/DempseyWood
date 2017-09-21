package com.dempseywood.operatordatacollector.service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by musing on 20/09/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private String TAG = "FirebaseID";
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.i(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.

        sendRegistrationToServer(refreshedToken);
    }
    //TODO:implement this method
    private void sendRegistrationToServer(String refreshedToken){
    }
}
