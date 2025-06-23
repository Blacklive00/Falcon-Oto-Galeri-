package com.alemdar_energy.backend.model;

import java.time.LocalDateTime;
import java.util.Collection;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "user")
public class User {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "id")
  private Long id;

  @Column(name = "username")
  private String username;

  @Column(name = "password")
  private String password;

  @Column(name = "email")
  private String email;

  @Column(name = "reset_token")
  private String resetToken;

  @Column(name = "token_expiry_date")
  private LocalDateTime tokenExpiryDate;

  @Column(name = "enabled")
  private boolean enabled;

  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Collection<Role> roles;

  
  
  public User() {
  }

  //for login
  public User(String username, String password, boolean enabled) {
    this.username = username;
    this.password = password;
    this.enabled = enabled;
  }

  //constructor for reset password
  public User(String username, String email, String resetToken, LocalDateTime tokenExpiryDate) {
    this.username = username;
    this.email = email;
    this.resetToken = resetToken;
    this.tokenExpiryDate = tokenExpiryDate;
  }


  //constructor for signup
  public User(String username, String password, String email, boolean enabled, Collection<Role> roles) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.enabled = enabled;
    this.roles = roles;
  }

  public User(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public User(String username, String password, String email,Collection<Role> roles) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.roles = roles;
  }

  

  public User(Long id, String username, String password, String email, String resetToken, LocalDateTime tokenExpiryDate,
      boolean enabled, Collection<Role> roles) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.email = email;
    this.resetToken = resetToken;
    this.tokenExpiryDate = tokenExpiryDate;
    this.enabled = enabled;
    this.roles = roles;
  }

  

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getResetToken() {
    return resetToken;
  }

  public void setResetToken(String resetToken) {
    this.resetToken = resetToken;
  }

  public LocalDateTime getTokenExpiryDate() {
    return tokenExpiryDate;
  }

  public void setTokenExpiryDate(LocalDateTime tokenExpiryDate) {
    this.tokenExpiryDate = tokenExpiryDate;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public Collection<Role> getRoles() {
    return roles;
  }

  public void setRoles(Collection<Role> roles) {
    this.roles = roles;
  }

  @Override
  public String toString() {
    return "User [id=" + id + ", username=" + username + ", password=" + password + ", email=" + email + ", resetToken="
        + resetToken + ", tokenExpiryDate=" + tokenExpiryDate + ", enabled=" + enabled + ", roles=" + roles + "]";
  }


  
}
