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
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@ContextConfiguration(classes = UberApplication.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"integtest"})
public class TaxiShippingControllerTest {

  private static final String URL = "http://localhost:";

  private static final String CREATE_TAXI_SHIPPING_URN = "/taxi_shipping/create";

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
  void shouldSaveTaxiShipping() {
    var passenger = insertUser("passenger@gmail.com", TypeUser.PASSENGER);

    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Type", "application/json;charset=UTF-8");

    TaxiShipping taxiShipping = DomainMockUtil.buildTaxiShipping(null, passenger.getId());
    String url = URL + port + CREATE_TAXI_SHIPPING_URN;

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

  private User insertUser(String email, TypeUser typeUser) {
    User user = DomainMockUtil.buildUser(email, "mockpassord", typeUser);
    var responseDB = provider.saveUser(user);
    Assertions.assertNotNull(responseDB);
    return responseDB;
  }

  private TaxiShippingHistory getTaxiShippingHistoryById(UUID idTaxiShipping, StatusRoute statusRoute) {
    TaxiShippingHistory history = provider.findTaxiShippingHistoryByIdTaxiShipping(idTaxiShipping, statusRoute);
    Assertions.assertNotNull(history);
    return history;
  }
}
