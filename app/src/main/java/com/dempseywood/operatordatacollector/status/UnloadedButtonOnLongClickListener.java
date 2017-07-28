package com.dempseywood.operatordatacollector.status;

import android.content.ClipData;
import android.view.View;
import android.view.View.OnLongClickListener;

/**
 * Created by musing on 28/07/2017.
 */

public class UnloadedButtonOnLongClickListener implements OnLongClickListener {
    @Override
    public boolean onLongClick(View v) {


        ClipData dragData =  ClipData.newPlainText("unloaded", "");
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
        v.startDrag(dragData, shadowBuilder, v, 0);
        //v.setVisibility(View.INVISIBLE);

        return false;
    }
}
