-- Create tables matching the JPA entity mappings, so the seed can run
-- before Spring Boot ever starts (Postgres init scripts run once, at
-- first container creation — before any Java service boots).

CREATE TABLE IF NOT EXISTS courses (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255),
    description VARCHAR(255),
    instructor_name VARCHAR(255),
    registration_start_time TIMESTAMP,
    registration_end_time TIMESTAMP
);

CREATE TABLE IF NOT EXISTS enrollments (
    id BIGSERIAL PRIMARY KEY,
    course_id BIGINT,
    student_name VARCHAR(255),
    enrolled_at TIMESTAMP
);

INSERT INTO courses (title, description, instructor_name, registration_start_time, registration_end_time)
VALUES
('Intro to Spring Boot', 'Learn the fundamentals of Spring Boot', 'Dr. Ahmed', NOW() - INTERVAL '5 days', NOW() + INTERVAL '10 days'),
('Advanced Docker', 'Deep dive into containerization', 'Dr. Salma', NOW() - INTERVAL '3 days', NOW() + INTERVAL '15 days'),
('Database Design', 'Relational database fundamentals', 'Dr. Khaled', NOW(), NOW() + INTERVAL '20 days'),
('Expired Registration Course', 'Registration window already closed', 'Dr. Omar', NOW() - INTERVAL '30 days', NOW() - INTERVAL '5 days'),
('Future Course - Not Open Yet', 'Registration has not started', 'Dr. Layla', NOW() + INTERVAL '10 days', NOW() + INTERVAL '30 days');

INSERT INTO enrollments (course_id, student_name, enrolled_at)
VALUES
(1, 'Sample Student One', NOW() - INTERVAL '1 day'),
(2, 'Sample Student Two', NOW() - INTERVAL '2 hours');