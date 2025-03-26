package com.TheOffice.theOffice.entities;

public class MachineInCompany {
    private Long id;
    private Long machineId;
    private Long companyId;

    public MachineInCompany() {
    }

    public MachineInCompany(Long id, Long machineId, Long companyId) {
        this.id = id;
        this.machineId = machineId;
        this.companyId = companyId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMachineId() {
        return machineId;
    }

    public void setMachineId(Long machineId) {
        this.machineId = machineId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
}
