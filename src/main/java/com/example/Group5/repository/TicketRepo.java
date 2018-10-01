package com.example.Group5.repository;

import com.example.Group5.entity.Ticket;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

public interface TicketRepo extends PagingAndSortingRepository<Ticket, Integer> {
    List<Ticket> findAllByBookingDate(Date a);

    List<Ticket> findAllByBusId(int busId);
}
