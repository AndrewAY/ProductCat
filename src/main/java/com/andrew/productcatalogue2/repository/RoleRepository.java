package com.andrew.productcatalogue2.repository;

import com.andrew.productcatalogue2.entity.users.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends CrudRepository<Role, Integer>
{

}
