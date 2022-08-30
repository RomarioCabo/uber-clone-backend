package br.com.uber.domain.taxi_shipping;

import br.com.uber.domain.user.User;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class TaxiShipping {
  private UUID id;
  private Destination destination;
  private User driver;
  private User passenger;
  private LocalDateTime createdAt;

  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @Getter
  public static class Destination {
    String street;
    String number;
    String state;
    String city;
    String neighborhood;
    String postalCode;
    Double latitude;
    Double longitude;
  }
}
