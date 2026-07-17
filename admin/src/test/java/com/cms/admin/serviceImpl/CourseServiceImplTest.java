package com.cms.admin.serviceImpl;

import com.cms.admin.dto.CourseDto;
import com.cms.admin.entity.Course;
import com.cms.admin.repository.CourseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseServiceImpl courseService;

    @Test
    void createCourse_shouldSaveAndReturnDto() {
        CourseDto dto = new CourseDto();
        dto.setTitle("Intro to Spring");
        dto.setDescription("Learn Spring Boot");
        dto.setInstructorName("Dr. Ahmed");
        dto.setRegistrationStartTime(LocalDateTime.now());
        dto.setRegistrationEndTime(LocalDateTime.now().plusDays(30));

        Course savedCourse = new Course();
        savedCourse.setId(1L);
        savedCourse.setTitle(dto.getTitle());
        savedCourse.setDescription(dto.getDescription());
        savedCourse.setInstructorName(dto.getInstructorName());
        savedCourse.setRegistrationStartTime(dto.getRegistrationStartTime());
        savedCourse.setRegistrationEndTime(dto.getRegistrationEndTime());

        when(courseRepository.save(any(Course.class))).thenReturn(savedCourse);

        CourseDto result = courseService.createCourse(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Intro to Spring", result.getTitle());
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void getAllCourses_shouldReturnListOfDtos() {
        Course course1 = new Course();
        course1.setId(1L);
        course1.setTitle("Course A");

        Course course2 = new Course();
        course2.setId(2L);
        course2.setTitle("Course B");

        when(courseRepository.findAll()).thenReturn(List.of(course1, course2));

        List<CourseDto> result = courseService.getAllCourses();

        assertEquals(2, result.size());
        assertEquals("Course A", result.get(0).getTitle());
        assertEquals("Course B", result.get(1).getTitle());
    }

    @Test
    void getCourseById_shouldReturnDto_whenFound() {
        Course course = new Course();
        course.setId(1L);
        course.setTitle("Intro to Spring");

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        CourseDto result = courseService.getCourseById(1L);

        assertNotNull(result);
        assertEquals("Intro to Spring", result.getTitle());
    }

    @Test
    void getCourseById_shouldThrow_whenNotFound() {
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> courseService.getCourseById(99L));
    }
}