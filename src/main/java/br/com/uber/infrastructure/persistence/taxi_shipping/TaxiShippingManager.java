package br.com.uber.infrastructure.persistence.taxi_shipping;

import br.com.uber.domain.taxi_shipping.TaxiShipping;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@AllArgsConstructor
public class TaxiShippingManager {

  private final TaxiShippingRepository repository;

  @Transactional
  public TaxiShipping saveTaxiShipping(TaxiShipping taxiShipping) {
    return repository.saveAndFlush(new TaxiShippingEntity(taxiShipping)).toModel();
  }
}
