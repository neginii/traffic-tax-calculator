package congestion.calculator.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
@Component
@ConfigurationProperties("tax-exempted-vehicles")
public class TaxExemptedVehicles {

    private List<String> vehicles = new ArrayList<>();

}
