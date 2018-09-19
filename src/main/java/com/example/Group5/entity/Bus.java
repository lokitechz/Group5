package com.example.Group5.entity;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "bus", uniqueConstraints = {@UniqueConstraint(name = "BUS_NO", columnNames = "BUS_NO")})
public class Bus {

    @Id
    @GeneratedValue
    @Column(name = "Bus_Id", nullable = false)
    private int busId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Route_Id", nullable = false)
    private BusRoute busRoute;

    @Column(name = "Bus_No", nullable = false)
    private String busNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Bus_type_Id", nullable = false)
    private BusType busType;

    public int getId() {
        return busId;
    }

    public void setId(int id) {
        this.busId = id;
    }

    public BusRoute getBusRoute() {
        return busRoute;
    }

    public void setBusRoute(BusRoute busRoute) {
        this.busRoute = busRoute;
    }

    public String getBusNo() {
        return busNo;
    }

    public void setBusNo(String busNo) {
        this.busNo = busNo;
    }

    public BusType getBusType() {
        return busType;
    }

    public void setBusType(BusType busType) {
        this.busType = busType;
    }
}

