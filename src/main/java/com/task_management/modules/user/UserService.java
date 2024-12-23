package com.task_management.modules.user;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.task_management.exceptions.ExistingUserException;
import com.task_management.modules.user.dtos.UserReturnDTO;
import com.task_management.security.JwtTokenService;

@Service
public class UserService {
    @Autowired
    UserRepository userRepo;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private ModelMapper modelMapper;

    public UserReturnDTO register(@RequestBody UserEntity payload) throws ExistingUserException {
        Optional<UserEntity> existingUser = userRepo.findByUsername(payload.getUsername());

        if (existingUser.isPresent()) {
            throw new ExistingUserException();
        }

        var builder = UserEntity.builder()
                .username(payload.getUsername())
                .password(encoder.encode(payload.getPassword()))
                .name(payload.getName())
                .admin(payload.getAdmin());

        if (payload.getAdmin()) {
            var role = Arrays.asList("ADMIN");
            var admin = builder.roles(role).build();
            var user = userRepo.save(admin);

            return modelMapper.map(user, UserReturnDTO.class);
        }

        var role = Arrays.asList("COMMON");
        var common = builder.roles(role).build();
        var user = userRepo.save(common);

        return modelMapper.map(user, UserReturnDTO.class);
    }

    public HashMap<String, String> login(UserEntity payload) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(payload.getUsername(),
                payload.getPassword());

        Authentication authentication = authenticationManager.authenticate(authToken);
        String token = jwtTokenService.createToken(authentication);

        HashMap<String, String> message = new HashMap<String, String>();
        message.put("token", token);

        return message;
    }

    public List<UserReturnDTO> read() {
        List<UserEntity> users = userRepo.findAll();

        return Arrays.asList(modelMapper.map(users, UserReturnDTO[].class));
    }
}
