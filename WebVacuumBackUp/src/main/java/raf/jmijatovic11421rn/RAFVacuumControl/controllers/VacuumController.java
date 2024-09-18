package raf.jmijatovic11421rn.RAFVacuumControl.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import raf.jmijatovic11421rn.RAFVacuumControl.model.*;
import raf.jmijatovic11421rn.RAFVacuumControl.services.UserService;
import raf.jmijatovic11421rn.RAFVacuumControl.services.VacuumService;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/vacuums")
@CrossOrigin(origins = {"http://localhost:4200"})
public class VacuumController {

    private VacuumService vacuumService;
    private UserService userService;

    @Autowired
    public VacuumController(VacuumService vacuumService, UserService userService) {
        this.vacuumService = vacuumService;
        this.userService = userService;
    }

    @GetMapping(value="/search")
    public ResponseEntity<?> searchVacuums(@RequestParam(name = "name", required = false) String name, @RequestParam(name = "status", required = false) List<Status> status, @RequestParam(name = "datefrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date datefrom, @RequestParam(name = "dateto", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateto) {
        if (checkPermission(Permissions.can_search_vacuum)) {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.vacuumService.searchVacuums(email, name, status, datefrom, dateto));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User does not have the permission to search vacuums");
    }

    @GetMapping(value="/{id}/start")
    public ResponseEntity<?> startVacuum(@PathVariable Long id) {
        if (checkPermission(Permissions.can_start_vacuum)) {
            this.vacuumService.startVacuumStatusChange(id, Status.OFF);
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User does not have the permission to start vacuums");
    }

    @GetMapping(value="/{id}/stop")
    public ResponseEntity<?> stopVacuum(@PathVariable Long id) {
        if (checkPermission(Permissions.can_stop_vacuum)) {
            this.vacuumService.startVacuumStatusChange(id, Status.ON);
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User does not have the permission to stop vacuums");
    }

    @GetMapping(value="/{id}/discharge")
    public ResponseEntity<?> dischargeVacuum(@PathVariable Long id) {
        if (checkPermission(Permissions.can_discharge_vacuum)) {
            this.vacuumService.startVacuumDischarge(id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User does not have the permission to discharge vacuums");
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addVacuum(@Valid @RequestBody Vacuum vacuum) {
        if (checkPermission(Permissions.can_add_vacuum)) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.vacuumService.addVacuum(vacuum));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User does not have the permission to add vacuums");
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> removeVacuum(@PathVariable Long id) {
        if (checkPermission(Permissions.can_remove_vacuum)) {
            this.vacuumService.removeVacuum(id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User does not have the permission to remove vacuums");
    }

    @PostMapping(value = "/schedule", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> schedule(@Valid @RequestBody ScheduledTask task) {
        if ((task.getAction().equals("START") && checkPermission(Permissions.can_start_vacuum)) ||
                (task.getAction().equals("STOP") && checkPermission(Permissions.can_stop_vacuum)) ||
                        (task.getAction().equals("DISCHARGE") && checkPermission(Permissions.can_discharge_vacuum))) {
            vacuumService.schedule(task);
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User does not have the permission to " + task.getAction().toLowerCase() + " vacuums");
    }

    public boolean checkPermission(Permissions permission) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User activeUser = this.userService.findByEmail(email);
        Set<Permission> permissions = activeUser.getPermissions();
        return permissions.contains(new Permission(permission.toString()));
    }

}
