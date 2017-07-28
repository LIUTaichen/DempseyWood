package com.dempseywood.operatordatacollector.status;

import android.graphics.drawable.StateListDrawable;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.util.Log;

import com.dempseywood.operatordatacollector.EquipmentStatusActivity;
import com.dempseywood.operatordatacollector.R;

/**
 * Created by musing on 28/07/2017.
 */

public class UnloadedButtonOnDragListener implements OnDragListener {
    @Override
    public boolean onDrag(View v, DragEvent event) {

        switch(event.getAction()){
            case DragEvent.ACTION_DRAG_STARTED:
                if(event.getClipDescription().getLabel().equals("loaded")){
                    return true;
                }else{
                    //v.setBackgroundResource(R.drawable.inactive);
                    return false;
                }


            case DragEvent.ACTION_DROP:
                EquipmentStatusActivity activity = (EquipmentStatusActivity)v.getContext();
                activity.switchToUnloaded();
                return true;
            case DragEvent.ACTION_DRAG_ENTERED:
                v.setBackgroundResource(R.drawable.entered_unloaded);


                return true;
            case DragEvent.ACTION_DRAG_EXITED:
                v.setBackgroundResource(R.drawable.inactive);
           /* case DragEvent.ACTION_DRAG_ENDED:
                if(event.getClipDescription().getLabel().equals("loaded")){
                    return true;
                }else{
                    v.setBackgroundResource(R.drawable.active_unloaded);
                    return false;
                }*/


        }
        return true;
    }
}
