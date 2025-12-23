package myy803.traineeship_app.service;

import myy803.traineeship_app.domain.Student;
import myy803.traineeship_app.domain.TraineeshipPosition;
import myy803.traineeship_app.mappers.StudentMapper;
import myy803.traineeship_app.mappers.TraineeshipPositionsMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommitteeService {

    private final StudentMapper studentMapper;
    private final TraineeshipPositionsMapper positionsMapper;

    public CommitteeService(StudentMapper studentMapper,
                            TraineeshipPositionsMapper positionsMapper) {
        this.studentMapper = studentMapper;
        this.positionsMapper = positionsMapper;
    }

    public List<Student> getPendingApplications() {
        return studentMapper.findByLookingForTraineeshipTrue();
    }

    public List<TraineeshipPosition> getInProgressPositions() {
        return positionsMapper.findByIsAssignedTrue();
    }

    @Transactional
    public void assignPositionToStudent(Integer positionId, String studentUsername) {

        Student student = studentMapper.findByUsername(studentUsername);
        if (student == null) {
            throw new IllegalArgumentException("Student not found: " + studentUsername);
        }

        TraineeshipPosition position =
                positionsMapper.findById(positionId)
                        .orElseThrow(() -> new IllegalArgumentException("Position not found"));

        if (position.isAssigned()) {
            throw new IllegalStateException("Position already assigned");
        }

        position.setAssigned(true);
        position.setStudent(student);

        student.setAssignedTraineeship(position);
        student.setLookingForTraineeship(false);

        positionsMapper.save(position);
    }

    // US21
    public TraineeshipPosition getPositionById(Integer positionId) {
        return positionsMapper.findById(positionId)
                .orElseThrow(() -> new IllegalArgumentException("Position not found"));
    }

    @Transactional
    public void completeTraineeship(Integer positionId, boolean pass) {
        TraineeshipPosition position = getPositionById(positionId);

        position.setPassFailGrade(pass);

        Student student = position.getStudent();
        if (student != null) {

            double traineeshipGrade = pass ? 10.0 : 0.0;

            double currentAvg = student.getAvgGrade();

            double newAvg;
            if (currentAvg <= 0.0) {
                newAvg = traineeshipGrade;
            } else {
                newAvg = (currentAvg + traineeshipGrade) / 2.0;
            }

            student.setAvgGrade(newAvg);
            studentMapper.save(student);
        }

        positionsMapper.save(position);
    }


}

