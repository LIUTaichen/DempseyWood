package com.dempseywood.operatordatacollector.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.dempseywood.operatordatacollector.activities.AsyncActionObserver;
import com.dempseywood.operatordatacollector.data.DB;
import com.dempseywood.operatordatacollector.data.dao.HaulDao;
import com.dempseywood.operatordatacollector.helpers.UpdateTaskRequestBuilder;
import com.dempseywood.operatordatacollector.helpers.UrlHelper;
import com.dempseywood.operatordatacollector.models.Haul;
import com.dempseywood.operatordatacollector.models.Task;
import com.dempseywood.operatordatacollector.models.UpdateTaskRequest;
import com.dempseywood.operatordatacollector.service.VariableTimeoutRestTemplate;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Jason.Liu on 7/02/2018.
 */

public class ChangeHaulTaskCommand extends AsyncTask<Void, Void, Boolean> {
    private String tag = "ChangeHaulTaskCommand";
    private AsyncActionObserver observer;
    private List<Haul> haulsToBeChanged;
    private Task newTask;
    private HaulDao haulDao;

    public ChangeHaulTaskCommand(Context context, AsyncActionObserver observer, List<Haul> hauls, Task newTask) {
        this.observer = observer;
        this.haulsToBeChanged = hauls;
        this.newTask = newTask;
        haulDao = DB.getInstance(context).haulDao();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        UpdateTaskRequest[] requestArray = new UpdateTaskRequest[haulsToBeChanged.size()];
        for (int i = 0; i < haulsToBeChanged.size(); i++) {
            Haul haul = haulsToBeChanged.get(i);
            UpdateTaskRequest request = UpdateTaskRequestBuilder.build(haul, newTask);
            requestArray[i] = request;
        }
        final String url = UrlHelper.getBatchUpdateTaskUrl();
        RestTemplate restTemplate = new VariableTimeoutRestTemplate(5000);
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

        // set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<UpdateTaskRequest[]> entity = new HttpEntity<UpdateTaskRequest[]>(requestArray, headers);
        // send request and parse result
        try {
            ResponseEntity<Haul[]> response = restTemplate
                    .exchange(url, HttpMethod.POST, entity, Haul[].class);
            if (response.getStatusCode() == HttpStatus.ACCEPTED) {
                for (Haul haul : response.getBody()) {
                    haulDao.update(haul);
                }
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.d(tag, "exception when communicating with server", e);
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (success) {
            observer.onComplete();
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
