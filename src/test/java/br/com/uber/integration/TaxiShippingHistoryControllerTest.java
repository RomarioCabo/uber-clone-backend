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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"integtest"})
public class TaxiShippingHistoryControllerTest {

    private static final String URL = "http://localhost:";

    private static final String CREATE_HISTORY_URN = "/taxi_shipping_history/create";

    @LocalServerPort private int port;

    @Autowired private PersistenceProvider provider;

    @Autowired private Flyway flyway;

    @Autowired private TestRestTemplate testRestTemplate;

    private User passenger;
    private TaxiShipping taxiShipping;

    @BeforeEach
    void init() {
        flyway.clean();
        flyway.migrate();

        passenger = insertUser("passenger@gmail.com", TypeUser.PASSENGER);
        taxiShipping = insertTaxiShipping(passenger.getId());
    }

    @ParameterizedTest
    @CsvSource({"DRIVER_ON_WAY", "TRAVELING", "FINISHED_ROUTE"})
    void shouldSaveHistory(StatusRoute statusRoute) {
        var taxiShippingHistory = DomainMockUtil
                .buildTaxiShippingHistory(taxiShipping.getId(), statusRoute);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json;charset=UTF-8");

        String url = URL + port + CREATE_HISTORY_URN;

        ResponseEntity<TaxiShippingHistory> response = testRestTemplate
                .postForEntity(url, new HttpEntity<>(taxiShippingHistory, headers), TaxiShippingHistory.class);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());

        Assertions.assertNotNull(response.getBody().getId());
        Assertions.assertEquals(taxiShippingHistory.getIdTaxiShipping(), response.getBody().getIdTaxiShipping());
        Assertions.assertEquals(taxiShippingHistory.getStatusRoute(), response.getBody().getStatusRoute());
        Assertions.assertNotNull(response.getBody().getEventDate());
    }

    private User insertUser(String email, TypeUser typeUser) {
        User user = DomainMockUtil.buildUser(email, "mockpassord", typeUser);
        var responseDB = provider.saveUser(user);
        Assertions.assertNotNull(responseDB);
        return responseDB;
    }

    private TaxiShipping insertTaxiShipping(int idPassenger) {
        TaxiShipping taxiShipping = DomainMockUtil.buildTaxiShipping(null, idPassenger);
        var responseDB = provider.saveTaxiShipping(taxiShipping);
        Assertions.assertNotNull(responseDB);
        return responseDB;
    }
}
