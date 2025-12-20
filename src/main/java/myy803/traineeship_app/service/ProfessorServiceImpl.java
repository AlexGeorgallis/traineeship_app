package myy803.traineeship_app.service;

import myy803.traineeship_app.domain.Professor;
import myy803.traineeship_app.domain.TraineeshipPosition;
import myy803.traineeship_app.mappers.ProfessorMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class ProfessorServiceImpl implements ProfessorService {

    private final ProfessorMapper professorMapper;

    public ProfessorServiceImpl(ProfessorMapper professorMapper) {
        this.professorMapper = professorMapper;
    }

    @Override
    public Professor getOrCreateProfile(String username) {
        Professor professor = professorMapper.findByUsername(username);
        if (professor == null) {
            professor = new Professor(username);
        }
        return professor;
    }

    @Override
    @Transactional
    public void updateProfile(String username, Professor formProfessor) {

        Professor db = professorMapper.findByUsername(username);
        if (db == null) {
            db = new Professor(username);
        }

        db.setProfessorName(formProfessor.getProfessorName());
        db.setInterests(formProfessor.getInterests());

        professorMapper.save(db);
    }

    // US14
    @Override
    public List<TraineeshipPosition> getSupervisedPositions(String username) {
        Professor professor = professorMapper.findByUsername(username);
        if (professor == null || professor.getSupervisedPositions() == null) {
            return Collections.emptyList();
        }
        return professor.getSupervisedPositions();
    }

}

