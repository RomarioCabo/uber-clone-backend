package br.com.uber.domain.taxi_shipping;

import br.com.uber.domain.provider.PersistenceProvider;
import br.com.uber.domain.taxi_shipping_history.StatusRoute;
import br.com.uber.domain.taxi_shipping_history.TaxiShippingHistory;
import br.com.uber.domain.taxi_shipping_history.TaxiShippingHistoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class TaxiShippingServiceImpl implements TaxiShippingService {

  private final PersistenceProvider provider;
  private final TaxiShippingHistoryService historyService;

  @Override
  public TaxiShipping saveTaxiShipping(TaxiShipping taxiShipping) {
    taxiShipping.setCreatedAt(LocalDateTime.now());
    TaxiShipping taxiShippingPersistence = provider.saveTaxiShipping(taxiShipping);

    historyService.saveTaxiShippingHistory(buildTaxiShippingHistory(taxiShippingPersistence.getId()));

    return taxiShippingPersistence;
  }

  private TaxiShippingHistory buildTaxiShippingHistory(UUID idTaxiShipping) {
    return TaxiShippingHistory.builder()
            .idTaxiShipping(idTaxiShipping)
            .statusRoute(StatusRoute.WAITING_ACCEPT_DRIVER)
            .eventDate(LocalDateTime.now())
            .build();
  }
}
