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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BUS_TYPE_ID", nullable = false)
    private BusType busType;


    public int getBusId() { return busId; }

    public void setBusId(int busId) {
        this.busId = busId;
    }

    public int getBusNo() {
        return busNo;
    }

    public void setBusNo(int busNo) {
        this.busNo = busNo;
    }

    public BusType getBusType() { return busType; }

    public void setBusType(BusType busType) { this.busType = busType; }
}
