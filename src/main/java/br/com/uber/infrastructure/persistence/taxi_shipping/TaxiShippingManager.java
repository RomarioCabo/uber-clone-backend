package br.com.uber.infrastructure.persistence.taxi_shipping;

import br.com.uber.domain.taxi_shipping.TaxiShipping;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class TaxiShippingManager {

  private final TaxiShippingRepository repository;

  @Transactional
  public TaxiShipping saveTaxiShipping(TaxiShipping taxiShipping) {
    return repository.saveAndFlush(new TaxiShippingEntity(taxiShipping)).toModel();
  }

  @Transactional
  public void addDriverInTaxiShipping(UUID idDriver, UUID id) {
    var affectedRows = repository.addDriverInTaxiShipping(idDriver, id);

    if (affectedRows != 1) {
      var msg = String.format("Error on update driver (affected %d rows)", affectedRows);
      throw new InternalError(msg);
    }
  }

  public List<TaxiShipping> getAllUberEligibleRoutes() {
    var taxiShipping = repository.getAllUberEligibleRoutes();
    return taxiShipping == null || taxiShipping.isEmpty() ? null : taxiShipping.stream()
            .map(TaxiShippingEntity::toModel).collect(Collectors.toList());
  }
}
