package br.com.uber.util;

import br.com.uber.domain.taxi_shipping.TaxiShipping;
import br.com.uber.domain.user.TypeUser;
import br.com.uber.domain.user.User;
import java.time.LocalDateTime;

public class DomainMockUtil {

  public static User buildUser(String email, String password, TypeUser typeUser) {

    return User.builder()
        .name("Mock Name")
        .lastName("Mock Last Name")
        .email(email)
        .password(password)
        .typeUser(typeUser)
        .build();
  }

  public static TaxiShipping buildTaxiShipping(Integer idDriver, Integer idPassenger) {
    return TaxiShipping.builder()
        .destination(buildDestination())
        .driver(idDriver != null ? User.builder().id(idDriver).build() : null)
        .passenger(User.builder().id(idPassenger).build())
        .createdAt(LocalDateTime.now())
        .build();
  }

  private static TaxiShipping.Destination buildDestination() {
    return TaxiShipping.Destination.builder()
        .street("Rua Corronel Perdigão Sobrinho")
        .number("241")
        .state("CE")
        .city("Russas")
        .neighborhood("Centro")
        .postalCode("62900-000")
        .latitude(-4.945766092381092)
        .longitude(-37.97288549493051)
        .build();
  }
}
