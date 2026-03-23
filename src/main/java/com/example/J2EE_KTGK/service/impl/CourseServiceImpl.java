package com.example.J2EE_KTGK.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.J2EE_KTGK.entity.Category;
import com.example.J2EE_KTGK.entity.Course;
import com.example.J2EE_KTGK.repository.CategoryRepository;
import com.example.J2EE_KTGK.repository.CourseRepository;
import com.example.J2EE_KTGK.service.CourseService;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;

    public CourseServiceImpl(CourseRepository courseRepository, CategoryRepository categoryRepository) {
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Page<Course> getCoursesByPage(int page, int size) {
        int safePage = Math.max(page, 0);
        Pageable pageable = PageRequest.of(safePage, size, Sort.by("id").ascending());
        return courseRepository.findAll(pageable);
    }

    @Override
    public Page<Course> searchCoursesByName(String keyword, int page, int size) {
        int safePage = Math.max(page, 0);
        Pageable pageable = PageRequest.of(safePage, size, Sort.by("id").ascending());
        return courseRepository.findByNameContainingIgnoreCase(keyword == null ? "" : keyword.trim(), pageable);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll(Sort.by("name").ascending());
    }

    @Override
    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay hoc phan co id = " + id));
    }

    @Override
    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public Course updateCourse(Long id, Course course) {
        Course existingCourse = getCourseById(id);
        existingCourse.setName(course.getName());
        existingCourse.setCredits(course.getCredits());
        existingCourse.setLecturer(course.getLecturer());
        existingCourse.setImage(course.getImage());
        existingCourse.setCategory(course.getCategory());
        return courseRepository.save(existingCourse);
    }

    @Override
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }
}
