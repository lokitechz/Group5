package com.example.Group5.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "bus_route")
public class BusRoute {

    @Id
    @GeneratedValue
    @Column(name = "Route_Id", nullable = false)
    private int busRouteId;

    @Column(name = "Origin", nullable = false)
    private String origin;

    @Column(name = "Destination", nullable = false)
    private String destination;

    @Column(name = "Break_Point", nullable = false)
    private String breakPoint;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Column(name = "Departure_Date", nullable = false)
    private java.sql.Date departureDate;

    @Column(name = "ARRIVALTIME", nullable = false)
    private String arrivalTime;

    @Column(name = "FARE", nullable = false)
    private float fare;

    public int getBusRouteId() {
        return busRouteId;
    }

    public void setBusRouteId(int busRouteId) {
        this.busRouteId = busRouteId;
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

    public java.sql.Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(java.sql.Date departureDate) {
        this.departureDate = departureDate;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public float getFare() {
        return fare;
    }

    public void setFare(float fare) {
        this.fare = fare;
    }
}
