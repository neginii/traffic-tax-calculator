package congestion.calculator.repository.tables;


import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.sql.Timestamp;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Table("daily_traffic_events")
public class DailyTrafficEvents {

    @PrimaryKey
    private String licencePlateNumber;

    private List<Timestamp> events;

    private String type;

}
