package com.emp.management.service;

import com.emp.management.entity.User;

public interface UserService {
    User addUser(User user);
    boolean validateUser(User user);
}
