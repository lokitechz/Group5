package com.example.Group5.repository;

import com.example.Group5.entity.AppUser;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AppUserRepo extends PagingAndSortingRepository<AppUser, Long> {
    AppUser findByUserId(long a);
    AppUser findAppUserByUserName(String name);
}
