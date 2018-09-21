package com.example.Group5.repository;

import com.example.Group5.entity.Bus;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BusRepo extends PagingAndSortingRepository<Bus, Integer> {
    List<Bus> findAllByBusNo(String busNo);
}
