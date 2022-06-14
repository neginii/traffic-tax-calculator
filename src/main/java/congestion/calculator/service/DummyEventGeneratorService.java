package congestion.calculator.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import congestion.calculator.model.input.DailyTrafficEventsInput;

/*
 This service is only demonstration purposes, in reality there will other microservices
 which are in charge of collecting events from traffic cameras and populating the entry topic.
 */

@Service
public class DummyEventGeneratorService {

    private static final String TOPIC = "daily-traffic-events";
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public DummyEventGeneratorService(ObjectMapper objectMapper, KafkaTemplate<String, String> kafkaTemplate) {
        this.objectMapper = objectMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelay = 1000)
    public void test() throws JsonProcessingException {

        String alphabetic = RandomStringUtils.randomAlphabetic(3).toUpperCase();
        String numeric = RandomStringUtils.randomNumeric(3);
        String licensePlateNumber = alphabetic + " " + numeric;

        DailyTrafficEventsInput event = new DailyTrafficEventsInput();
        event.setLicencePlateNumber(licensePlateNumber);
        event.setType("CAR");
        event.setEvents(new LocalDateTime[]{(LocalDateTime.now().minusHours(8))});

        sendMessage(licensePlateNumber, objectMapper.writeValueAsString(event));

    }

    public void sendMessage(String key, String message) {
        this.kafkaTemplate.send(TOPIC, key, message);
    }

}
