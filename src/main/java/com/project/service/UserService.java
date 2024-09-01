package com.project.service;

import com.project.entity.Users;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Users saveUser(Users user);
    Users updateUser(Long id, Users user);
    Optional<Users> getUserById(Long id);
    List<Users> getAllUsers();
    void deleteUser(Long id);
}
