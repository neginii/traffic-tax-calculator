package congestion.calculator.model.input;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TaxRequestInput implements Serializable {

    private String registrationNumber;
    private String vehicleType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime[] dates;

}
