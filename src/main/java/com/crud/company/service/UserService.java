package com.crud.company.service;

import com.crud.company.entity.User;

public interface UserService {
    User addUser(User user);
    boolean validateUser(User user);
}
