package com.example.Group5.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "ticket")
public class Ticket {
    @Id
    @Column(name = "TICKET_ID", nullable = false)
    private int ticketId;

    @Basic
    @Column(name = "BUS_ID", nullable = false)
    private int busId;

    @Basic
    @Column(name = "PASSENGER_ID", nullable = false)
    private int passengerId;

    @Basic
    @Column(name = "AMOUNT", nullable = false)
    private int amount;

    @Basic
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @CreationTimestamp
    @Column(name = "BOOKING_DATE", nullable = false)
    private Date bookingDate;


    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public int getBusId() {
        return busId;
    }

    public void setBusId(int busId) {
        this.busId = busId;
    }

    public int getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }
}
