package raf.jmijatovic11421rn.RAFVacuumControl.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import raf.jmijatovic11421rn.RAFVacuumControl.model.ErrorMessage;
import raf.jmijatovic11421rn.RAFVacuumControl.model.Vacuum;
import raf.jmijatovic11421rn.RAFVacuumControl.repositories.ErrorMessageRepository;
import raf.jmijatovic11421rn.RAFVacuumControl.repositories.VacuumRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ErrorMessageService {
    private ErrorMessageRepository errorMessageRepository;
    private VacuumRepository vacuumRepository;

    @Autowired
    public ErrorMessageService(ErrorMessageRepository errorMessageRepository, VacuumRepository vacuumRepository) {
        this.errorMessageRepository = errorMessageRepository;
        this.vacuumRepository = vacuumRepository;
    }

    public List<ErrorMessage> getErrorMessages() {
        List<ErrorMessage> errors = new ArrayList<>();
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Vacuum> vacuums = this.vacuumRepository.findAllByAddedBy(email);
        for (Vacuum v : vacuums) {
            errors.addAll(this.errorMessageRepository.findAllByVacuumId(v.getVacuumId()));
        }
        return errors;
    }
}
