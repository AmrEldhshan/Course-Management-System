package com.cms.admin.dto;

import java.time.LocalDateTime;

public class CourseDto {

    private Long id;
    private String title;
    private String description;
    private String instructorName;
    private LocalDateTime registrationStartTime;
    private LocalDateTime registrationEndTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getInstructorName() { return instructorName; }
    public void setInstructorName(String instructorName) { this.instructorName = instructorName; }

    public LocalDateTime getRegistrationStartTime() { return registrationStartTime; }
    public void setRegistrationStartTime(LocalDateTime t) { this.registrationStartTime = t; }

    public LocalDateTime getRegistrationEndTime() { return registrationEndTime; }
    public void setRegistrationEndTime(LocalDateTime t) { this.registrationEndTime = t; }
}