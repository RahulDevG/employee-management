package com.emp.management.service;

import com.emp.management.Exception.BusinessValidationException;
import com.emp.management.entity.User;
import com.emp.management.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User addUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public boolean validateUser(User user) {
        User dbUser = userRepository.findByEmail(user.getEmail());
        if(dbUser == null) {
            throw new BusinessValidationException("Email does not exist");
        }
        else if(!passwordEncoder.matches(user.getPassword(), dbUser.getPassword())) {
            throw new BusinessValidationException("Password does not match");
        }
        return true;
    }
}
