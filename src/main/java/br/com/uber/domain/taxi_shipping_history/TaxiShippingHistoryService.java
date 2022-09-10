package br.com.uber.domain.taxi_shipping_history;

import java.util.UUID;

public interface TaxiShippingHistoryService {
    TaxiShippingHistory saveTaxiShippingHistory(TaxiShippingHistory taxiShippingHistory, UUID idDriver);

    TaxiShippingHistory findTaxiShippingHistoryByIdPassenger(int idPassenger);
}
