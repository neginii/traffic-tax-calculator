package congestion.calculator.configuration;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import congestion.calculator.model.TimeCost;
import lombok.Data;

@Data
@Component
@ConfigurationProperties("time-price")
public class TimeCosts {

    private List<TimeCost> timeCosts = new ArrayList<>();

}
