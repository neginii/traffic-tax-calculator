package congestion.calculator.model;


import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Tax {

    @Id
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Tax tax = (Tax) o;
        return registrationNumber != null && Objects.equals(registrationNumber, tax.registrationNumber);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
