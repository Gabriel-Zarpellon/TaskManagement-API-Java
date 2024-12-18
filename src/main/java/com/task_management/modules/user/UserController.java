package com.task_management.modules.user;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.task_management.exceptions.ExistingUserException;
import com.task_management.modules.user.dtos.UserCreateDTO;
import com.task_management.modules.user.dtos.UserReturnDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserReturnDTO> register(@Valid @RequestBody UserCreateDTO payload)
            throws ExistingUserException {

        UserEntity user = new UserEntity();

        user.setUsername(payload.getUsername());
        user.setPassword(payload.getPassword());
        user.setName(payload.getName());
        user.setAdmin(payload.isAdmin());

        UserReturnDTO response = userService.register(user);

        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<HashMap<String, String>> login(@RequestBody UserEntity payload) {
        var response = userService.login(payload);

        return ResponseEntity.status(200).body(response);
    }
}
