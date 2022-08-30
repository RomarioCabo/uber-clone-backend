package br.com.uber.infrastructure.persistence;

import br.com.uber.domain.provider.PersistenceProvider;
import br.com.uber.domain.taxi_shipping.TaxiShipping;
import br.com.uber.domain.user.User;
import br.com.uber.infrastructure.persistence.taxi_shipping.TaxiShippingManager;
import br.com.uber.infrastructure.persistence.user.UserPersistenceManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DatabaseProvider implements PersistenceProvider {

  private final UserPersistenceManager userPersistenceManager;
  private final TaxiShippingManager taxiShippingManager;

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
}
