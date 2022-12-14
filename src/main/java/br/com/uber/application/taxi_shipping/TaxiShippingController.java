package br.com.uber.application.taxi_shipping;

import br.com.uber.application.exception.ApiError;
import br.com.uber.domain.taxi_shipping.TaxiShipping;
import br.com.uber.domain.taxi_shipping.TaxiShippingService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class TaxiShippingController {

  private final TaxiShippingService service;

  @PostMapping(
      value = "/taxi_shipping/create",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiResponses({
      @ApiResponse(code = 201, message = "CREATED", response = TaxiShipping.class),
      @ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR", response = ApiError.class),
      @ApiResponse(code = 501, message = "NOT_IMPLEMENTED", response = ApiError.class)
  })
  @ResponseStatus(value = HttpStatus.CREATED)
  public ResponseEntity<TaxiShipping> saveTaxiShipping(@RequestBody TaxiShipping taxiShipping) {
    return ResponseEntity.created(null).body(service.saveTaxiShipping(taxiShipping));
  }

  @GetMapping(
          value = "/get_all_uber_eligible_routes",
          produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiResponses({
          @ApiResponse(code = 200, message = "OK", response = TaxiShipping[].class),
          @ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR", response = ApiError.class),
          @ApiResponse(code = 501, message = "NOT_IMPLEMENTED", response = ApiError.class)
  })
  @ResponseStatus(value = HttpStatus.OK)
  public ResponseEntity<List<TaxiShipping>> getAllUberEligibleRoutes() {
    return ResponseEntity.ok().body(service.getAllUberEligibleRoutes());
  }
}
