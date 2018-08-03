package com.andrew.productcatalogue2.security;

import com.andrew.productcatalogue2.entity.users.Role;
import com.andrew.productcatalogue2.entity.users.User;
import com.andrew.productcatalogue2.repository.RoleRepository;
import com.andrew.productcatalogue2.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    //uses BCryptPasswordEncoder as defined in config bean
    @Autowired
    PasswordEncoder passwordEncoder;


    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public User addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

        public List<Role> getRoles() {
                return convertToList(roleRepository.findAll());
        }

    private <T> List<T> convertToList(Iterable<T> iterable) {
        List<T> list = new ArrayList<T>();
        iterable.forEach(list::add);
        return list;
    }
}
