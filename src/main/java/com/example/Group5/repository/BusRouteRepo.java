package com.example.Group5.repository;

import com.example.Group5.entity.BusRoute;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BusRouteRepo extends PagingAndSortingRepository<BusRoute, Integer> {
    List<BusRoute> findAllByBusRouteId(int id);
}
