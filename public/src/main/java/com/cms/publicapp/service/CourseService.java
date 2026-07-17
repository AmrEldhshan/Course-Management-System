package com.cms.publicapp.service;

import com.cms.publicapp.entity.Course;
import java.util.List;

public interface CourseService {
    List<Course> getAllCourses();
    Course getCourseById(Long id);
}