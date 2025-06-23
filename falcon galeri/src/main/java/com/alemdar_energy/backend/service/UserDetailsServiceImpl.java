package com.alemdar_energy.backend.service;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.alemdar_energy.backend.model.Role;
import com.alemdar_energy.backend.model.User;
import com.alemdar_energy.backend.repository.UserRepo;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepo userRepo;

  @Autowired
  public UserDetailsServiceImpl(UserRepo userRepo) {
    this.userRepo = userRepo;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepo.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

    return new org.springframework.security.core.userdetails.User(
        user.getUsername(),
        user.getPassword(),
        mapToAuthorities(user.getRoles()));
  }

  private Collection<? extends GrantedAuthority> mapToAuthorities(Collection<Role> roles) {
    return roles.stream()
        .map(role -> new SimpleGrantedAuthority(role.getName()))
        .collect(Collectors.toList());
  }

}

// @Service
// public class UserDetailsServiceImpl implements UserDetailsService {

// private UserRepo userRepo;
// private BCryptPasswordEncoder encoder;
// private RoleRepo roleRepo;

// @Autowired
// public UserDetailsServiceImpl(UserRepo userRepo, BCryptPasswordEncoder
// encoder, RoleRepo roleRepo) {
// this.userRepo = userRepo;
// this.encoder = encoder;
// this.roleRepo = roleRepo;
// }

// @Override
// public UserDetails loadUserByUsername(String username) throws
// UsernameNotFoundException {
// User user = findByUsername(username);
// if (user == null) {
// throw new UsernameNotFoundException("username is not found"+username);
// }
// return new
// org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),mapToAuthorities(user.getRoles()));
// }

// @Override
// public User findByUsername(String username) {
// return userRepo.findByUsername(username)
// .orElseThrow(() -> new UsernameNotFoundException("User not found: " +
// username));
// }

// @Override
// public User saveUser(User user) {
// String hashedPassword = encoder.encode(user.getPassword());
// user.setPassword(hashedPassword);
// user.setEnabled(true);
// user.setResetToken(null);
// user.setTokenExpiryDate(null);

// if (user.getRoles() != null) {
// HashSet<Role> updatedRoles = new HashSet<>();
// user.getRoles().stream().forEach(role ->{ if (role.getId()!= null) {
// Optional<Role>existingRole= roleRepo.findByName(role.getName());
// if (existingRole.isPresent()) {
// updatedRoles.add(existingRole.get());
// }
// }else{
// Role newRole = new Role();
// updatedRoles.add(newRole);
// }
// });

// user.setRoles(updatedRoles);
// }
// return userRepo.save(user);
// }

// @Override
// public User saveDefUser(User user) {

// String hashedPassword = encoder.encode(user.getPassword());
// user.setPassword(hashedPassword);
// user.setEnabled(true);
// user.setTokenExpiryDate(null);
// user.setResetToken(null);

// Role role = roleRepo.findByName("ROLE_USER").get();
// if (role ==null) {
// role = new Role();
// role.setName("ROLE_USER");
// roleRepo.save(role);
// }
// user.setRoles(Set.of(role));

// return userRepo.save(user);

// }

// private Collection<? extends GrantedAuthority>
// mapToAuthorities(Collection<Role> roles) {
// return roles.stream().map(role -> new
// SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
// }

// @Override
// public User findUserById(long id) {
// return userRepo.findById(id).get();
// }

// }
