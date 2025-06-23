package com.alemdar_energy.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.alemdar_energy.backend.service.PasswordResetService;

@Controller
@RequestMapping("/pas")
public class PasswordResetController {

    private final PasswordResetService resetService;

    @Autowired
    public PasswordResetController(PasswordResetService resetService) {
        this.resetService = resetService;
    }

    // 🟢 1. Kullanıcı "Şifremi Unuttum" sayfasını açar
    @GetMapping("/send-mail")
    public String showEmailForm(Model model) {
        model.addAttribute("email", "");  
        return "user/resetPassword";  
    }

    // 🟢 2. Kullanıcı e-posta adresini girer ve sıfırlama linki alır
    @PostMapping("/reset")
    public String sendResetLink(@RequestParam("email") String email, Model model) {
        try {
            resetService.sendResetToken(email);
            model.addAttribute("message", "Şifre sıfırlama bağlantısı e-posta adresinize gönderildi.");
        } catch (Exception e) {
            model.addAttribute("error", "Geçersiz e-posta adresi veya kullanıcı bulunamadı.");
        }
        return "user/resetPassword";  
    }

    // 🟢 3. Kullanıcı e-postadaki linke tıklayınca yeni şifre sayfasını açar
    @GetMapping("/resetPassword")
    public String showResetForm(@RequestParam("token") String token, Model model) {
        model.addAttribute("token", token);
        model.addAttribute("newPassword", "");
        return "user/reset-form";  
    }

    // 🟢 4. Kullanıcı yeni şifresini belirler ve kaydeder
    @PostMapping("/save")
    public String resetPassword(@RequestParam("token") String token, @RequestParam("newPassword") String newPassword, Model model) {
        try {
            resetService.resetPassword(token, newPassword);
            return "redirect:/showMyLoginPage";  
        } catch (Exception e) {
            model.addAttribute("error", "Şifre sıfırlama başarısız. Token süresi dolmuş olabilir.");
            return "user/reset-form";  
        }
    }
}
