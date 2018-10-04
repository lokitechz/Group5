package com.example.Group5.entity;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "bus_route")
public class BusRoute {

    @Id
    @Column(name = "ROUTE_ID", nullable = false)
    private int routeId;

    @Basic
    @Column(name = "ORIGIN", nullable = false, length = 50)
    private String origin;

    @Basic
    @Column(name = "DESTINATION", nullable = false, length = 50)
    private String destination;

    @Basic
    @Column(name = "BREAK_POINT", nullable = false)
    private String breakPoint;

    @Basic
    @Column(name = "DEPARTURE_DATE", nullable = false)
    private Date departureDate;

    @Basic
    @Column(name = "ARRIVALTIME", nullable = false, length = 50)
    private String arrivaltime;

    @Basic
    @Column(name = "FARE", nullable = false, precision = 0)
    private double fare;

    @Basic
    @Column(name = "BUS_ID", nullable = false)
    private int busId;


    public int getRouteId() { return routeId; }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getBreakPoint() {
        return breakPoint;
    }

    public void setBreakPoint(String breakPoint) {
        this.breakPoint = breakPoint;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public String getArrivaltime() {
        return arrivaltime;
    }

    public void setArrivaltime(String arrivaltime) {
        this.arrivaltime = arrivaltime;
    }

    public double getFare() {
        return fare;
    }

    public void setFare(double fare) {
        this.fare = fare;
    }

    public int getBusId() { return busId; }

    public void setBusId(int busId) {
        this.busId = busId;
    }

}
