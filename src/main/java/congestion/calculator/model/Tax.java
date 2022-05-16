package congestion.calculator.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Tax {

    private String registrationNumber;
    private String vehicleType;
    private LocalDateTime[] eventTime;
    private double tax;

    public Tax(String registrationNumber, String vehicleType, LocalDateTime[] eventTime, double tax) {
        this.registrationNumber = registrationNumber;
        this.vehicleType = vehicleType;
        this.eventTime = eventTime;
        this.tax = tax;
    }

    @Override
    public String toString() {
        return this.vehicleType + " with registration number " + this.registrationNumber + "for these dates should pay " + this.tax;
    }
}
