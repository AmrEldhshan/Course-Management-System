package com.cms.admin.service;

import com.cms.admin.dto.CourseDto;
import java.util.List;

public interface CourseService {
    CourseDto createCourse(CourseDto courseDto);
    List<CourseDto> getAllCourses();
    CourseDto getCourseById(Long id);
}