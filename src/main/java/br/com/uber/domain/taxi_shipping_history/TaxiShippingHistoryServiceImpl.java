package br.com.uber.domain.taxi_shipping_history;

import br.com.uber.domain.provider.PersistenceProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class TaxiShippingHistoryServiceImpl implements TaxiShippingHistoryService {

    private final PersistenceProvider provider;

    @Override
    public TaxiShippingHistory saveTaxiShippingHistory(TaxiShippingHistory taxiShippingHistory) {
        TaxiShippingHistory history = provider
                .findTaxiShippingHistoryByIdTaxiShipping(taxiShippingHistory.getIdTaxiShipping(),
                        taxiShippingHistory.getStatusRoute());

        if(history != null) {
            return history;
        }

        return provider.saveTaxiShippingHistory(taxiShippingHistory);
    }
}
