package br.com.uber.integration;

import br.com.uber.UberApplication;
import br.com.uber.domain.provider.PersistenceProvider;
import br.com.uber.domain.taxi_shipping.TaxiShipping;
import br.com.uber.domain.taxi_shipping_history.StatusRoute;
import br.com.uber.domain.taxi_shipping_history.TaxiShippingHistory;
import br.com.uber.domain.user.TypeUser;
import br.com.uber.domain.user.User;
import br.com.uber.util.DomainMockUtil;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@ContextConfiguration(classes = UberApplication.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"integtest"})
class TaxiShippingControllerTest {

  private static final String URL = "http://localhost:";

  private static final String CREATE_TAXI_SHIPPING_URN = "/taxi_shipping/create";
  private static final String GET_ALL_UBER_ELIGIBLE_ROUTES = "/get_all_uber_eligible_routes";

  private static final String CONTENT_TYPE = "Content-Type";

  @LocalServerPort
  private int port;

  @Autowired
  private PersistenceProvider provider;

  @Autowired
  private Flyway flyway;

  @Autowired
  private TestRestTemplate testRestTemplate;

  @BeforeEach
  void init() {
    flyway.clean();
    flyway.migrate();
  }

  @Test
  void shouldSaveTaxiShipping() {
    var passenger = insertUser("passenger@gmail.com", TypeUser.PASSENGER);

    HttpHeaders headers = new HttpHeaders();
    headers.set(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

    TaxiShipping taxiShipping = DomainMockUtil.buildTaxiShipping(null, passenger.getId());
    String url = String.format("%s%s%s", URL, port, CREATE_TAXI_SHIPPING_URN);

    ResponseEntity<TaxiShipping> response = testRestTemplate
        .postForEntity(url, new HttpEntity<>(taxiShipping, headers), TaxiShipping.class);

    Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    Assertions.assertNotNull(response.getBody());
    Assertions.assertNotNull(response.getBody().getId());

    Assertions.assertEquals(taxiShipping.getDestination().getStreet(),
        response.getBody().getDestination().getStreet());
    Assertions.assertEquals(taxiShipping.getDestination().getNumber(),
        response.getBody().getDestination().getNumber());
    Assertions.assertEquals(taxiShipping.getDestination().getCity(),
        response.getBody().getDestination().getCity());
    Assertions.assertEquals(taxiShipping.getDestination().getNeighborhood(),
        response.getBody().getDestination().getNeighborhood());
    Assertions.assertEquals(taxiShipping.getDestination().getPostalCode(),
        response.getBody().getDestination().getPostalCode());
    Assertions.assertEquals(taxiShipping.getDestination().getLatitude(),
        response.getBody().getDestination().getLatitude());
    Assertions.assertEquals(taxiShipping.getDestination().getLongitude(),
        response.getBody().getDestination().getLongitude());

    Assertions.assertNull(response.getBody().getDriver());

    Assertions.assertEquals(1, response.getBody().getPassenger().getId());
    Assertions.assertEquals(taxiShipping.getPassenger().getName(),
        response.getBody().getPassenger().getName());
    Assertions.assertEquals(taxiShipping.getPassenger().getLastName(),
        response.getBody().getPassenger().getLastName());
    Assertions.assertEquals(taxiShipping.getPassenger().getEmail(),
        response.getBody().getPassenger().getEmail());
    Assertions.assertEquals(taxiShipping.getPassenger().getPassword(),
        response.getBody().getPassenger().getPassword());
    Assertions.assertEquals(taxiShipping.getPassenger().getTypeUser(),
        response.getBody().getPassenger().getTypeUser());

    TaxiShippingHistory history = getTaxiShippingHistoryById(response.getBody().getId(),
        StatusRoute.WAITING_ACCEPT_DRIVER);

    Assertions.assertEquals(response.getBody().getId(), history.getIdTaxiShipping());
    Assertions.assertEquals(StatusRoute.WAITING_ACCEPT_DRIVER, history.getStatusRoute());
    Assertions.assertNotNull(history.getEventDate());
  }

  @Test
  void shouldReturnAllUberEligibleRoutes() {
    List<User> users = new ArrayList<>();
    List<TaxiShipping> eligibleRoutes = new ArrayList<>();

    for (int i = 0; i < 4; i++) {
      users.add(insertUser(String.format("passenger%s@gmail.com", i), TypeUser.PASSENGER));
    }

    HttpHeaders headers = new HttpHeaders();
    headers.set(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

    String url = String.format("%s%s%s", URL, port, CREATE_TAXI_SHIPPING_URN);

    users.forEach(passenger -> {
      ResponseEntity<TaxiShipping> response = testRestTemplate
          .postForEntity(url,
              new HttpEntity<>(DomainMockUtil.buildTaxiShipping(null, passenger.getId()),
                  headers), TaxiShipping.class);

      eligibleRoutes.add(response.getBody());
    });

    String urlGetRoutes = String.format("%s%s%s", URL, port, GET_ALL_UBER_ELIGIBLE_ROUTES);

    ResponseEntity<TaxiShipping[]> response = testRestTemplate.getForEntity(urlGetRoutes,
        TaxiShipping[].class);

    var routes = Arrays.asList(Objects.requireNonNull(response.getBody()));

    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals(4, routes.size());

    Assertions.assertEquals(eligibleRoutes.get(0).getId(), routes.get(0).getId());
    Assertions.assertEquals(eligibleRoutes.get(0).getDestination().getStreet(),
        routes.get(0).getDestination().getStreet());
    Assertions.assertEquals(eligibleRoutes.get(0).getDestination().getNumber(),
        routes.get(0).getDestination().getNumber());
    Assertions.assertEquals(eligibleRoutes.get(0).getDestination().getState(),
        routes.get(0).getDestination().getState());
    Assertions.assertEquals(eligibleRoutes.get(0).getDestination().getCity(),
        routes.get(0).getDestination().getCity());
    Assertions.assertEquals(eligibleRoutes.get(0).getDestination().getNeighborhood(),
        routes.get(0).getDestination().getNeighborhood());
    Assertions.assertEquals(eligibleRoutes.get(0).getDestination().getPostalCode(),
        routes.get(0).getDestination().getPostalCode());
    Assertions.assertEquals(eligibleRoutes.get(0).getDestination().getLatitude(),
        routes.get(0).getDestination().getLatitude());
    Assertions.assertEquals(eligibleRoutes.get(0).getDestination().getLongitude(),
        routes.get(0).getDestination().getLongitude());
    Assertions.assertNull(routes.get(0).getDriver());
    Assertions.assertEquals(eligibleRoutes.get(0).getPassenger().getId(),
        routes.get(0).getPassenger().getId());
    Assertions.assertNotNull(routes.get(0).getPassenger());
    Assertions.assertNotNull(routes.get(0).getCreatedAt());
  }

  private User insertUser(String email, TypeUser typeUser) {
    User user = DomainMockUtil.buildUser(email, "mockpassord", typeUser);
    var responseDB = provider.saveUser(user);
    Assertions.assertNotNull(responseDB);
    return responseDB;
  }

  private TaxiShippingHistory getTaxiShippingHistoryById(UUID idTaxiShipping,
      StatusRoute statusRoute) {
    TaxiShippingHistory history = provider.findTaxiShippingHistoryByIdTaxiShipping(idTaxiShipping,
        statusRoute);
    Assertions.assertNotNull(history);
    return history;
  }
}
