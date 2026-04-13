package com.example.productcrud.controller;

import com.example.productcrud.dto.ChangePasswordRequest;
import com.example.productcrud.dto.EditProfileRequest;
import com.example.productcrud.model.User;
import com.example.productcrud.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {

    private final UserService userService;

    public ProfileController(final UserService userService) {
        this.userService = userService;
    }

    // GET /profile -> Tampilkan halaman view profil user
    @GetMapping("/profile")
    public String profile(Authentication authentication, Model model) {
        User user = userService.findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("User tidak ditemukan"));
        model.addAttribute("user", user);
        return "profile";
    }

    // GET /profile/edit -> Tampilkan form edit profil
    @GetMapping("/profile/edit")
    public String showEditProfileForm(Authentication authentication, Model model) {
        User user = userService.findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("User tidak ditemukan"));

        EditProfileRequest editProfileRequest = new EditProfileRequest();
        editProfileRequest.setFullName(user.getFullName());
        editProfileRequest.setEmail(user.getEmail());
        editProfileRequest.setPhoneNumber(user.getPhoneNumber());
        editProfileRequest.setAddress(user.getAddress());
        editProfileRequest.setBio(user.getBio());
        editProfileRequest.setProfileImageUrl(user.getProfileImageUrl());

        model.addAttribute("editProfileRequest", editProfileRequest);
        model.addAttribute("username", user.getUsername());
        return "edit-profile";
    }

    // POST /profile/edit -> Proses update profil
    @PostMapping("/profile/edit")
    public String processEditProfile(
            @ModelAttribute EditProfileRequest editProfileRequest,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        userService.updateProfile(authentication.getName(), editProfileRequest);
        redirectAttributes.addFlashAttribute("success", "Profil berhasil diperbarui!");
        return "redirect:/profile";
    }

    // GET /profile/change-password -> Tampilkan form ganti password
    @GetMapping("/profile/change-password")
    public String showChangePasswordForm(Model model) {
        model.addAttribute("changePasswordRequest", new ChangePasswordRequest());
        return "change-password";
    }

    // POST /profile/change-password -> Proses ganti password
    @PostMapping("/profile/change-password")
    public String processChangePassword(
            @ModelAttribute ChangePasswordRequest changePasswordRequest,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        String username = authentication.getName();

        if (changePasswordRequest.getOldPassword() == null || changePasswordRequest.getOldPassword().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Password lama tidak boleh kosong");
            return "redirect:/profile/change-password";
        }

        if (changePasswordRequest.getNewPassword() == null || changePasswordRequest.getNewPassword().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Password baru tidak boleh kosong");
            return "redirect:/profile/change-password";
        }

        if (changePasswordRequest.getConfirmNewPassword() == null || changePasswordRequest.getConfirmNewPassword().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Konfirmasi password baru tidak boleh kosong");
            return "redirect:/profile/change-password";
        }

        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmNewPassword())) {
            redirectAttributes.addFlashAttribute("error", "Password baru dan konfirmasi password harus sama");
            return "redirect:/profile/change-password";
        }

        if (changePasswordRequest.getOldPassword().equals(changePasswordRequest.getNewPassword())) {
            redirectAttributes.addFlashAttribute("error", "Password baru tidak boleh sama dengan password lama");
            return "redirect:/profile/change-password";
        }

        boolean success = userService.changePassword(
                username,
                changePasswordRequest.getOldPassword(),
                changePasswordRequest.getNewPassword()
        );

        if (!success) {
            redirectAttributes.addFlashAttribute("error", "Password lama tidak sesuai");
            return "redirect:/profile/change-password";
        }

        redirectAttributes.addFlashAttribute("success", "Password berhasil diubah!");
        return "redirect:/profile";
    }
}