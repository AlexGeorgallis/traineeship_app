package myy803.traineeship_app.service;

import myy803.traineeship_app.domain.Professor;

public interface ProfessorService {

    Professor getOrCreateProfile(String username);

    void updateProfile(String username, Professor formProfessor);
}

