package com.alemdar_energy.backend.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.alemdar_energy.backend.model.User;
import com.alemdar_energy.backend.repository.UserRepo;
import com.alemdar_energy.backend.util.TokenUtil;

@Service
public class PasswordResetService {
  
  private MailService mailService;
  private UserRepo userRepo;
  private BCryptPasswordEncoder encoder;

  @Autowired
  public PasswordResetService(MailService mailService, UserRepo userRepo, BCryptPasswordEncoder encoder) {
    this.mailService = mailService;
    this.userRepo = userRepo;
    this.encoder = encoder;
  }

  public String sendResetToken(String email){
    User user = userRepo.findByEmail(email).get();
    if (user ==null) {
      throw new NullPointerException("user is null...");
    }
    String token = TokenUtil.generateResetToken();
    user.setResetToken(encoder.encode(token));
    user.setTokenExpiryDate(LocalDateTime.now().plusHours(1));
    userRepo.save(user);
    mailService.mailSend(email, token);
    
    return token;
  }

  public User validGenerateToken(String token){
    User user = userRepo.findAll().stream()
    .filter(u -> encoder.matches(token,u.getResetToken())).findFirst()
    .orElseThrow(()-> new IllegalArgumentException("geçersiz token veya Tokenin süresi dolmuş."));
    if (user.getTokenExpiryDate().isBefore(LocalDateTime.now())) {
      throw new IllegalArgumentException("tokenin süresi dolmuş");
    }
    return user;
  }

  public void resetPassword(String token ,String newPassword){
    User user = validGenerateToken(token);
    user.setPassword(encoder.encode(newPassword));
    user.setResetToken(null);
    user.setTokenExpiryDate(null);
    userRepo.save(user);
  }

}
