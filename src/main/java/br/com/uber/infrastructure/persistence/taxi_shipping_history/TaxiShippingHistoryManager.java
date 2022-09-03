package br.com.uber.infrastructure.persistence.taxi_shipping_history;

import br.com.uber.domain.taxi_shipping_history.TaxiShippingHistory;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@AllArgsConstructor
public class TaxiShippingHistoryManager {

  private final TaxiShippingHistoryRepository repository;

  @Transactional
  public TaxiShippingHistory saveTaxiShippingHistory(TaxiShippingHistory taxiShippingHistory){
    return repository.saveAndFlush(new TaxiShippingHistoryEntity(taxiShippingHistory)).toModel();
  }
}
