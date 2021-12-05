package app.controller;

import app.container.dto.UserDto;
import app.container.dto.request.UserRegistrationRequest;
import app.container.dto.response.ResponseDto;
import app.container.dto.response.StatusDto;
import app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Validated
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {
    private final UserService userService;

    @GetMapping("/user/list")
    public List<UserDto> getUsersData() {
        return userService.getUsersList();
    }

    @PostMapping("/user/add")
    public ResponseDto addUserData(@RequestBody UserRegistrationRequest userRegistrationRequest) {
        return userService.addUser(userRegistrationRequest);
    }

    @GetMapping("/user/edit")
    public ResponseDto editUserData(@PathVariable UUID uuid,
                                    @RequestBody UserRegistrationRequest userRegistrationRequest) {
        return userService.editUser(uuid, userRegistrationRequest);
    }

    @GetMapping("/user/delete")
    public ResponseDto deleteUserData(@PathVariable UUID uuid) {
        return userService.deleteUser(uuid);
    }

    @GetMapping("/status")
    public StatusDto status() {
        final ResponseEntity<StatusDto> ok = ResponseEntity.ok(userService.getStatusInfo());
        return StatusDto.builder().status(ok.getStatusCode()).build();
    }
}
