package br.com.uber.infrastructure.persistence.taxi_shipping_history;

import br.com.uber.infrastructure.persistence.taxi_shipping_history.TaxiShippingHistoryEntity.HistoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxiShippingHistoryRepository
    extends JpaRepository<TaxiShippingHistoryEntity, HistoryId> {

}
