package com.alemdar_energy.backend.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alemdar_energy.backend.model.Role;
import com.alemdar_energy.backend.model.User;
import com.alemdar_energy.backend.repository.RoleRepo;
import com.alemdar_energy.backend.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

  private UserService service;
  private RoleRepo roleService; // Role service eklendi

  @Autowired
  public UserController(UserService service, RoleRepo roleService) {
    this.service = service;
    this.roleService = roleService;
  }

  // Kullanıcıyı aramak için formu gösteren GET metodu
  @GetMapping("/findUserForm")
  public String findUser(Model model) {
    // Kullanıcıyı bulmak için username alacak model ekliyoruz
    model.addAttribute("foundUser", new String());
    return "user/show-user";
  }

  @GetMapping("/showFindingUser")
  public String showFindingUser(@RequestParam("username") String foundUser, Model model) {
    User user = service.findByUsername(foundUser);
    if (user != null) {
      model.addAttribute("userinfo", user);
    } else {
      model.addAttribute("error", "User not found!");
    }
    return "user/show-user-info";
  }

  // Kullanıcı ekleme formunu gösteren GET metodu
  @GetMapping("/showFormForAddUser")
  public String saveUser(Model model) {
    User user = new User();
    model.addAttribute("user", user);

    // Tüm rolleri model'e ekleyerek HTML formunda gösterilmesini sağlıyoruz
    model.addAttribute("allRoles", roleService.findAll());

    return "user/add-user";
  }

  // Kullanıcıyı kaydetme işlemi
  @PostMapping("/save")
  public String saveUser(@ModelAttribute("user") User user) {

    System.out.println("kaydedilmeye çalışılan user : "+user);
    service.saveUser(user);
    // burda başta redirect:/admin/admin-panel; yapmıştım fakat buraya html dosya
    // uzantısını değil url yi yazmamız gerekiyor eğer redirect kullanıyorsak

    return "redirect:/admin/showPanel";
  }

  // Kullanıcı ekleme formunu gösteren GET metodu
  @GetMapping("/showFormForAddDefUser")
  public String saveDefUser(Model model) {
    User user = new User();
    model.addAttribute("user", user);

    // Tüm rolleri model'e ekleyerek HTML formunda gösterilmesini sağlıyoruz
    model.addAttribute("allRoles", roleService.findAll());

    return "user/add-def-user";
  }

  @PostMapping("/register")
  public String saveDefUser(@ModelAttribute("user") User user) {
    service.saveDefUser(user);
    return "redirect:/products/list";
  }

  @GetMapping("/editRoles/{id}")
  public String editUserRoles(@PathVariable("id") Long id, Model model) {
    User user = service.findUserById(id);
    if (user == null) {
      model.addAttribute("error", "User not found!");
      return "redirect:/products/list";
    }

    model.addAttribute("user", user);
    model.addAttribute("allRoles", roleService.findAll()); // Tüm rolleri listele
    return "user/edit-roles";
  }

  @PostMapping("/updateRoles")
  public String updateUserRoles(@RequestParam Long userId, @RequestParam List<Long> roleIds) {
    User user = service.findUserById(userId);
    if (user != null) {
      Set<Role> roles = new HashSet<>(roleService.findAllById(roleIds));
      user.setRoles(roles);
      service.saveUser(user);
    }
    return "redirect:/products/list";
  }

}
