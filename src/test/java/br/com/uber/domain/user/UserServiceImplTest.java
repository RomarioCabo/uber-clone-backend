package br.com.uber.domain.user;

import br.com.uber.domain.provider.PersistenceProvider;
import br.com.uber.util.DomainMockUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    PersistenceProvider persistenceProvider;

    UserService userService;

    @BeforeEach
    public void init() {
        userService = spy(new UserServiceImpl(persistenceProvider));
    }


    @Test
    void shouldThrowExceptionWhenEmailNotFound() {
        UserAuthenticate authenticate = DomainMockUtil.buildUserAuthenticate();

        doThrow(new BusinessRuleException("E-mail informado não encotrado!")).when(userService)
                .authenticateUser(authenticate);

        var exception = assertThrows(BusinessRuleException.class, () -> userService.authenticateUser(authenticate));

        assertEquals("E-mail informado não encotrado!", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAuthenticateAndPasswordNotMatches() {
        UserAuthenticate authenticate = DomainMockUtil.buildUserAuthenticate();
        User user = DomainMockUtil.buildUser(authenticate.getEmail(), "passwordmock", TypeUser.DRIVER);

        lenient().when(persistenceProvider.findUserByEmail(authenticate.getEmail())).thenReturn(user);

        doThrow(new BusinessRuleException("Usuário inválido!")).when(userService).authenticateUser(authenticate);

        var exception = assertThrows(
                BusinessRuleException.class,
                () -> userService.authenticateUser(authenticate)
        );

        assertEquals("Usuário inválido!", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        UserAuthenticate authenticate = DomainMockUtil.buildUserAuthenticate();
        User user = DomainMockUtil.buildUser(authenticate.getEmail(), "passwordmock", TypeUser.DRIVER);

        lenient().when(persistenceProvider.existsEmail(authenticate.getEmail())).thenReturn(true);

        doThrow(new BusinessRuleException("E-mail ja cadastrado em nossa base de dados!"))
                .when(userService).saveUser(user);

        var exception = assertThrows(
                BusinessRuleException.class,
                () -> userService.saveUser(user)
        );

        assertEquals("E-mail ja cadastrado em nossa base de dados!", exception.getMessage());
    }

    @Test
    void shouldAuthenticateUser() {
        UserAuthenticate authenticate = DomainMockUtil.buildUserAuthenticate();

        User user = DomainMockUtil.buildUser(
                authenticate.getEmail(),
                encoderPassword(authenticate.getPassword()),
                TypeUser.DRIVER
        );

        when(persistenceProvider.findUserByEmail(authenticate.getEmail())).thenReturn(user);

        User userAuthenticate = userService.authenticateUser(authenticate);

        assertEquals(user.getName(), userAuthenticate.getName());
        assertEquals(user.getLastName(), userAuthenticate.getLastName());
        assertEquals(user.getTypeUser(), userAuthenticate.getTypeUser());
    }

    private String encoderPassword(String passwordToEncoder) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(passwordToEncoder);
    }
}
