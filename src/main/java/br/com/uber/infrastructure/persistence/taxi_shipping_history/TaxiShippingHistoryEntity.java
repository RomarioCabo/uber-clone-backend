package br.com.uber.infrastructure.persistence.taxi_shipping_history;

import br.com.uber.domain.taxi_shipping_history.StatusRoute;
import br.com.uber.domain.taxi_shipping_history.TaxiShippingHistory;
import br.com.uber.infrastructure.persistence.taxi_shipping_history.TaxiShippingHistoryEntity.HistoryId;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(HistoryId.class)
@Table(name = "taxi_shipping_history")
public class TaxiShippingHistoryEntity {

  @Id
  @Column(name = "id_taxi_shipping", unique = true, nullable = false)
  private UUID idTaxiShipping;

  @Id
  @Column(name = "status", nullable = false)
  @Convert(converter = EnumStatusRouteAttributeConverter.class)
  private StatusRoute statusRoute;

  @Column(name = "event_date", nullable = false)
  private LocalDateTime eventDate;

  @AllArgsConstructor
  @NoArgsConstructor
  static class HistoryId implements Serializable {
    private UUID idTaxiShipping;
    private StatusRoute statusRoute;

    static HistoryId fromTaxiShippingHistory(TaxiShippingHistory taxiShippingHistory) {
      return new HistoryId(taxiShippingHistory.getIdTaxiShipping(),
          taxiShippingHistory.getStatusRoute());
    }
  }

  TaxiShippingHistoryEntity(TaxiShippingHistory taxiShippingHistory) {
    this.idTaxiShipping = taxiShippingHistory.getIdTaxiShipping();
    this.statusRoute = taxiShippingHistory.getStatusRoute();
    this.eventDate = taxiShippingHistory.getEventDate();
  }

  public TaxiShippingHistory toModel() {
    return TaxiShippingHistory.builder()
        .idTaxiShipping(this.idTaxiShipping)
        .statusRoute(this.statusRoute)
        .eventDate(this.eventDate)
        .build();
  }
}
