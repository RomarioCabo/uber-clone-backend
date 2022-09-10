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

    @Transactional(readOnly = true)
    @Query(value = "select tsh\n" +
            "from TaxiShippingHistoryEntity tsh\n" +
            "         inner join TaxiShippingEntity ts on ts.id = tsh.idTaxiShipping\n" +
            "where ts.passenger.id = :idPassenger\n" +
            "  and tsh.statusRoute = 'WAITING_ACCEPT_DRIVER'\n" +
            "  and not exists(select 1\n" +
            "                 from TaxiShippingHistoryEntity tsh2\n" +
            "                 where tsh.idTaxiShipping = tsh2.idTaxiShipping\n" +
            "                   and tsh2.statusRoute in ('FINISHED_ROUTE', 'CANCELED_BY_PASSENGER', 'CANCELED_BY_DRIVER'))")
    TaxiShippingHistoryEntity findTaxiShippingHistoryByIdPassenger(@Param("idPassenger") int idPassenger);
}
