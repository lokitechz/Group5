package com.example.Group5.repository;

import com.example.Group5.entity.Booking;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BookingRepo extends PagingAndSortingRepository<Booking, Integer> {
}
