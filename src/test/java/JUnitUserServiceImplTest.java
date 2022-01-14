import app.container.dto.UserDto;
import app.container.dto.request.UserRegistrationRequest;
import app.container.dto.response.ResponseDto;
import app.container.entities.UserEntity;
import app.container.enums.OperationStatuses;
import app.container.enums.OperationTypes;
import app.repositories.UserRepository;
import app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RequiredArgsConstructor
@ExtendWith({MockitoExtension.class})

public class JUnitUserServiceImplTest {
    @Mock
    private UserRepository repo;
    @InjectMocks
    private UserService service;

    private final UserEntity user = createNewUser();
    private final UserDto dto = getDto();
    private final UserRegistrationRequest req = createNewRegister();
    private final UUID id = UUID.randomUUID();


    private UserRegistrationRequest createNewRegister() {
        return UserRegistrationRequest.builder()
                .name("Tahir")
                .phone("994555555555")
                .build();
    }

    private UserDto getDto() {
        return new UserDto(id, "UserName", "12345678");
    }

    private UserEntity createNewUser() {
        return new UserEntity(id, "UserName", "12345678");
    }

    @Before
    public void set ()
    { when(repo.save(any())).thenReturn(any()); }

    @Test
    public void addUser(){
        ResponseDto responseDto = ResponseDto.builder()
                .operation_type(OperationTypes.OPERATION_ADD)
                .operation_status(OperationStatuses.SUCCESS)
                .build();
        Assertions.assertNotNull(service.addUser(req));
        Assertions.assertEquals(service.addUser(req), responseDto);
    }

    @Test
    public void getStatus(){
        when(repo.findAll()).thenReturn(Collections.singletonList(user));
        Assertions.assertNotNull(service.getStatusInfo());
        Assertions.assertEquals(200, service.getStatusInfo().getStatus().value());
    }

    @Test
    public void editUser() {
        when(repo.findById(any())).thenReturn(Optional.of(user));
        Assertions.assertNotNull(service.editUser(id, req));
    }

    @Test
    public void getUsers() {
        when(repo.findAll()).thenReturn(Collections.singletonList(user));
        Assertions.assertNotNull(service.getUsersList());
    }

    @Test
    public void deleteUser(){
        when(repo.findById(any())).thenReturn(Optional.of(user));
        Assertions.assertNotNull(service.deleteUser(createNewUser().getId()));
    }

}
