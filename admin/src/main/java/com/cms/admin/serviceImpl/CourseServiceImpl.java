package com.cms.admin.serviceImpl;

import com.cms.admin.dto.CourseDto;
import com.cms.admin.entity.Course;
import com.cms.admin.repository.CourseRepository;
import com.cms.admin.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public CourseDto createCourse(CourseDto dto) {
        Course course = new Course();
        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
        course.setInstructorName(dto.getInstructorName());
        course.setRegistrationStartTime(dto.getRegistrationStartTime());
        course.setRegistrationEndTime(dto.getRegistrationEndTime());

        Course saved = courseRepository.save(course);
        return toDto(saved);
    }

    @Override
    public List<CourseDto> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CourseDto getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
        return toDto(course);
    }

    private CourseDto toDto(Course course) {
        CourseDto dto = new CourseDto();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setInstructorName(course.getInstructorName());
        dto.setRegistrationStartTime(course.getRegistrationStartTime());
        dto.setRegistrationEndTime(course.getRegistrationEndTime());
        return dto;
    }
}