package com.andrew.productcatalogue2.security;

import com.andrew.productcatalogue2.entity.users.Role;
import com.andrew.productcatalogue2.entity.users.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> findUserByEmail(String email);

    User addUser(User user);

    List<Role> getRoles();
}
