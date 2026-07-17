package com.cms.admin.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String instructorName;

    private LocalDateTime registrationStartTime;
    private LocalDateTime registrationEndTime;

    // Getters and setters
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