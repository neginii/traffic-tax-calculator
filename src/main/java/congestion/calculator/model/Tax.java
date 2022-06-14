package congestion.calculator.model;


import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Tax {

    private String licensePlateNumber;
    private String vehicleType;

    private LocalDateTime[] eventTime;
    private double tax;

    public Tax(String licensePlateNumber, String vehicleType, LocalDateTime[] events, double tax) {
        this.licensePlateNumber = licensePlateNumber;
        this.vehicleType = vehicleType;
        this.eventTime = events;
        this.tax = tax;
    }

    @Override
    public String toString() {
        return this.vehicleType + " with registration number " + this.licensePlateNumber + " should pay " + this.tax;
    }
}
