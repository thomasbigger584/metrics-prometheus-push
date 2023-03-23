package com.twb.app.dto;

public class SnmpWalkDTO {
    private Long ifInOctets;
    private Long sysUpTime;

    public Long getIfInOctets() {
        return ifInOctets;
    }

    public void setIfInOctets(Long ifInOctets) {
        this.ifInOctets = ifInOctets;
    }

    public Long getSysUpTime() {
        return sysUpTime;
    }

    public void setSysUpTime(Long sysUpTime) {
        this.sysUpTime = sysUpTime;
    }
}
