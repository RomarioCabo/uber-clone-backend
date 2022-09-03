package br.com.uber.domain.taxi_shipping_history;

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
public class TaxiShippingHistory {

  private UUID idTaxiShipping;
  private StatusRoute statusRoute;
  private LocalDateTime eventDate;
}
