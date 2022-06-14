package congestion.calculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CongestionTaxCalculatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(CongestionTaxCalculatorApplication.class, args);
    }


}
