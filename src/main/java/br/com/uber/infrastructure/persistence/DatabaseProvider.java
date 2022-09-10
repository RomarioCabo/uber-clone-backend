package br.com.uber.infrastructure.persistence;

import br.com.uber.domain.provider.PersistenceProvider;
import br.com.uber.domain.taxi_shipping.TaxiShipping;
import br.com.uber.domain.taxi_shipping_history.StatusRoute;
import br.com.uber.domain.taxi_shipping_history.TaxiShippingHistory;
import br.com.uber.domain.user.User;
import br.com.uber.infrastructure.persistence.taxi_shipping.TaxiShippingManager;
import br.com.uber.infrastructure.persistence.taxi_shipping_history.TaxiShippingHistoryManager;
import br.com.uber.infrastructure.persistence.user.UserPersistenceManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class DatabaseProvider implements PersistenceProvider {

  private final UserPersistenceManager userPersistenceManager;
  private final TaxiShippingManager taxiShippingManager;
  private final TaxiShippingHistoryManager taxiShippingHistoryManager;

  @Override
  public User saveUser(User user) {
    return userPersistenceManager.saveUser(user);
  }

  @Override
  public boolean existsEmail(String email) {
    return userPersistenceManager.existsEmail(email);
  }

  @Override
  public User findUserByEmail(String email) {
    return userPersistenceManager.findUserByEmail(email);
  }

  @Override
  public TaxiShipping saveTaxiShipping(TaxiShipping taxiShipping) {
    return taxiShippingManager.saveTaxiShipping(taxiShipping);
  }

  @Override
  public void addDriverInTaxiShipping(UUID idDriver, UUID id) {
    taxiShippingManager.addDriverInTaxiShipping(idDriver, id);
  }

  @Override
  public TaxiShippingHistory saveTaxiShippingHistory(TaxiShippingHistory taxiShippingHistory) {
    return taxiShippingHistoryManager.saveTaxiShippingHistory(taxiShippingHistory);
  }

  @Override
  public TaxiShippingHistory findTaxiShippingHistoryByIdTaxiShipping(UUID idTaxiShipping, StatusRoute statusRoute) {
    return taxiShippingHistoryManager.findTaxiShippingHistoryByIdTaxiShipping(idTaxiShipping, statusRoute);
  }

  @Override
  public boolean existsTaxiShippingHistoryByIdTaxiShipping(UUID idTaxiShipping, StatusRoute statusRoute) {
    return taxiShippingHistoryManager.existsTaxiShippingHistoryByIdTaxiShipping(idTaxiShipping, statusRoute);
  }

  @Override
  public TaxiShippingHistory findTaxiShippingHistoryByIdPassenger(int idPassenger) {
    return taxiShippingHistoryManager.findTaxiShippingHistoryByIdPassenger(idPassenger);
  }
}
