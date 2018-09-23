package com.example.Group5.repository;

import com.example.Group5.entity.Passenger;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PassengerRepo extends PagingAndSortingRepository<Passenger, Integer> {
}
