package com.TheOffice.theOffice.dtos;

import com.TheOffice.theOffice.entities.MachineInCompany;

public class MachineInCompanyDto {
    private Long id;
    private Long machineId;
    private Long companyId;

    public MachineInCompanyDto() {}

    public MachineInCompanyDto(Long id, Long machineId, Long companyId) {
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

    public static MachineInCompanyDto fromEntity(MachineInCompany machineInCompany) {
        return new MachineInCompanyDto(machineInCompany.getId(), machineInCompany.getMachineId(), machineInCompany.getCompanyId());
    }
}
