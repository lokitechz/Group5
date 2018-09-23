package com.example.Group5.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "passenger")
public class Passenger {
    private int passengerId;
    private String passengerName;
    private int passengerAge;

    @Id
    @Column(name = "PASSENGER_ID", nullable = false)
    public int getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }

    @Basic
    @Column(name = "PASSENGER_NAME", nullable = false, length = 255)
    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    @Basic
    @Column(name = "PASSENGER_AGE", nullable = false)
    public int getPassengerAge() {
        return passengerAge;
    }

    public void setPassengerAge(int passengerAge) {
        this.passengerAge = passengerAge;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Passenger passenger = (Passenger) o;
        return passengerId == passenger.passengerId &&
                passengerAge == passenger.passengerAge &&
                Objects.equals(passengerName, passenger.passengerName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(passengerId, passengerName, passengerAge);
    }
}