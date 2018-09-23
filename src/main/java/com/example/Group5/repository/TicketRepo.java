package com.example.Group5.repository;

import com.example.Group5.entity.Ticket;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TicketRepo extends PagingAndSortingRepository<Ticket, Integer> {
}
