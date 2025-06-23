package com.alemdar_energy.backend.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.alemdar_energy.backend.model.Role;
import com.alemdar_energy.backend.model.User;
import com.alemdar_energy.backend.repository.RoleRepo;
import com.alemdar_energy.backend.repository.UserRepo;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final BCryptPasswordEncoder encoder;
    private final RoleRepo roleRepo;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, BCryptPasswordEncoder encoder, RoleRepo roleRepo) {
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.roleRepo = roleRepo;
    }

    @Override
    public User findByUsername(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

   @Override
public User saveUser(User user) {
    if (user.getId() != null) {
        User existingUser = userRepo.findById(user.getId()).orElse(null);
        if (existingUser != null) {
            // Şifre değişmemişse
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                user.setPassword(existingUser.getPassword());
            } else {
                user.setPassword(encoder.encode(user.getPassword()));
            }
        }
    } else {
        // Yeni kullanıcı ise
        user.setPassword(encoder.encode(user.getPassword()));
    }

    user.setEnabled(true);
    user.setResetToken(null);
    user.setTokenExpiryDate(null);

    Set<Role> updatedRoles = user.getRoles().stream()
            .map(role -> roleRepo.findByName(role.getName())
                    .orElseGet(() -> roleRepo.save(new Role(role.getName()))))
            .collect(Collectors.toSet());

    user.setRoles(updatedRoles);
    return userRepo.save(user);
}


    @Override
    public User findUserById(long id) {
        return userRepo.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + id));
    }

    @Override
    public User saveDefUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        user.setEnabled(true);
        user.setTokenExpiryDate(null);
        user.setResetToken(null);

        Role role = roleRepo.findByName("ROLE_USER").orElseGet(() -> {
            Role newRole = new Role();
            newRole.setName("ROLE_USER");
            return roleRepo.save(newRole);
        });

        user.setRoles(Set.of(role));

        return userRepo.save(user);
    }

}
