package com.alemdar_energy.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alemdar_energy.backend.model.User;

@Repository
public interface UserRepo extends JpaRepository<User,Long>{
  Optional<User>findByUsername(String username);

  Optional<User>findByEmail(String email);

  Optional<User>findByResetToken(String resetToken);
}
