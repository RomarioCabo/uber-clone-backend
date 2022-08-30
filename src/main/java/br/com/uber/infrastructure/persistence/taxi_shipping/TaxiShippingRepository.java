package br.com.uber.infrastructure.persistence.taxi_shipping;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxiShippingRepository extends JpaRepository<TaxiShippingEntity, UUID> {

}
