package com.andrew.productcatalogue2.repository;

import com.andrew.productcatalogue2.entity.users.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer>
{
    Optional<User> findByEmail(String email);
}
