package com.example.Group5.entity;

import javax.persistence.*;

@Entity
@Table(name = "bus")
public class Bus {

    @Id
    @Column(name = "BUS_ID", nullable = false)
    private int busId;

    @Basic
    @Column(name = "BUS_NO", nullable = false)
    private int busNo;

    @Basic
    @Column(name = "BUS_TYPE_ID", nullable = false)
    private int busTypeId;


    public int getBusId() {
        return busId;
    }

    public void setBusId(int busId) {
        this.busId = busId;
    }

    public int getBusNo() {
        return busNo;
    }

    public void setBusNo(int busNo) {
        this.busNo = busNo;
    }

    public int getBusTypeId() {
        return busTypeId;
    }

    public void setBusTypeId(int busTypeId) {
        this.busTypeId = busTypeId;
    }
}
