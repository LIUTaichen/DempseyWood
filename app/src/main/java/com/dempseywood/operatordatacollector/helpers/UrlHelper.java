package com.dempseywood.operatordatacollector.helpers;

/**
 * Created by Jason.Liu on 18/01/2018.
 */

public class UrlHelper {
    //public static final String SERVER_CONTEXT_ROOT = "http://54.153.134.220:8090";
    //public static final String SERVER_CONTEXT_ROOT = "http://loadcount.ap-southeast-2.elasticbeanstalk.com";
    public static final String SERVER_CONTEXT_ROOT = "http://192.168.100.103:8090";
    public static final String API_EQUIPMENT = "/api/equipment";
    public static final String API_TASK = "/api/task";
    public static final String API_HAUL_ROOT = "/api/haul";
    public static final String API_HAUL_FINISH = "/unload";
    public static final String API_HAUL_UPDATE_TASK = "/updateTask";
    public static final String API_HAUL_BATCH_UPDATE_TASK = "/batch";


    public static String getFetchEquipmentUrl(){
        return SERVER_CONTEXT_ROOT + API_EQUIPMENT;
    }

    public static String getFetchTaskUrl(){
        return SERVER_CONTEXT_ROOT + API_TASK;
    }

    public static String getStartHaulUrl(){
        return SERVER_CONTEXT_ROOT + API_HAUL_ROOT;
    }

    public static String getFinisheHaulUrl(Integer haulId){
        return SERVER_CONTEXT_ROOT  + API_HAUL_ROOT + "/" +haulId + API_HAUL_FINISH;
    }
    public static String getUpdateTaskUrl(Integer haulId){
        return SERVER_CONTEXT_ROOT + API_HAUL_ROOT + haulId + API_HAUL_UPDATE_TASK ;
    }

    public static String getBatchUpdateUrl(){
        return SERVER_CONTEXT_ROOT + API_HAUL_ROOT + API_HAUL_BATCH_UPDATE_TASK;
    }


}
