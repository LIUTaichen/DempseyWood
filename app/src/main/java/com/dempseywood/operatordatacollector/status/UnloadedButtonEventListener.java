package com.dempseywood.operatordatacollector.status;

import android.content.ClipData;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Button;

/**
 * Created by musing on 28/07/2017.
 */

public class UnloadedButtonEventListener implements OnLongClickListener, View.OnTouchListener{

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Button button = (Button)v;
        if(button.isClickable()) {

            ClipData dragData = ClipData.newPlainText("unloaded", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            v.startDrag(dragData, shadowBuilder, v, 0);
        }
        return false;
    }

    @Override
    public boolean onLongClick(View v) {

        Button button = (Button)v;
        if(button.isClickable()) {
            ClipData dragData = ClipData.newPlainText("unloaded", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            v.startDrag(dragData, shadowBuilder, v, 0);
            //v.setVisibility(View.INVISIBLE);
        }
        return false;
    }
}
