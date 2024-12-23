package com.task_management.modules.user;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.task_management.exceptions.ExistingUserException;
import com.task_management.modules.user.dtos.UserCreateDTO;
import com.task_management.modules.user.dtos.UserReturnDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserReturnDTO> register(@Valid @RequestBody UserCreateDTO payload)
            throws ExistingUserException {

        UserReturnDTO response = userService.register(payload);

        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<HashMap<String, String>> login(@RequestBody UserEntity payload) {
        HashMap<String, String> response = userService.login(payload);

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping
    public ResponseEntity<List<UserReturnDTO>> read() {
        List<UserReturnDTO> response = userService.read();

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserReturnDTO> getProfile(HttpServletRequest req) {
        UserReturnDTO response = userService.getProfile(req);

        return ResponseEntity.status(200).body(response);
    }
}
