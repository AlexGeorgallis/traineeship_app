package myy803.traineeship_app.service;

import myy803.traineeship_app.domain.Student;

public interface StudentService {
    Student getOrCreateByUsername(String username);
    void updateStudentProfile(String username, Student formStudent);

    // US4
    void updateLogbook(String username, String logText);
}
