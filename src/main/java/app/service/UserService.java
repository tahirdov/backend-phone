package app.service;

import app.container.dto.UserDto;
import app.container.dto.request.UserRegistrationRequest;
import app.container.dto.response.ResponseDto;
import app.container.dto.response.StatusDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface UserService {
    ResponseDto addUser(UserRegistrationRequest userRegistrationRequest);

    ResponseDto editUser(UUID uuid, UserRegistrationRequest userRegistrationRequest);

    List<UserDto> getUsersList();

    ResponseDto deleteUser(UUID uuid);

    StatusDto getStatusInfo();

}
