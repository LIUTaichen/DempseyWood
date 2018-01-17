package com.dempseywood.operatordatacollector.listeners;

import android.view.DragEvent;
import android.view.View;
import android.widget.Button;

import com.dempseywood.operatordatacollector.R;
import com.dempseywood.operatordatacollector.models.EquipmentStatus;
import com.dempseywood.operatordatacollector.activities.EquipmentStatusActivity;
import com.dempseywood.operatordatacollector.rest.SynchronizeWithServerTask;
import com.dempseywood.operatordatacollector.models.DataHolder;

import java.util.Date;

/**
 * Created by musing on 31/07/2017.
 */

public class ButtonOnDragListener implements View.OnDragListener {
    @Override
    public boolean onDrag(View v, DragEvent event) {
        Button button = (Button)v;
        //check if button receiving the event is the loaded button or the unloaded button
        boolean isLoadedButton = button.getText().equals( v.getContext().getResources().getString(R.string.string_loaded));
        switch(event.getAction()){
            case DragEvent.ACTION_DRAG_STARTED:
                if(event.getClipDescription().getLabel().equals(button.getText())){
                    return false;
                }else{
                    return true;
                }
            case DragEvent.ACTION_DROP:
                EquipmentStatusActivity activity = (EquipmentStatusActivity)v.getContext();
                //switch to unloaded status if the current button is loaded button, loaded status if the current button is unloaded button
                if(isLoadedButton){
                    activity.switchToLoaded();
                }
                else{
                    activity.switchToUnloaded();
                }
                EquipmentStatus status = DataHolder.getInstance().getEquipmentStatus();
                status.setEquipment(DataHolder.getInstance().getEquipment().getName());
                status.setTimestamp( new Date());
                if(DataHolder.getInstance().getLocation() != null) {
                    status.setLongitude(DataHolder.getInstance().getLocation().getLongitude());
                    status.setLatitude(DataHolder.getInstance().getLocation().getLatitude());
                }
                new SynchronizeWithServerTask(v.getContext()).execute();
                return true;
            case DragEvent.ACTION_DRAG_ENTERED:
                if(isLoadedButton){
                    v.setBackgroundResource(R.drawable.entered_loaded);
                }
                else{
                    v.setBackgroundResource(R.drawable.entered_unloaded);
                }
                return true;
            case DragEvent.ACTION_DRAG_EXITED:
                v.setBackgroundResource(R.drawable.inactive);
            case DragEvent.ACTION_DRAG_ENDED:

        }
        return true;
    }


}
