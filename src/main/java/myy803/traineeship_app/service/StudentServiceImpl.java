package myy803.traineeship_app.service;

import myy803.traineeship_app.domain.Student;
import myy803.traineeship_app.domain.TraineeshipPosition;
import myy803.traineeship_app.mappers.StudentMapper;
import myy803.traineeship_app.mappers.TraineeshipPositionsMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentMapper studentMapper;
    private final TraineeshipPositionsMapper traineeshipPositionsMapper;

    public StudentServiceImpl(StudentMapper studentMapper, TraineeshipPositionsMapper traineeshipPositionsMapper) {
        this.studentMapper = studentMapper;
        this.traineeshipPositionsMapper = traineeshipPositionsMapper;
    }

    @Override
    public Student getOrCreateByUsername(String username) {
        Student s = studentMapper.findByUsername(username);
        return (s != null) ? s : new Student(username);
    }

    @Override
    @Transactional
    public void updateStudentProfile(String username, Student formStudent) {
        Student dbStudent = studentMapper.findByUsername(username);
        if (dbStudent == null) dbStudent = new Student(username);

        dbStudent.setAM(formStudent.getAM());
        dbStudent.setStudentName(formStudent.getStudentName());
        dbStudent.setPreferredLocation(formStudent.getPreferredLocation());
        dbStudent.setInterests(formStudent.getInterests());
        dbStudent.setSkills(formStudent.getSkills());

        dbStudent.setLookingForTraineeship(true);

        studentMapper.save(dbStudent);
    }

    @Override
    @Transactional
    public void updateLogbook(String username, String logText) {
        Student student = studentMapper.findByUsername(username);
        if (student == null || student.getAssignedTraineeship() == null) {
            throw new IllegalStateException("Student has no assigned traineeship");
        }

        TraineeshipPosition position = student.getAssignedTraineeship();
        position.setStudentLogbook(logText);
        traineeshipPositionsMapper.save(position);
    }
}
