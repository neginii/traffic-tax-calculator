package congestion.calculator.model;

import java.util.List;

import lombok.Data;

@Data
public class FreeDatesInMonth {

    int month;
    List<Integer> days;

}
