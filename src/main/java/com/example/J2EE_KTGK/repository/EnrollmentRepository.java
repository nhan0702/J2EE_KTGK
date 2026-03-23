package com.example.J2EE_KTGK.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.J2EE_KTGK.entity.Enrollment;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

    @Query("select e from Enrollment e join fetch e.course c left join fetch c.category where e.student.username = :username order by c.id asc")
    List<Enrollment> findByStudentUsername(String username);
}
