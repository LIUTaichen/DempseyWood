package com.dempseywood.operatordatacollector.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.dempseywood.operatordatacollector.activities.AsyncActionForDataObserver;
import com.dempseywood.operatordatacollector.helpers.UrlHelper;
import com.dempseywood.operatordatacollector.helpers.VersionHelper;
import com.dempseywood.operatordatacollector.service.VariableTimeoutRestTemplate;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Jason.Liu on 8/02/2018.
 */

public class VersionCheckCommand extends AsyncTask<Void, Void, Boolean> {

    private String tag = "VersionCheckCommand";
    private AsyncActionForDataObserver observer;
    private int versionCode = 1;
    private Boolean isUpdateNeeded;

    public VersionCheckCommand(Context context, AsyncActionForDataObserver<Boolean> observer) {
        this.observer = observer;
        versionCode = VersionHelper.getVersionCode(context);
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        Log.d(tag, "version code is : " + versionCode);
        RestTemplate restTemplate = new VariableTimeoutRestTemplate(5000);
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        String versionCheckApiUrl = UrlHelper.getVersionCheckUrl();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");

        HttpEntity<Integer> entity = new HttpEntity(versionCode, headers);
        try {
            HttpEntity<Boolean> response = restTemplate.exchange(versionCheckApiUrl, HttpMethod.POST, entity, Boolean.class);
            isUpdateNeeded = response.getBody();
            Log.d(tag, "version check result: need to update? : " + isUpdateNeeded);
            return true;
        } catch (Exception e) {
            Log.d(tag, "exception when communicating with server", e);
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (success) {
            observer.receiveData(isUpdateNeeded);
        } else {
            observer.onError();
        }
        super.onPostExecute(success);
    }


    @Override
    protected void onPreExecute() {
        observer.onPreStart();
        super.onPreExecute();
    }

    @Override
    protected void onCancelled() {
        Log.d(tag, "task is being cancelled");
        observer.onTaskCancelled();
        super.onCancelled();
    }
}
