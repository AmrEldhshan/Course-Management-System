package com.cms.publicapp.controller;

import com.cms.publicapp.entity.Enrollment;
import com.cms.publicapp.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping
    public ResponseEntity<Enrollment> enroll(@RequestBody EnrollmentRequest request) {
        Enrollment enrollment = enrollmentService.enroll(request.getCourseId(), request.getStudentName());
        return ResponseEntity.ok(enrollment);
    }

    // simple inline request DTO — small enough not to need a separate file
    public static class EnrollmentRequest {
        private Long courseId;
        private String studentName;

        public Long getCourseId() { return courseId; }
        public void setCourseId(Long courseId) { this.courseId = courseId; }
        public String getStudentName() { return studentName; }
        public void setStudentName(String studentName) { this.studentName = studentName; }
    }
}