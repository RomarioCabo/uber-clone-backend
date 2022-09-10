package br.com.uber.infrastructure.persistence.taxi_shipping;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxiShippingRepository extends JpaRepository<TaxiShippingEntity, UUID> {

    @Modifying
    @Query(value = "UPDATE taxi_shipping ts SET ts.id_driver = :idDriver WHERE ts.id = :id", nativeQuery = true)
    int addDriverInTaxiShipping(@Param("idDriver") UUID idDriver, @Param("id") UUID id);
}
