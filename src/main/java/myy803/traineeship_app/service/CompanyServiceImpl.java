package myy803.traineeship_app.service;

import myy803.traineeship_app.domain.Company;
import myy803.traineeship_app.domain.Evaluation;
import myy803.traineeship_app.domain.EvaluationType;
import myy803.traineeship_app.domain.TraineeshipPosition;
import myy803.traineeship_app.mappers.CompanyMapper;
import myy803.traineeship_app.mappers.TraineeshipPositionsMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyMapper companyMapper;
    private final TraineeshipPositionsMapper traineeshipPositionsMapper;

    public CompanyServiceImpl(CompanyMapper companyMapper, TraineeshipPositionsMapper traineeshipPositionsMapper) {
        this.companyMapper = companyMapper;
        this.traineeshipPositionsMapper = traineeshipPositionsMapper;
    }

    @Override
    public Company getOrCreateProfile(String username) {
        Company company = companyMapper.findByUsername(username);
        if (company == null) {
            company = new Company(username);
        }
        return company;
    }

    @Override
    @Transactional
    public void updateProfile(Company formCompany, String username) {
        Company db = companyMapper.findByUsername(username);

        if (db == null) {
            db = new Company(username);
        }

        db.setCompanyName(formCompany.getCompanyName());
        db.setCompanyLocation(formCompany.getCompanyLocation());

        companyMapper.save(db);
    }

    @Override
    public List<TraineeshipPosition> getAvailablePositions(String username) {
        Company company = companyMapper.findByUsername(username);
        return (company != null) ? company.getAvailablePositions() : List.of();
    }

    @Override
    @Transactional
    public void savePositionForCompany(String username, TraineeshipPosition position) {
        Company company = companyMapper.findByUsername(username);

        if (company == null) {
            company = new Company(username);
        }

        position.setCompany(company);
        company.addPosition(position);

        companyMapper.save(company);
    }

    // US9
    @Override
    public List<TraineeshipPosition> getAssignedPositions(String username) {
        Company company = companyMapper.findByUsername(username);
        return (company != null) ? company.getAssignedPositions() : List.of();
    }

    // US11
    @Override
    @Transactional
    public void deletePosition(String username, Integer positionId) {
        TraineeshipPosition position =
                traineeshipPositionsMapper.findById(positionId)
                        .orElseThrow(() -> new IllegalArgumentException("Position not found"));

        if (position.getCompany() == null ||
                !username.equals(position.getCompany().getUsername())) {
            throw new IllegalStateException("Cannot delete position that does not belong to current company");
        }

        if (position.isAssigned()) {
            throw new IllegalStateException("Cannot delete a position that is already assigned to a student");
        }

        Company company = position.getCompany();

        if (company.getPositions() != null) {
            company.getPositions().removeIf(p -> p.getId().equals(positionId));
        }

        traineeshipPositionsMapper.delete(position);
    }

    @Override
    @Transactional
    public void createCompanyEvaluation(String username, Integer positionId, Evaluation formEvaluation) {
        TraineeshipPosition position =
                traineeshipPositionsMapper.findById(positionId)
                        .orElseThrow(() -> new IllegalArgumentException("Position not found"));

        Company company = position.getCompany();
        if (company == null || !username.equals(company.getUsername())) {
            throw new IllegalStateException("Cannot evaluate a position not owned by current company");
        }

        if (!position.isAssigned()) {
            throw new IllegalStateException("Cannot evaluate a position that is not assigned to a student");
        }

        Evaluation eval = new Evaluation();
        eval.setEvaluationType(EvaluationType.COMPANY_EVALUATION);
        eval.setMotivation(formEvaluation.getMotivation());
        eval.setEfficiency(formEvaluation.getEfficiency());
        eval.setEffectiveness(formEvaluation.getEffectiveness());

        List<Evaluation> evaluations = position.getEvaluations();
        if (evaluations == null) {
            evaluations = new ArrayList<>();
            position.setEvaluations(evaluations);
        }
        evaluations.add(eval);

        traineeshipPositionsMapper.save(position);
    }

}
