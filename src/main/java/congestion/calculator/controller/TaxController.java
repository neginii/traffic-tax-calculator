package congestion.calculator.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import congestion.calculator.model.Tax;
import congestion.calculator.model.input.TaxRequestInput;
import congestion.calculator.model.output.TaxResponse;
import congestion.calculator.service.TaxCongestionCalculationService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@RestController
//@Api(value = "Tax Calculator Service")
@OpenAPIDefinition(info = @Info(title = "Tax calculation API", version = "0.0.1", description = "this api calculate traffic tax "))

public class TaxController {

    private final TaxCongestionCalculationService taxCongestionCalculationService;

    public TaxController(TaxCongestionCalculationService taxCongestionCalculationService) {
        this.taxCongestionCalculationService = taxCongestionCalculationService;
    }

    @PostMapping("/calculateTax")
    public ResponseEntity<TaxResponse> taxCalculator(@RequestBody TaxRequestInput request) {

        TaxResponse response;

        try {
            response = new TaxResponse(null, taxCongestionCalculationService.getTax(request), "tax calculated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response = new TaxResponse(e.getLocalizedMessage(), new Tax(request.getRegistrationNumber(), request.getVehicleType(), request.getDates(), 0),
                    "Tax calculation failed.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

}






