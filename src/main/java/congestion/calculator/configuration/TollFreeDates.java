package congestion.calculator.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import congestion.calculator.model.FreeDatesInYear;
import lombok.Data;

@Data
@Component
@ConfigurationProperties("toll-free-dates")
public class TollFreeDates {

    private List<FreeDatesInYear> years = new ArrayList<>();

}
