package com.example.J2EE_KTGK.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.J2EE_KTGK.service.EnrollmentService;

@Controller
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/enroll/{courseId}")
    public String enrollCourse(@PathVariable Long courseId,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes) {
        boolean enrolled = enrollmentService.enrollCourse(authentication.getName(), courseId);

        if (enrolled) {
            redirectAttributes.addFlashAttribute("successMessage", "Dang ky hoc phan thanh cong.");
        } else {
            redirectAttributes.addFlashAttribute("successMessage", "Ban da dang ky hoc phan nay roi.");
        }

        return "redirect:/courses";
    }

    @GetMapping("/my-courses")
    public String myCourses(Authentication authentication, Model model) {
        model.addAttribute("courses", enrollmentService.getEnrolledCourses(authentication.getName()));
        return "my-courses";
    }
}
