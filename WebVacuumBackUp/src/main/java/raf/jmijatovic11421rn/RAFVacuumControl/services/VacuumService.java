package raf.jmijatovic11421rn.RAFVacuumControl.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import raf.jmijatovic11421rn.RAFVacuumControl.model.ErrorMessage;
import raf.jmijatovic11421rn.RAFVacuumControl.model.ScheduledTask;
import raf.jmijatovic11421rn.RAFVacuumControl.model.Status;
import raf.jmijatovic11421rn.RAFVacuumControl.model.Vacuum;
import raf.jmijatovic11421rn.RAFVacuumControl.repositories.ErrorMessageRepository;
import raf.jmijatovic11421rn.RAFVacuumControl.repositories.VacuumRepository;
import raf.jmijatovic11421rn.RAFVacuumControl.repositories.VacuumSpec;
import raf.jmijatovic11421rn.RAFVacuumControl.services.threads.ScheduledStatusChangeThread;
import raf.jmijatovic11421rn.RAFVacuumControl.services.threads.StatusChangeThread;
import raf.jmijatovic11421rn.RAFVacuumControl.services.threads.WaitingThreadListener;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class VacuumService implements WaitingThreadListener {

    private VacuumRepository vacuumRepository;
    private ErrorMessageRepository errorMessageRepository;

    private TaskScheduler taskScheduler;

    @Autowired
    public VacuumService(VacuumRepository vacuumRepository, ErrorMessageRepository errorMessageRepository, TaskScheduler taskScheduler) {
        this.vacuumRepository = vacuumRepository;
        this.errorMessageRepository = errorMessageRepository;
        this.taskScheduler = taskScheduler;
    }

    public List<Vacuum> searchVacuums(String email, String name, List<Status> status, Date dateFrom, Date dateTo) {
        Specification<Vacuum> spec1 = VacuumSpec.vacuumNameLike(name);
        Specification<Vacuum> spec2 = VacuumSpec.vacuumStatusIn(status);
        Specification<Vacuum> spec3 = VacuumSpec.vacuumAddedByEquals(email);
        Specification<Vacuum> spec4 = VacuumSpec.vacuumCreationDateBetween(dateFrom, dateTo);
        Specification<Vacuum> spec5 = VacuumSpec.vacuumActiveEquals(true);
        Specification<Vacuum> spec = Specification.where(spec1).and(spec2).and(spec4).and(spec3).and(spec5);
        return vacuumRepository.findAll(spec);
    }

    public void startVacuumStatusChange(Long id, Status status) {
        Vacuum vacuum = vacuumRepository.findByVacuumId(id);
        if (vacuum.getStatus().equals(status) && !vacuum.getDirty()) {
            vacuumRepository.lock(vacuum);
            StatusChangeThread t = new StatusChangeThread(id, status.equals(Status.OFF) ? "START" : "STOP");
            t.addListener(this);
            t.start();
        } else {
            errorMessageRepository.save(new ErrorMessage(new Timestamp(new Date().getTime()), vacuum.getVacuumId(), status.equals(Status.OFF) ? "START" : "STOP", status.equals(Status.OFF) ? "Vacuum failed to start" : "Vacuum failed to stop"));
        }
    }

    private void changeVacuumStatus(Long id) {
        Vacuum vacuum = vacuumRepository.findByVacuumId(id);
        if (vacuum.getStatus().equals(Status.OFF)) {
            vacuum.setStatus(Status.ON);
        } else if (vacuum.getStatus().equals(Status.ON)) {
            vacuum.setStatus(Status.OFF);
            vacuum.setCycles(vacuum.getCycles() + 1);
        }
        vacuumRepository.update(vacuum);
        vacuumRepository.lock(vacuum);
        if (vacuum.getCycles() % 3 == 0) {
            startVacuumDischarge(id);
        }
    }

    public void startVacuumDischarge(Long id) {
        Vacuum vacuum = vacuumRepository.findByVacuumId(id);
        if (vacuum.getStatus().equals(Status.OFF) && !vacuum.getDirty()) {
            vacuumRepository.lock(vacuum);
            StatusChangeThread t = new StatusChangeThread(id, "DISCHARGE");
            t.addListener(this);
            t.start();
        } else {
            errorMessageRepository.save(new ErrorMessage(new Timestamp(new Date().getTime()), vacuum.getVacuumId(), "DISCHARGE", "Vacuum failed to discharge"));
        }
    }

    private void dischargeVacuum(Long id) {
        Vacuum vacuum = vacuumRepository.findByVacuumId(id);
        if (vacuum.getStatus().equals(Status.OFF)) {
            vacuum.setStatus(Status.DISCHARGING);
            vacuumRepository.update(vacuum);
            StatusChangeThread t = new StatusChangeThread(id, "DISCHARGE");
            t.addListener(this);
            t.start();
        } else if (vacuum.getStatus().equals(Status.DISCHARGING)) {
            vacuum.setStatus(Status.OFF);
            vacuum.setCycles(0);
            vacuumRepository.update(vacuum);
            vacuumRepository.lock(vacuum);
        }
    }

    public Vacuum addVacuum(Vacuum vacuum) {
        vacuum.setStatus(Status.OFF);
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        vacuum.setAddedBy(email);
        vacuum.setActive(true);
        vacuum.setCreationDate(new Date());
        vacuum.setCycles(0);
        vacuum.setDirty(false);
        return vacuumRepository.save(vacuum);
    }

    public void removeVacuum(Long id) {
        Vacuum v = vacuumRepository.findByVacuumId(id);
        if (v.getStatus().equals(Status.OFF) && !v.getDirty()) {
            v.setActive(false);
            vacuumRepository.save(v);
        }
    }

    public void schedule(ScheduledTask task) {
        ScheduledStatusChangeThread ssst = new ScheduledStatusChangeThread(task.getVacuumId(), task.getAction());
        ssst.addListener(this);
        taskScheduler.schedule(ssst, task.getTime().toInstant());
    }

    @Override
    public void notify(Thread thread, Long id, String action) {
        if (thread instanceof StatusChangeThread) {
            if (action.equals("STOP") || action.equals("START")) {
                changeVacuumStatus(id);
            } else if (action.equals("DISCHARGE")){
                dischargeVacuum(id);
            }
        } else if (thread instanceof ScheduledStatusChangeThread) {
            switch (action) {
                case "START" -> startVacuumStatusChange(id, Status.OFF);
                case "STOP" -> startVacuumStatusChange(id, Status.ON);
                case "DISCHARGE" -> startVacuumDischarge(id);
            }
        }
    }
}
