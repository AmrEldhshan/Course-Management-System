package com.cms.publicapp.service;

import com.cms.publicapp.entity.Enrollment;

public interface EnrollmentService {
    Enrollment enroll(Long courseId, String studentName);
}