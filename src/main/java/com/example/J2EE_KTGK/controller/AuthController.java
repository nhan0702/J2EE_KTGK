package com.example.J2EE_KTGK.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.J2EE_KTGK.entity.Student;
import com.example.J2EE_KTGK.service.StudentService;

import jakarta.validation.Valid;

@Controller
public class AuthController {

    private final StudentService studentService;

    public AuthController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm(@RequestParam(defaultValue = "false") boolean success, Model model) {
        if (!model.containsAttribute("student")) {
            model.addAttribute("student", new Student());
        }
        model.addAttribute("success", success);
        return "register";
    }

    @PostMapping("/register")
    public String registerStudent(@Valid @ModelAttribute("student") Student student,
                                  BindingResult bindingResult,
                                  Model model) {
        if (studentService.existsByUsername(student.getUsername())) {
            bindingResult.rejectValue("username", "duplicate", "Username da ton tai");
        }

        if (studentService.existsByEmail(student.getEmail())) {
            bindingResult.rejectValue("email", "duplicate", "Email da ton tai");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("success", false);
            return "register";
        }

        studentService.registerStudent(student);
        return "redirect:/login?registered";
    }
}
