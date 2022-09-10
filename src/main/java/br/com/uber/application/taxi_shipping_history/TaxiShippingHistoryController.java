package br.com.uber.application.taxi_shipping_history;

import br.com.uber.application.exception.ApiError;
import br.com.uber.domain.taxi_shipping_history.TaxiShippingHistory;
import br.com.uber.domain.taxi_shipping_history.TaxiShippingHistoryService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
public class TaxiShippingHistoryController {

    private final TaxiShippingHistoryService service;

    @PostMapping(
            value = "/taxi_shipping_history/create",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = 201, message = "CREATED", response = TaxiShippingHistory.class),
            @ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR", response = ApiError.class),
            @ApiResponse(code = 501, message = "NOT_IMPLEMENTED", response = ApiError.class)
    })
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<TaxiShippingHistory> saveTaxiShippingHistory(@RequestBody TaxiShippingHistory history,
                                                                       @RequestParam(required = false) UUID idDriver) {
        return ResponseEntity.created(null).body(service.saveTaxiShippingHistory(history, idDriver));
    }

    @GetMapping(
            value = "/taxi_shipping_history/passenger/waiting_accept_driver/{idPassenger}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK", response = TaxiShippingHistory.class),
            @ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR", response = ApiError.class),
            @ApiResponse(code = 501, message = "NOT_IMPLEMENTED", response = ApiError.class)
    })
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<TaxiShippingHistory> findTaxiShippingHistoryByIdPassenger(@PathVariable int idPassenger) {
        return ResponseEntity.ok().body(service.findTaxiShippingHistoryByIdPassenger(idPassenger));
    }
}
