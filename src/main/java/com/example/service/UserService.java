package com.example.service;


import com.example.domain.user.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    User getById(Long id);

    User getByUsername(String username);

    User update(User user);

    User create(User user);

    void delete(Long id);
}
