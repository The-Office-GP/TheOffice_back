package com.TheOffice.theOffice.classes;

import java.util.List;

public class MachineList {
    private List<Machine> machineList;

    public MachineList(){

    }

    public MachineList(List<Machine> machineList) {
        this.machineList = machineList;
    }

    public List<Machine> getMachineList() {
        return machineList;
    }

    public void setMachineList(List<Machine> machineList) {
        this.machineList = machineList;
    }
}
