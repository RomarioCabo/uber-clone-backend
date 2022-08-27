package br.com.uber.integration;

import br.com.uber.UberApplication;
import br.com.uber.application.exception.ApiError;
import br.com.uber.domain.provider.PersistenceProvider;
import br.com.uber.domain.user.TypeUser;
import br.com.uber.domain.user.User;
import br.com.uber.util.DomainMockUtil;
import java.util.Objects;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@ContextConfiguration(classes = UberApplication.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"integtest"})
class UserControllerTest {

  private static final String URL = "http://localhost:";

  private static final String CREATE_USER_URN = "/user/create";
  private static final String AUTHENTICATE_USER_URN = "/user/authenticate";

  @LocalServerPort private int port;

  @Autowired private PersistenceProvider provider;

  @Autowired private Flyway flyway;

  @Autowired private TestRestTemplate testRestTemplate;

  @BeforeEach
  void init() {
    flyway.clean();
    flyway.migrate();
  }

  @Test
  void shouldThrowExceptionWhenEmailAlreadyExists() {
    insertUser();

    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Type", "application/json;charset=UTF-8");

    User user = DomainMockUtil.buildUser("mock@mock.com", "mockpassord", TypeUser.DRIVER);
    String url = URL + port + CREATE_USER_URN;

    ResponseEntity<ApiError> response = testRestTemplate
        .postForEntity(url, new HttpEntity<>(user, headers), ApiError.class);

    Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    Assertions.assertEquals("E-mail ja cadastrado em nossa base de dados!", Objects
        .requireNonNull(response.getBody()).getReasons().get(0));
  }

  @Test
  void shouldSaveUser() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Type", "application/json;charset=UTF-8");

    User user = DomainMockUtil.buildUser("mock@mock.com", "mockpassord", TypeUser.DRIVER);
    String url = URL + port + CREATE_USER_URN;

    ResponseEntity<User> response =
        testRestTemplate.postForEntity(url, new HttpEntity<>(user, headers), User.class);

    Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertEquals(1, response.getBody().getId());
    Assertions.assertEquals(user.getName(), response.getBody().getName());
    Assertions.assertEquals(user.getLastName(), response.getBody().getLastName());
    Assertions.assertEquals(user.getTypeUser(), response.getBody().getTypeUser());
  }

  @Test
  void shouldThrowExceptionWhenAuthenticateAndEmailNotExists() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Type", "application/json;charset=UTF-8");

    User user = DomainMockUtil.buildUser("mock@mock.com", "mockpassord", TypeUser.DRIVER);
    String url = URL + port + AUTHENTICATE_USER_URN;

    ResponseEntity<ApiError> response = testRestTemplate
        .postForEntity(url, new HttpEntity<>(user, headers), ApiError.class);

    Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    Assertions.assertEquals("E-mail informado não encotrado!", Objects
        .requireNonNull(response.getBody()).getReasons().get(0));
  }

  @Test
  void shouldThrowExceptionWhenAuthenticateAndPasswordNotMatches() {
    insertUser();

    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Type", "application/json;charset=UTF-8");

    User user = DomainMockUtil.buildUser("mock@mock.com", "mockpassord3", TypeUser.DRIVER);
    String url = URL + port + AUTHENTICATE_USER_URN;

    ResponseEntity<ApiError> response = testRestTemplate
        .postForEntity(url, new HttpEntity<>(user, headers), ApiError.class);

    Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    Assertions.assertEquals("Usuário inválido!", Objects
        .requireNonNull(response.getBody()).getReasons().get(0));
  }

  @Test
  void shouldAuthenticateUser() {
    insertUser();

    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Type", "application/json;charset=UTF-8");

    User user = DomainMockUtil.buildUser("mock@mock.com", "mockpassord", TypeUser.DRIVER);
    String url = URL + port + AUTHENTICATE_USER_URN;

    ResponseEntity<User> response = testRestTemplate
        .postForEntity(url, new HttpEntity<>(user, headers), User.class);

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertEquals(1, response.getBody().getId());
    Assertions.assertEquals(user.getName(), response.getBody().getName());
    Assertions.assertEquals(user.getLastName(), response.getBody().getLastName());
    Assertions.assertEquals(user.getTypeUser(), response.getBody().getTypeUser());
    Assertions.assertNotNull(response.getBody().getPassword());
    Assertions.assertNotEquals(user.getPassword(), response.getBody().getPassword());
  }

  private void insertUser() {
    User user = DomainMockUtil.buildUser("mock@mock.com", "mockpassord", TypeUser.DRIVER);
    var responseDB = provider.saveUser(user);
    Assertions.assertNotNull(responseDB);
  }
}
