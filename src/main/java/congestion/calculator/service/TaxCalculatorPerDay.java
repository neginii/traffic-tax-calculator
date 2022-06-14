package congestion.calculator.service;

import congestion.calculator.model.Tax;
import congestion.calculator.model.input.TaxRequestInput;
import congestion.calculator.repository.DailyTrafficEventsRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;

@Log4j2
@Service
public class TaxCalculatorPerDay {

    private final DailyTrafficEventsRepository dailyTrafficEventsRepository;
    private final TaxCongestionCalculationService taxCongestionCalculationService;
    private final KafkaProducerService kafkaProducerService;

    public TaxCalculatorPerDay(
            DailyTrafficEventsRepository dailyTrafficEventsRepository,
            TaxCongestionCalculationService taxCongestionCalculationService,
            KafkaProducerService kafkaProducerService) {
        this.dailyTrafficEventsRepository = dailyTrafficEventsRepository;
        this.taxCongestionCalculationService = taxCongestionCalculationService;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Scheduled(cron = "0 0 22 * * MON-FRI")
    public void calculateTaxPerDay() {

        dailyTrafficEventsRepository.findAll().forEach(record -> {
            TaxRequestInput taxRequestInput = new TaxRequestInput();
            taxRequestInput.setRegistrationNumber(record.getLicencePlateNumber());
            taxRequestInput.setVehicleType(record.getType());
            taxRequestInput.setEvents(convertToLocalDateTimes((record.getEvents()).toArray(new Timestamp[record.getEvents().size()])));
            Tax tax = taxCongestionCalculationService.getTax(taxRequestInput);
            kafkaProducerService.sendTaxCalculation(record.getLicencePlateNumber(), tax);
        });

        // Cleaning up daily records after tax calculation to be ready for the next day
        dailyTrafficEventsRepository.deleteAll();
    }

    private LocalDateTime[] convertToLocalDateTimes(Timestamp[] events) {
        return Arrays.stream(events).map(Timestamp::toLocalDateTime).toArray(LocalDateTime[]::new);
    }
}
