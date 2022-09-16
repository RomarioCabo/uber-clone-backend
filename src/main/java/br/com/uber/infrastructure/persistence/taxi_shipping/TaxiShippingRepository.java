package br.com.uber.infrastructure.persistence.taxi_shipping;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TaxiShippingRepository extends JpaRepository<TaxiShippingEntity, UUID> {

    @Modifying
    @Query(value = "UPDATE taxi_shipping ts SET ts.id_driver = :idDriver WHERE ts.id = :id", nativeQuery = true)
    int addDriverInTaxiShipping(@Param("idDriver") UUID idDriver, @Param("id") UUID id);

    @Transactional(readOnly = true)
    @Query(value = "select ts\n" +
            "from TaxiShippingEntity ts\n" +
            "      inner join TaxiShippingHistoryEntity tsh on ts.id = tsh.idTaxiShipping\n" +
            "where not exists(select 1\n" +
            "          from TaxiShippingHistoryEntity tsh2\n" +
            "          where tsh.idTaxiShipping = tsh2.idTaxiShipping\n" +
            "          and tsh2.statusRoute in ('DRIVER_ON_WAY', 'TRAVELING', 'FINISHED_ROUTE', 'CANCELED_BY_PASSENGER', 'CANCELED_BY_DRIVER'))")
    List<TaxiShippingEntity> getAllUberEligibleRoutes();
}