package com.dempseywood.operatordatacollector.activities;

/**
 * Created by Jason.Liu on 7/02/2018.
 */

public interface AsyncActionObserver {
    void onPreStart();
    void onComplete();
    void onError();
    void onTaskCancelled();
}
