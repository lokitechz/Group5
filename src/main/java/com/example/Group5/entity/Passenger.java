package com.example.Group5.entity;

import javax.persistence.*;

@Entity
@Table(name = "passenger")
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PASSENGER_ID", nullable = false)
    private int passengerId;

    @Basic
    @Column(name = "PASSENGER_NAME", nullable = false)
    private String passengerName;

    @Basic
    @Column(name = "PASSENGER_AGE", nullable = false)
    private int passengerAge;

    public int getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public int getPassengerAge() {
        return passengerAge;
    }

    public void setPassengerAge(int passengerAge) {
        this.passengerAge = passengerAge;
    }
}
