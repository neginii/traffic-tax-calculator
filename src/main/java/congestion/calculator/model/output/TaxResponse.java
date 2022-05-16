package congestion.calculator.model.output;

import congestion.calculator.model.Tax;
import lombok.Data;

@Data
public class TaxResponse {
    private String error;
    private Tax tax;
    private String message;

    public TaxResponse(String error, Tax tax, String message) {
        this.error = error;
        this.tax = tax;
        this.message = message;
    }
}
