package com.driver.services.impl;

import com.driver.model.User;
import com.driver.repository.UserRepository;
import com.driver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository4;
    @Override
    public void deleteUser(Integer userId) {Optional<User> op = userRepository4.findById(userId);
        Optional<User> opt = userRepository4.findById(userId);
        if(!opt.isPresent()){
            throw new RuntimeException("User not found");
        }
        userRepository4.delete(opt.get());
    }

    @Override
    public User updatePassword(Integer userId, String password) {
        Optional<User> op = userRepository4.findById(userId);
        if(!op.isPresent()){
            throw new RuntimeException("User not found");
        }
        User user = op.get();
        user.setPassword(password);
        return userRepository4.save(user);
    }

    @Override
    public void register(String name, String phoneNumber, String password) {
        User user = new User();
        user.setName(name);
        user.setPhoneNumber(phoneNumber);
        user.setPassword(password);
        userRepository4.save(user);

    }
}
