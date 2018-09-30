package com.example.Group5.repository;

import com.example.Group5.entity.AppUser;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AppUserRepo extends PagingAndSortingRepository<AppUser, Integer> {
    AppUser findAppUserByUserName(String name);
}
