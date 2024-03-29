package com.example.Group5.repository;

import com.example.Group5.entity.Bus;
import com.example.Group5.entity.BusRoute;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

public interface BusRouteRepo extends PagingAndSortingRepository<BusRoute, Integer> {

    @Query(value = "select br from BusRoute br where br.origin = ?1 and br.destination = ?2 and br.departureDate = ?3")
    BusRoute search(String from, String to, Date date);

    List<BusRoute> findAllByBus(Bus bus);

    List<BusRoute> findBusRouteByDepartureDate(java.sql.Date date);
}
