package com.example.J2EE_KTGK.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.J2EE_KTGK.entity.Category;
import com.example.J2EE_KTGK.entity.Course;

public interface CourseService {

    Page<Course> getCoursesByPage(int page, int size);

    Page<Course> searchCoursesByName(String keyword, int page, int size);

    List<Category> getAllCategories();

    Course getCourseById(Long id);

    Course saveCourse(Course course);

    Course updateCourse(Long id, Course course);

    void deleteCourse(Long id);
}
