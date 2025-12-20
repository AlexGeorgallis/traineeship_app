package myy803.traineeship_app.service;

import myy803.traineeship_app.domain.Student;
import myy803.traineeship_app.mappers.StudentMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentMapper studentMapper;

    public StudentServiceImpl(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
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
}
