package com.dempseywood.operatordatacollector.equipmentstatus.listener;

import android.content.ClipData;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

/**
 * Created by musing on 31/07/2017.
 */

public class ButtonOnTouchListener implements View.OnTouchListener{

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Button button = (Button)v;
        String buttonText = button.getText().toString();
        if(button.isClickable()) {
            ClipData dragData = ClipData.newPlainText(buttonText, "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            v.startDrag(dragData, shadowBuilder, v, 0);
        }
        return false;
    }
}
