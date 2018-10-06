package com.example.Group5.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "ticket")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TICKET_ID", nullable = false)
    private int ticketId;

    @Basic
    @Column(name = "ROUTE_ID", nullable = false)
    private int routeId;

    @Basic
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User_Id", nullable = false)
    private AppUser customer;

    @Basic
    @Column(name = "AMOUNT", nullable = false)
    private int amount;

    @Basic
    @CreationTimestamp
    @Column(name = "BOOKING_DATE", nullable = false)
    private Date bookingDate;

    @Basic
    @Column(name = "STATUS")
    private Boolean status;


    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public AppUser getCustomer() { return customer; }

    public void setCustomer(AppUser customer) { this.customer = customer; }

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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
