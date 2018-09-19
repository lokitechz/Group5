package com.example.Group5.entity;

import javax.persistence.*;

@Entity
@Table(name = "bus_type")
public class BusType {

    @Id
    @GeneratedValue
    @Column(name = "Bus_type_Id", nullable = false)
    private int busRouteId;

    @Column(name = "Type", nullable = false)
    private String busType;

    public int getBusRouteId() {
        return busRouteId;
    }

    public void setBusRouteId(int busRouteId) {
        this.busRouteId = busRouteId;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }
}
