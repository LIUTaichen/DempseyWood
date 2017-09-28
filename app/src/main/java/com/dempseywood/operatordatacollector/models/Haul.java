package com.dempseywood.operatordatacollector.models;

/**
 * Created by musing on 26/09/2017.
 */
public class Haul {
    private EquipmentStatus loadEvent;
    private EquipmentStatus unloadEvent;

    public EquipmentStatus getLoadEvent() {
        return loadEvent;
    }

    public void setLoadEvent(EquipmentStatus loadEvent) {
        this.loadEvent = loadEvent;
    }

    public EquipmentStatus getUnloadEvent() {
        return unloadEvent;
    }

    public void setUnloadEvent(EquipmentStatus unloadEvent) {
        this.unloadEvent = unloadEvent;
    }
}
