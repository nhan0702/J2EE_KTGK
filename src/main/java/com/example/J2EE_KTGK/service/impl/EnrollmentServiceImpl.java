package com.example.J2EE_KTGK.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.J2EE_KTGK.entity.Course;
import com.example.J2EE_KTGK.entity.Enrollment;
import com.example.J2EE_KTGK.entity.Student;
import com.example.J2EE_KTGK.repository.CourseRepository;
import com.example.J2EE_KTGK.repository.EnrollmentRepository;
import com.example.J2EE_KTGK.repository.StudentRepository;
import com.example.J2EE_KTGK.service.EnrollmentService;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository,
                                 StudentRepository studentRepository,
                                 CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public boolean enrollCourse(String username, Long courseId) {
        Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay sinh vien: " + username));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay hoc phan co id = " + courseId));

        if (enrollmentRepository.existsByStudentIdAndCourseId(student.getId(), course.getId())) {
            return false;
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollmentRepository.save(enrollment);
        return true;
    }

    @Override
    public List<Course> getEnrolledCourses(String username) {
        return enrollmentRepository.findByStudentUsername(username)
                .stream()
                .map(Enrollment::getCourse)
                .toList();
    }
}
