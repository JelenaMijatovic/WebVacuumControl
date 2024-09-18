package raf.jmijatovic11421rn.RAFVacuumControl.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import raf.jmijatovic11421rn.RAFVacuumControl.services.ErrorMessageService;

@RestController
@RequestMapping("/errors")
@CrossOrigin(origins = {"http://localhost:4200"})
public class ErrorMessageController {

    private ErrorMessageService errorMessageService;

    @Autowired
    public ErrorMessageController(ErrorMessageService errorMessageService) {
        this.errorMessageService = errorMessageService;
    }

    @GetMapping
    public ResponseEntity<?> getErrorMessages() {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.errorMessageService.getErrorMessages());
    }
}
