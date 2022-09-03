package br.com.uber.infrastructure.persistence.taxi_shipping_history;

import br.com.uber.domain.taxi_shipping_history.StatusRoute;
import br.com.uber.domain.taxi_shipping_history.TaxiShippingHistory;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "taxi_shipping_history")
public class TaxiShippingHistoryEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", updatable = false, unique = true, nullable = false)
  private UUID id;

  @Column(name = "id_taxi_shipping", nullable = false)
  private UUID idTaxiShipping;

  @Column(name = "status_route", nullable = false, unique = true)
  @Convert(converter = EnumStatusRouteAttributeConverter.class)
  private StatusRoute statusRoute;

  @Column(name = "event_date", nullable = false)
  private LocalDateTime eventDate;

  TaxiShippingHistoryEntity(TaxiShippingHistory taxiShippingHistory) {
    this.id = taxiShippingHistory.getId();
    this.idTaxiShipping = taxiShippingHistory.getIdTaxiShipping();
    this.statusRoute = taxiShippingHistory.getStatusRoute();
    this.eventDate = taxiShippingHistory.getEventDate();
  }

  public TaxiShippingHistory toModel() {
    return TaxiShippingHistory.builder()
            .id(this.id)
            .idTaxiShipping(this.idTaxiShipping)
            .statusRoute(this.statusRoute)
            .eventDate(this.eventDate)
            .build();
  }
}
