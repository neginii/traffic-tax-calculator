package congestion.calculator.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import congestion.calculator.model.Tax;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class KafkaProducerService {

    private static final String TOPIC = "daily-tax-calculation";
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }


    public void sendTaxCalculation(String key, Tax tax) {

        try {
            this.kafkaTemplate.send(TOPIC, key, this.objectMapper.writeValueAsString(tax));
            log.info("Licence Plate: {} | Calculated TAX: {}", tax.getLicensePlateNumber(), tax.getTax());
        } catch (JsonProcessingException e) {
            log.error("Error while trying to calculate tax for License Plate {}", key);
            throw new RuntimeException(e);
        }
    }
}
