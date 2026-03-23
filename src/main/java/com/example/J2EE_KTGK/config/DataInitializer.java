package com.example.J2EE_KTGK.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.J2EE_KTGK.entity.Category;
import com.example.J2EE_KTGK.entity.Course;
import com.example.J2EE_KTGK.entity.Role;
import com.example.J2EE_KTGK.entity.Student;
import com.example.J2EE_KTGK.repository.CategoryRepository;
import com.example.J2EE_KTGK.repository.CourseRepository;
import com.example.J2EE_KTGK.repository.RoleRepository;
import com.example.J2EE_KTGK.repository.StudentRepository;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner seedSampleData(RoleRepository roleRepository,
                                            StudentRepository studentRepository,
                                            CategoryRepository categoryRepository,
                                            CourseRepository courseRepository,
                                            PasswordEncoder passwordEncoder) {
        return args -> {
            seedRoles(roleRepository);
            seedAdminAccount(roleRepository, studentRepository, passwordEncoder);
            seedStudentAccount(roleRepository, studentRepository, passwordEncoder);

            if (courseRepository.count() > 0) {
                return;
            }

            Category softwareEngineering = new Category();
            softwareEngineering.setName("Cong nghe phan mem");

            Category informationSystems = new Category();
            informationSystems.setName("He thong thong tin");

            Category computerNetworks = new Category();
            computerNetworks.setName("Mang may tinh");

            categoryRepository.saveAll(List.of(softwareEngineering, informationSystems, computerNetworks));

            List<Course> courses = List.of(
                    createCourse("Lap trinh Java", 3, "Nguyen Van A",
                            "https://images.unsplash.com/photo-1515879218367-8466d910aaa4?auto=format&fit=crop&w=900&q=80",
                            softwareEngineering),
                    createCourse("Phat trien ung dung Web", 3, "Tran Thi B",
                            "https://images.unsplash.com/photo-1461749280684-dccba630e2f6?auto=format&fit=crop&w=900&q=80",
                            softwareEngineering),
                    createCourse("Kiem thu phan mem", 2, "Le Minh C",
                            "https://images.unsplash.com/photo-1516321318423-f06f85e504b3?auto=format&fit=crop&w=900&q=80",
                            softwareEngineering),
                    createCourse("Co so du lieu", 3, "Pham Thi D",
                            "https://images.unsplash.com/photo-1544383835-bda2bc66a55d?auto=format&fit=crop&w=900&q=80",
                            informationSystems),
                    createCourse("Phan tich thiet ke he thong", 3, "Vo Quoc E",
                            "https://images.unsplash.com/photo-1454165804606-c3d57bc86b40?auto=format&fit=crop&w=900&q=80",
                            informationSystems),
                    createCourse("Thuong mai dien tu", 2, "Doan Thi F",
                            "https://images.unsplash.com/photo-1556740749-887f6717d7e4?auto=format&fit=crop&w=900&q=80",
                            informationSystems),
                    createCourse("Mang may tinh", 3, "Bui Van G",
                            "https://images.unsplash.com/photo-1526379095098-d400fd0bf935?auto=format&fit=crop&w=900&q=80",
                            computerNetworks),
                    createCourse("An toan bao mat thong tin", 3, "Hoang Thi H",
                            "https://images.unsplash.com/photo-1510511459019-5dda7724fd87?auto=format&fit=crop&w=900&q=80",
                            computerNetworks),
                    createCourse("Dien toan dam may", 2, "Dang Van I",
                            "https://images.unsplash.com/photo-1451187580459-43490279c0fa?auto=format&fit=crop&w=900&q=80",
                            computerNetworks));

            courseRepository.saveAll(courses);
        };
    }

    private void seedRoles(RoleRepository roleRepository) {
        if (!roleRepository.existsByName("ADMIN")) {
            Role adminRole = new Role();
            adminRole.setName("ADMIN");
            roleRepository.save(adminRole);
        }

        if (!roleRepository.existsByName("STUDENT")) {
            Role studentRole = new Role();
            studentRole.setName("STUDENT");
            roleRepository.save(studentRole);
        }
    }

    private void seedAdminAccount(RoleRepository roleRepository,
                                  StudentRepository studentRepository,
                                  PasswordEncoder passwordEncoder) {
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new IllegalArgumentException("Chua ton tai role ADMIN"));

        Student admin = studentRepository.findByUsername("admin").orElseGet(Student::new);
        admin.setUsername("admin");
        admin.setEmail("admin@j2ee.local");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole(adminRole);
        studentRepository.save(admin);
    }

    private void seedStudentAccount(RoleRepository roleRepository,
                                    StudentRepository studentRepository,
                                    PasswordEncoder passwordEncoder) {
        Role studentRole = roleRepository.findByName("STUDENT")
                .orElseThrow(() -> new IllegalArgumentException("Chua ton tai role STUDENT"));

        Student student = studentRepository.findByUsername("student").orElseGet(Student::new);
        student.setUsername("student");
        student.setEmail("student@j2ee.local");
        student.setPassword(passwordEncoder.encode("student123"));
        student.setRole(studentRole);
        studentRepository.save(student);
    }

    private Course createCourse(String name, Integer credits, String lecturer, String image, Category category) {
        Course course = new Course();
        course.setName(name);
        course.setCredits(credits);
        course.setLecturer(lecturer);
        course.setImage(image);
        course.setCategory(category);
        return course;
    }
}
