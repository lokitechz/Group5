package com.example.Group5.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ticket")
public class Ticket {
    private int ticketId;
    private int seatNo;

    @Id
    @Column(name = "TICKET_ID", nullable = false)
    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    @Basic
    @Column(name = "SEAT_NO", nullable = false)
    public int getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(int seatNo) {
        this.seatNo = seatNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return ticketId == ticket.ticketId &&
                seatNo == ticket.seatNo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticketId, seatNo);
    }
}