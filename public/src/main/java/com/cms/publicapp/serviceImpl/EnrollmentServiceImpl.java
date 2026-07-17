package com.cms.publicapp.serviceImpl;

import com.cms.publicapp.entity.Course;
import com.cms.publicapp.entity.Enrollment;
import com.cms.publicapp.repository.CourseRepository;
import com.cms.publicapp.repository.EnrollmentRepository;
import com.cms.publicapp.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Override
    public Enrollment enroll(Long courseId, String studentName) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + courseId));

        LocalDateTime now = LocalDateTime.now();

        if (course.getRegistrationStartTime() != null && now.isBefore(course.getRegistrationStartTime())) {
            throw new RegistrationWindowException(
                "Registration has not started yet. It opens at " + course.getRegistrationStartTime());
        }

        if (course.getRegistrationEndTime() != null && now.isAfter(course.getRegistrationEndTime())) {
            throw new RegistrationWindowException(
                "Registration period has ended. It closed at " + course.getRegistrationEndTime());
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setCourseId(courseId);
        enrollment.setStudentName(studentName);
        enrollment.setEnrolledAt(now);

        return enrollmentRepository.save(enrollment);
    }
}
