package raf.jmijatovic11421rn.RAFVacuumControl.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import raf.jmijatovic11421rn.RAFVacuumControl.model.Permission;
import raf.jmijatovic11421rn.RAFVacuumControl.model.Permissions;
import raf.jmijatovic11421rn.RAFVacuumControl.model.User;
import raf.jmijatovic11421rn.RAFVacuumControl.services.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = {"http://localhost:4200"})
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?>  createUser(@Valid @RequestBody User user) {
        if (checkPermission(Permissions.can_create_users)) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.userService.save(user));
        }
        List<User> users = this.userService.findAll();
        for (User u : users) {
            if (Objects.equals(u.getEmail(), user.getEmail())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User with email address " + user.getEmail() + " already exists");
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User does not have the permission to create users");
    }

    @GetMapping
    public ResponseEntity<?> allUsers() {
        if (checkPermission(Permissions.can_read_users)) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.userService.findAll());
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User does not have the permission to read users");
    }
    /*
    @GetMapping
    public ResponseEntity<?> allUsers(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size) {
        if (checkPermission(Permissions.can_read_users)) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.userService.paginate(page, size));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User does not have the permission to read users");
    }*/

    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public User me() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return this.userService.findByEmail(username);
    }

    @GetMapping(value = "/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUser(@PathVariable("email") String email) {
        if (checkPermission(Permissions.can_read_users)) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.userService.findByEmail(email));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User does not have the permission to read users");
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUser(@Valid @RequestBody User user) {
        if (checkPermission(Permissions.can_update_users)) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.userService.update(user));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User does not have the permission to update users");
    }

    @DeleteMapping(value = "/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteUser(@PathVariable("email") String email) {
        if (checkPermission(Permissions.can_delete_users)) {
            this.userService.deleteUser(email);
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User does not have the permission to delete users");
    }

    public boolean checkPermission(Permissions permission) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User activeUser = this.userService.findByEmail(username);
        Set<Permission> permissions = activeUser.getPermissions();
        return permissions.contains(new Permission(permission.toString()));
    }
}
