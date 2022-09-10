package br.com.uber.domain.taxi_shipping_history;

import br.com.uber.domain.provider.PersistenceProvider;
import br.com.uber.domain.user.BusinessRuleException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class TaxiShippingHistoryServiceImpl implements TaxiShippingHistoryService {

    private final PersistenceProvider provider;

    @Override
    public TaxiShippingHistory saveTaxiShippingHistory(TaxiShippingHistory taxiShippingHistory, UUID idDriver) {
        taxiShippingHistory.setId(null);
        taxiShippingHistory.setEventDate(LocalDateTime.now());

        TaxiShippingHistory history = provider
                .findTaxiShippingHistoryByIdTaxiShipping(taxiShippingHistory.getIdTaxiShipping(),
                        taxiShippingHistory.getStatusRoute());

        if(history != null) {
            return history;
        }

        addDriverInTaxiShipping(taxiShippingHistory, idDriver);

        return provider.saveTaxiShippingHistory(taxiShippingHistory);
    }

    @Override
    public TaxiShippingHistory findTaxiShippingHistoryByIdPassenger(int idPassenger) {
        return provider.findTaxiShippingHistoryByIdPassenger(idPassenger);
    }

    private void addDriverInTaxiShipping(TaxiShippingHistory taxiShippingHistory, UUID idDriver) {
        if(taxiShippingHistory.getStatusRoute() == StatusRoute.DRIVER_ON_WAY) {
            if(idDriver == null) {
                throw new BusinessRuleException("Para o estágio DRIVER_ON_WAY o ID do motorista é obrigatório");
            }

            provider.addDriverInTaxiShipping(idDriver, taxiShippingHistory.getIdTaxiShipping());
        }
    }
}
