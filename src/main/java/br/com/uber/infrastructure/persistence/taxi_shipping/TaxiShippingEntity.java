package br.com.uber.infrastructure.persistence.taxi_shipping;

import br.com.uber.domain.taxi_shipping.TaxiShipping;
import br.com.uber.infrastructure.persistence.user.UserEntity;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Table(name = "taxi_shipping")
public class TaxiShippingEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", updatable = false, unique = true, nullable = false)
  private UUID id;

  @Type(type = "jsonb")
  @Column(name = "destination", columnDefinition = "jsonb", nullable = false)
  private Destination destination;

  @ManyToOne
  @JoinColumn(name = "id_driver")
  private UserEntity driver;

  @ManyToOne
  @JoinColumn(name = "id_passenger", nullable = false)
  private UserEntity passenger;

  @Column(name = "created_at", nullable = false)
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

    public TaxiShipping.Destination toModel() {
      return TaxiShipping.Destination.builder()
          .street(this.street)
          .number(this.number)
          .state(this.state)
          .city(this.city)
          .neighborhood(this.neighborhood)
          .postalCode(this.postalCode)
          .latitude(this.latitude)
          .longitude(this.longitude)
          .build();
    }
  }

  TaxiShippingEntity(TaxiShipping taxiShipping) {
    this.id = taxiShipping.getId();
    this.destination = buildDestination(taxiShipping.getDestination());
    this.driver = buildDriver(taxiShipping);
    this.passenger = buildPassanger(taxiShipping);
    this.createdAt = taxiShipping.getCreatedAt();
  }

  public TaxiShipping toModel() {
    return TaxiShipping.builder()
        .id(this.getId())
        .destination(this.getDestination().toModel())
        .driver(this.getDriver() != null ? this.getDriver().toModel() : null)
        .passenger(this.getPassenger().toModel())
        .createdAt(this.getCreatedAt())
        .build();
  }

  private Destination buildDestination(TaxiShipping.Destination destination) {
    return Destination.builder()
        .street(destination.getStreet())
        .number(destination.getNumber())
        .state(destination.getState())
        .city(destination.getCity())
        .neighborhood(destination.getNeighborhood())
        .postalCode(destination.getPostalCode())
        .latitude(destination.getLatitude())
        .longitude(destination.getLongitude())
        .build();
  }

  private UserEntity buildDriver(TaxiShipping taxiShipping) {
    return taxiShipping.getDriver() != null ? UserEntity.builder()
        .id(taxiShipping.getDriver().getId())
        .name(taxiShipping.getDriver().getName())
        .lastName(taxiShipping.getDriver().getLastName())
        .email(taxiShipping.getDriver().getEmail())
        .password(taxiShipping.getDriver().getPassword())
        .typeUser(taxiShipping.getDriver().getTypeUser())
        .build() : null;
  }

  private UserEntity buildPassanger(TaxiShipping taxiShipping) {
    return taxiShipping.getPassenger() != null ? UserEntity.builder()
        .id(taxiShipping.getPassenger().getId())
        .name(taxiShipping.getPassenger().getName())
        .lastName(taxiShipping.getPassenger().getLastName())
        .email(taxiShipping.getPassenger().getEmail())
        .password(taxiShipping.getPassenger().getPassword())
        .typeUser(taxiShipping.getPassenger().getTypeUser())
        .build() : null;
  }
}
