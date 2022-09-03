package br.com.uber.infrastructure.persistence.taxi_shipping_history;

import br.com.uber.domain.taxi_shipping_history.StatusRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface TaxiShippingHistoryRepository
    extends JpaRepository<TaxiShippingHistoryEntity, UUID> {

    @Transactional(readOnly = true)
    @Query(value = "select tsh from TaxiShippingHistoryEntity tsh where tsh.idTaxiShipping = :idTaxiShipping " +
            "and tsh.statusRoute = :statusRoute")
    TaxiShippingHistoryEntity findTaxiShippingHistoryByIdTaxiShipping(
            @Param("idTaxiShipping") UUID idTaxiShipping,
            @Param("statusRoute") StatusRoute statusRoute
    );
}
