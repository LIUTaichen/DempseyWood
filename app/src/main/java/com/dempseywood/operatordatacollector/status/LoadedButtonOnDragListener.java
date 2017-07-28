package com.dempseywood.operatordatacollector.status;

import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.widget.Button;

import com.dempseywood.operatordatacollector.EquipmentStatusActivity;
import com.dempseywood.operatordatacollector.R;

/**
 * Created by musing on 28/07/2017.
 */

public class LoadedButtonOnDragListener implements OnDragListener {
    @Override
    public boolean onDrag(View v, DragEvent event) {

        switch(event.getAction()){
            case DragEvent.ACTION_DRAG_STARTED:
                if(event.getClipDescription().getLabel().equals("loaded")){
                    return false;
                }else{
                    return true;
                }

            case DragEvent.ACTION_DROP:
                EquipmentStatusActivity activity = (EquipmentStatusActivity)v.getContext();
                activity.switchToLoaded();
                return true;
            case DragEvent.ACTION_DRAG_ENTERED:
                //v.setBackgroundColor(Color.GREEN);
                v.setBackgroundResource(R.drawable.entered_loaded);
                //v.setBackground(android.R.drawable.);
                return true;
            case DragEvent.ACTION_DRAG_EXITED:
                v.setBackgroundResource(R.drawable.inactive);
            case DragEvent.ACTION_DRAG_ENDED:
                Log.e("LoadedButtonOnDrag", "ACTION_DRAG_ENDED");
                /*if(event.getClipDescription().getLabel().equals("unloaded")){
                    return true;
                }else{
                    v.setBackgroundResource(R.drawable.active_loaded);
                    return false;
                }*/

        }
        return true;
    }
}
