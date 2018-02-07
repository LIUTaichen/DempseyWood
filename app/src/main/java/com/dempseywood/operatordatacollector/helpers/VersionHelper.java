package com.dempseywood.operatordatacollector.helpers;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * Created by Jason.Liu on 8/02/2018.
 */

public class VersionHelper {
    public static int getVersionCode(Context context){
        PackageManager pm = context.getPackageManager();
        PackageInfo pInfo = null;

        try {
            pInfo =  pm.getPackageInfo(context.getPackageName(),0);

        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
        int currentVersion = pInfo.versionCode;
        return currentVersion;
    }
}
