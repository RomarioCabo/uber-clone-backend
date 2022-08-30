package br.com.uber.domain.provider;

import br.com.uber.domain.taxi_shipping.TaxiShipping;
import br.com.uber.domain.user.User;

public interface PersistenceProvider {

  User saveUser(User user);

  boolean existsEmail(String email);

  User findUserByEmail(String email);

  TaxiShipping saveTaxiShipping(TaxiShipping taxiShipping);
}
