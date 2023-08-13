package com.wanted.wantedpreonboardingbackend.user;

import com.wanted.wantedpreonboardingbackend.user.domain.dto.LoginRequest;
import com.wanted.wantedpreonboardingbackend.user.domain.dto.UserCreateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody @Valid UserCreateRequest userCreateRequest) {
        Long savedUserId = userService.join(userCreateRequest.email(), userCreateRequest.password());

        return ResponseEntity.created(
                URI.create("/users/" + savedUserId)
        ).build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequest loginRequest) {
        String token = userService.login(loginRequest.email(), loginRequest.password());

        return ResponseEntity.ok(token);
    }
}
