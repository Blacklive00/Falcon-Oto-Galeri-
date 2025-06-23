package com.alemdar_energy.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/showMyLoginPage")
  public String showMyLoginPage() {
    return "login/fancy-login";
  }
  
}
