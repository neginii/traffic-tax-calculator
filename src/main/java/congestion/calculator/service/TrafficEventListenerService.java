package congestion.calculator.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import congestion.calculator.model.input.DailyTrafficEventsInput;
import congestion.calculator.repository.DailyTrafficEventsRepository;
import congestion.calculator.repository.tables.DailyTrafficEvents;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class TrafficEventListenerService {

    private static final String TOPIC = "daily-traffic-events";
    private static final String GROUP_ID = "traffic-tax-calculator";
    private final DailyTrafficEventsRepository dailyTrafficEventsRepository;
    private final ObjectMapper objectMapper;

    public TrafficEventListenerService(
            DailyTrafficEventsRepository dailyTrafficEventsRepository,
            ObjectMapper objectMapper) {
        this.dailyTrafficEventsRepository = dailyTrafficEventsRepository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = TOPIC, groupId = GROUP_ID)
    public void consume(
            @Payload String message,
            @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key) throws JsonProcessingException {

        DailyTrafficEventsInput event = objectMapper.readValue(message, DailyTrafficEventsInput.class);
        Optional<DailyTrafficEvents> optionalExistingData = dailyTrafficEventsRepository.findById(key);

        if (optionalExistingData.isEmpty()) {
            DailyTrafficEvents newCarEvent = new DailyTrafficEvents();
            newCarEvent.setLicencePlateNumber(key);
            newCarEvent.setType(event.getType());
            newCarEvent.setEvents(convertToTimestamps(event.getEvents()));
            DailyTrafficEvents record = dailyTrafficEventsRepository.save(newCarEvent);
            log.info("{} | {} | First Time Detection | {} ", key, event.getType(), record.getEvents());
        } else {
            DailyTrafficEvents existingCarEvents = optionalExistingData.get();
            List<Timestamp> events = existingCarEvents.getEvents();
            events.add(Timestamp.valueOf(event.getEvents()[0]));
            existingCarEvents.setEvents(events);
            DailyTrafficEvents record = dailyTrafficEventsRepository.save(existingCarEvents);
            log.info("{} | {} | {} Times Detections | {}", key, event.getType(), record.getEvents().size(), record.getEvents());
        }

    }

    private List<Timestamp> convertToTimestamps(LocalDateTime[] events) {
        return Arrays.stream(events).map(Timestamp::valueOf).collect(Collectors.toList());
    }
}
