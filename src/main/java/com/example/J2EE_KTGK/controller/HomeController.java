package com.example.J2EE_KTGK.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.J2EE_KTGK.entity.Course;
import com.example.J2EE_KTGK.service.CourseService;

import jakarta.validation.Valid;

@Controller
@RequestMapping
public class HomeController {

    private static final int PAGE_SIZE = 5;

    private final CourseService courseService;

    public HomeController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping({"/", "/home", "/courses"})
    public String home(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "") String keyword,
                       Model model) {
        int currentPage = Math.max(page, 1);
        String trimmedKeyword = keyword == null ? "" : keyword.trim();
        Page<Course> coursePage = trimmedKeyword.isBlank()
                ? courseService.getCoursesByPage(currentPage - 1, PAGE_SIZE)
                : courseService.searchCoursesByName(trimmedKeyword, currentPage - 1, PAGE_SIZE);

        model.addAttribute("coursePage", coursePage);
        model.addAttribute("courses", coursePage.getContent());
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", coursePage.getTotalPages());
        model.addAttribute("keyword", trimmedKeyword);
        model.addAttribute("pageNumbers", coursePage.getTotalPages() > 0
                ? java.util.stream.IntStream.rangeClosed(1, coursePage.getTotalPages()).boxed().toList()
                : java.util.List.of());

        return "home";
    }

    @GetMapping("/courses/create")
    public String showCreateForm(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("categories", courseService.getAllCategories());
        model.addAttribute("formTitle", "Them hoc phan");
        model.addAttribute("formAction", "/courses/create");
        return "course-form";
    }

    @PostMapping("/courses/create")
    public String createCourse(@Valid @ModelAttribute("course") Course course,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", courseService.getAllCategories());
            model.addAttribute("formTitle", "Them hoc phan");
            model.addAttribute("formAction", "/courses/create");
            return "course-form";
        }

        courseService.saveCourse(course);
        return "redirect:/home";
    }

    @GetMapping("/courses/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("course", courseService.getCourseById(id));
        model.addAttribute("categories", courseService.getAllCategories());
        model.addAttribute("formTitle", "Cap nhat hoc phan");
        model.addAttribute("formAction", "/courses/edit/" + id);
        return "course-form";
    }

    @PostMapping("/courses/edit/{id}")
    public String updateCourse(@PathVariable Long id,
                               @Valid @ModelAttribute("course") Course course,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", courseService.getAllCategories());
            model.addAttribute("formTitle", "Cap nhat hoc phan");
            model.addAttribute("formAction", "/courses/edit/" + id);
            return "course-form";
        }

        courseService.updateCourse(id, course);
        return "redirect:/home";
    }

    @PostMapping("/courses/delete/{id}")
    public String deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return "redirect:/home";
    }
}
