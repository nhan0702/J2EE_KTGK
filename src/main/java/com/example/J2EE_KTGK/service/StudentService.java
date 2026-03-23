package com.example.J2EE_KTGK.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.example.J2EE_KTGK.entity.Student;

public interface StudentService extends UserDetailsService {

    Student registerStudent(Student student);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Student getStudentByUsername(String username);
}
