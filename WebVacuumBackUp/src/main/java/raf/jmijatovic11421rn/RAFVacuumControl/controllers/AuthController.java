package raf.jmijatovic11421rn.RAFVacuumControl.controllers;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import raf.jmijatovic11421rn.RAFVacuumControl.requests.LoginRequest;
import raf.jmijatovic11421rn.RAFVacuumControl.responses.LoginResponse;
import raf.jmijatovic11421rn.RAFVacuumControl.services.UserService;
import raf.jmijatovic11421rn.RAFVacuumControl.utils.JwtUtil;

@RestController
@CrossOrigin(origins = {"http://localhost:4200"})
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), DigestUtils.sha256Hex(loginRequest.getPassword())));
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(new LoginResponse(jwtUtil.generateToken(loginRequest.getEmail())));
    }

}
