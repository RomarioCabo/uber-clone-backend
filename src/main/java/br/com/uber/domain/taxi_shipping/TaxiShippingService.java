package br.com.uber.domain.taxi_shipping;

import java.util.List;

public interface TaxiShippingService {

  TaxiShipping saveTaxiShipping(TaxiShipping taxiShipping);

  List<TaxiShipping> getAllUberEligibleRoutes();
}
