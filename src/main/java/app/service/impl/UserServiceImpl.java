package app.service.impl;

import app.container.dto.UserDto;
import app.container.dto.request.UserRegistrationRequest;
import app.container.dto.response.ResponseDto;
import app.container.dto.response.StatusDto;
import app.container.entities.UserEntity;
import app.container.enums.OperationStatuses;
import app.container.enums.OperationTypes;
import app.repositories.UserRepository;
import app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static app.container.enums.OperationTypes.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public ResponseDto addUser(UserRegistrationRequest userRegistrationRequest) {
        var userDto = mapFromRegistrationRequestToDto(userRegistrationRequest);
        var userEntity = getEntity(userDto);
        var status = save(userEntity);
        return getResponse(status, userEntity.getId(), OPERATION_ADD);
    }

    @Override
    public ResponseDto editUser(UUID uuid, UserRegistrationRequest userRegistrationRequest) {
        var user = mapFromRegistrationRequestToDto(userRegistrationRequest);
        var userFromRepo = repository.findById(uuid);
        if (userFromRepo.isEmpty()) return getResponse(false, uuid, OPERATION_EDIT);
        userFromRepo.get().setName(user.getName());
        userFromRepo.get().setPhone(user.getPhone());
        var status = save(userFromRepo.get());
        return getResponse(status, uuid, OPERATION_EDIT);
    }

    @Override
    public List<UserDto> getUsersList() {
        var collect = repository.findAll().stream()
                .map(this::mapFromEntityToDto)
                .collect(Collectors.toList());
        log.info("LOG: {}", collect);
        return collect;
    }

    @Override
    public ResponseDto deleteUser(UUID uuid) {
        boolean status = true;
        try {
            repository.deleteById(uuid);
        } catch (Exception exception) {
            status = false;
        }
        return getResponse(status, uuid, OPERATION_DELETE);
    }

    @Override
    public StatusDto getStatusInfo() {
        repository.findAll();
        return StatusDto.builder()
                .status(HttpStatus.OK)
                .build();
    }

    private ResponseDto getResponse(boolean status, UUID id, OperationTypes operation) {
        return ResponseDto.builder()
                .user_id(id)
                .operation_type(operation)
                .operation_status(getStatus(status)).build();
    }


    private OperationStatuses getStatus(boolean status) {
        return (status) ? OperationStatuses.SUCCESS : OperationStatuses.FAIL;
    }

    private boolean save(UserEntity userEntity) {
        try {
            repository.save(userEntity);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private UserEntity getEntity(UserDto user) {
        return UserEntity.builder()
                .name(user.getName())
                .phone(user.getPhone()).build();
    }

    private UserDto mapFromEntityToDto(UserEntity userEntity) {
        return UserDto.builder()
                .name(userEntity.getName())
                .user_id(userEntity.getId())
                .phone(userEntity.getPhone()).build();
    }

    private UserDto mapFromRegistrationRequestToDto(UserRegistrationRequest userRegistrationRequest) {
        return UserDto.builder()
                .name(userRegistrationRequest.getName())
                .phone(userRegistrationRequest.getPhone())
                .build();
    }
}
