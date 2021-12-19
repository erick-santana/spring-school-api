package br.com.alura.school.controller;

import br.com.alura.school.controller.request.NewUserRequest;
import br.com.alura.school.controller.response.UserResponse;
import br.com.alura.school.model.User;
import br.com.alura.school.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.net.URI;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/users/{username}")
    public ResponseEntity<UserResponse> userByUsername(@PathVariable("username") String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("User %s not found", username)));
        return ResponseEntity.ok(new UserResponse(user));
    }

    @PostMapping("/users")
    public ResponseEntity<Void> newUser(@RequestBody @Valid NewUserRequest newUserRequest) {
        userRepository.save(newUserRequest.toEntity());
        URI location = URI.create(format("/users/%s", newUserRequest.getUsername()));
        return ResponseEntity.created(location).build();
    }

}
