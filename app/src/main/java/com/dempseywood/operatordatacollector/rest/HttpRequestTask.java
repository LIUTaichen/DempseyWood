package com.dempseywood.operatordatacollector.rest;

import android.os.AsyncTask;
import android.util.Log;

import com.dempseywood.operatordatacollector.rest.status.EquipmentStatus;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by musing on 04/08/2017.
 */

public class HttpRequestTask extends AsyncTask<Void, Void, EquipmentStatus> {

    @Override
    protected EquipmentStatus doInBackground(Void... params) {
        try {
            Log.e("HttpRequestTask", "started");
            final String url = "http://192.168.100.103:8080/status";
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

            //JSONObject status = new JSONObject();
            /*tatus.put("id", "11");
            status.put("equipment", "ABGCCC");
            status.put("operator", "kkkoper");
            status.put("status", "Unloaded");s*/
            EquipmentStatus status = new EquipmentStatus();
            status.setEquipment("piece");
            status.setOperator("Ruse");
            status.setStatus("Loaded");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            //status.put("timestamp", sdf.format(new Date()));
            // set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Arrays.asList(MediaType.TEXT_PLAIN));
            HttpEntity<EquipmentStatus> entity = new HttpEntity<EquipmentStatus>(status, headers);

// send request and parse result
            ResponseEntity<String> loginResponse = restTemplate
                    .exchange(url, HttpMethod.POST, entity, String.class);
            if (loginResponse.getStatusCode() == HttpStatus.CREATED) {
                //JSONObject userJson = new JSONObject(loginResponse.getBody());
                Log.e("HttpRequestTask", "created");
            } else if (loginResponse.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                // nono... bad credentials

            }
            Log.e("HttpRequestTask", "finished");
        }catch(Exception e){
                Log.e("HttpRequestTask", e.getMessage(), e);
            }

            return null;

    }

    @Override
    protected void onPostExecute(EquipmentStatus status) {
       /* TextView greetingIdText = (TextView) findViewById(R.id.id_value);
        TextView greetingContentText = (TextView) findViewById(R.id.content_value);
        greetingIdText.setText(greeting.getId());
        greetingContentText.setText(greeting.getContent());*/
    }

}
