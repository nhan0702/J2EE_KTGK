package com.example.J2EE_KTGK.service;

import java.util.List;

import com.example.J2EE_KTGK.entity.Course;

public interface EnrollmentService {

    boolean enrollCourse(String username, Long courseId);

    List<Course> getEnrolledCourses(String username);
}
