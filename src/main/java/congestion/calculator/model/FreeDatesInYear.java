package congestion.calculator.model;


import java.util.List;

import lombok.Data;

@Data
public class FreeDatesInYear {

    int year;
    List<FreeDatesInMonth> months;

}
