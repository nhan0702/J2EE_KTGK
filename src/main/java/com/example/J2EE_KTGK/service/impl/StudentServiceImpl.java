package com.example.J2EE_KTGK.service.impl;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.J2EE_KTGK.entity.Role;
import com.example.J2EE_KTGK.entity.Student;
import com.example.J2EE_KTGK.repository.RoleRepository;
import com.example.J2EE_KTGK.repository.StudentRepository;
import com.example.J2EE_KTGK.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentServiceImpl(StudentRepository studentRepository,
                              RoleRepository roleRepository,
                              PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Student registerStudent(Student student) {
        Role studentRole = roleRepository.findByName("STUDENT")
                .orElseThrow(() -> new IllegalArgumentException("Chua ton tai role STUDENT"));

        student.setRole(studentRole);
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        return studentRepository.save(student);
    }

    @Override
    public boolean existsByUsername(String username) {
        return studentRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return studentRepository.existsByEmail(email);
    }

    @Override
    public Student getStudentByUsername(String username) {
        return studentRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Khong tim thay tai khoan: " + username));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Student student = getStudentByUsername(username);
        String resolvedRole = student.getRoleName();

        if ((resolvedRole == null || resolvedRole.isBlank()) && student.getRole() != null) {
            resolvedRole = student.getRole().getName();
        }

        if (resolvedRole == null || resolvedRole.isBlank()) {
            throw new UsernameNotFoundException("Tai khoan chua duoc gan quyen hop le: " + username);
        }

        return new User(
                student.getUsername(),
                student.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + resolvedRole)));
    }
}
