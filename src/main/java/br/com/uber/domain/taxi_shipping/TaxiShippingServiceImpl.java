package br.com.uber.domain.taxi_shipping;

import br.com.uber.domain.provider.PersistenceProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class TaxiShippingServiceImpl implements TaxiShippingService {

  private final PersistenceProvider provider;

  @Override
  public TaxiShipping saveTaxiShipping(TaxiShipping taxiShipping) {
    try {
      return provider.saveTaxiShipping(taxiShipping);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
