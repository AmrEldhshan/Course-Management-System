package com.cms.publicapp.serviceImpl;

import com.cms.publicapp.entity.Course;
import com.cms.publicapp.entity.Enrollment;
import com.cms.publicapp.repository.CourseRepository;
import com.cms.publicapp.repository.EnrollmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceImplTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    private EnrollmentServiceImpl enrollmentService;

    @Test
    void enroll_shouldSucceed_whenWithinRegistrationWindow() {
        Course course = new Course();
        course.setId(1L);
        course.setRegistrationStartTime(LocalDateTime.now().minusDays(1));
        course.setRegistrationEndTime(LocalDateTime.now().plusDays(1));

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.save(any(Enrollment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Enrollment result = enrollmentService.enroll(1L, "Amr");

        assertNotNull(result);
        assertEquals(1L, result.getCourseId());
        assertEquals("Amr", result.getStudentName());
        verify(enrollmentRepository, times(1)).save(any(Enrollment.class));
    }

    @Test
    void enroll_shouldThrow_whenRegistrationNotStartedYet() {
        Course course = new Course();
        course.setId(1L);
        course.setRegistrationStartTime(LocalDateTime.now().plusDays(1));
        course.setRegistrationEndTime(LocalDateTime.now().plusDays(5));

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        RegistrationWindowException ex = assertThrows(
                RegistrationWindowException.class,
                () -> enrollmentService.enroll(1L, "Amr")
        );

        assertTrue(ex.getMessage().contains("has not started yet"));
        verify(enrollmentRepository, never()).save(any());
    }

    @Test
    void enroll_shouldThrow_whenRegistrationAlreadyEnded() {
        Course course = new Course();
        course.setId(1L);
        course.setRegistrationStartTime(LocalDateTime.now().minusDays(5));
        course.setRegistrationEndTime(LocalDateTime.now().minusDays(1));

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        RegistrationWindowException ex = assertThrows(
                RegistrationWindowException.class,
                () -> enrollmentService.enroll(1L, "Amr")
        );

        assertTrue(ex.getMessage().contains("has ended"));
        verify(enrollmentRepository, never()).save(any());
    }

    @Test
    void enroll_shouldThrow_whenCourseDoesNotExist() {
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> enrollmentService.enroll(99L, "Amr")
        );

        verify(enrollmentRepository, never()).save(any());
    }
}