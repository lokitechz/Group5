package com.example.Group5.entity;

import javax.persistence.*;

@Entity
@Table(name = "bus_type")
public class BusType {

    @Id
    @GeneratedValue
    @Column(name = "Bus_type_Id", nullable = false)
    private int busTypeId;

    @Column(name = "Type", nullable = false)
    private String busType;

    public int getBusTypeId() {
        return busTypeId;
    }

    public void setBusTypeId(int busTypeId) {
        this.busTypeId = busTypeId;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }
}
