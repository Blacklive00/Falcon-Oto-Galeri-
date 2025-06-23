package com.alemdar_energy.backend.service;

import com.alemdar_energy.backend.model.User;

public interface UserService {

  User findByUsername(String username);

  User saveUser(User user);

  User findUserById(long id);

  User saveDefUser(User user);
}
