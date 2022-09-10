package br.com.uber.domain.provider;

import br.com.uber.domain.taxi_shipping.TaxiShipping;
import br.com.uber.domain.taxi_shipping_history.StatusRoute;
import br.com.uber.domain.taxi_shipping_history.TaxiShippingHistory;
import br.com.uber.domain.user.User;

import java.util.UUID;

public interface PersistenceProvider {

  User saveUser(User user);

  boolean existsEmail(String email);

  User findUserByEmail(String email);

  TaxiShipping saveTaxiShipping(TaxiShipping taxiShipping);

  void addDriverInTaxiShipping(UUID idDriver, UUID id);

  TaxiShippingHistory saveTaxiShippingHistory(TaxiShippingHistory taxiShippingHistory);

  TaxiShippingHistory findTaxiShippingHistoryByIdTaxiShipping(UUID idTaxiShipping, StatusRoute statusRoute);

  boolean existsTaxiShippingHistoryByIdTaxiShipping(UUID idTaxiShipping, StatusRoute statusRoute);

  TaxiShippingHistory findTaxiShippingHistoryByIdPassenger(int idPassenger);
}
