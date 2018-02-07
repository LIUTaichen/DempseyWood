package com.dempseywood.operatordatacollector.activities;

/**
 * Created by Jason.Liu on 8/02/2018.
 */

public interface AsyncActionForDataObserver <T> extends AsyncActionObserver {
    void receiveData(T data);
}
