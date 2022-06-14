package congestion.calculator.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import congestion.calculator.configuration.TaxExemptedVehicles;
import congestion.calculator.configuration.TimeCosts;
import congestion.calculator.configuration.TollFreeDates;
import congestion.calculator.model.FreeDatesInMonth;
import congestion.calculator.model.FreeDatesInYear;
import congestion.calculator.model.Tax;
import congestion.calculator.model.TimeCost;
import congestion.calculator.model.input.TaxRequestInput;

@Service
public class TaxCongestionCalculationService {

    private final TimeCosts timeCosts;
    private final TollFreeDates tollFreeDates;
    private final TaxExemptedVehicles taxExemptedVehicles;

    public TaxCongestionCalculationService(TimeCosts timeCosts, TollFreeDates tollFreeDates, TaxExemptedVehicles taxExemptedVehicles) {
        this.timeCosts = timeCosts;
        this.tollFreeDates = tollFreeDates;
        this.taxExemptedVehicles = taxExemptedVehicles;
    }

    public Tax getTax(TaxRequestInput taxRequestInput) {

        String vehicleType = taxRequestInput.getVehicleType();
        LocalDateTime[] events = taxRequestInput.getEvents();
        Arrays.sort(events);
        LocalDateTime intervalStart = events[0].plusMinutes(60);

        int totalFee = 0;
        int totalPerDay = 0;
        int tempFee = getTollFee(intervalStart.minusMinutes(60), vehicleType);

        for (LocalDateTime event : events) {

            int nextFee = getTollFee(event, vehicleType);

            if (!event.toLocalDate().equals(intervalStart.toLocalDate())) {
                totalFee += totalPerDay;
                intervalStart = event.plusMinutes(60);
                totalPerDay = 0;
                tempFee = getTollFee(intervalStart.minusMinutes(60), vehicleType);
            }

            totalPerDay = calculateTaxForOneDay(intervalStart, totalPerDay, nextFee, tempFee, event);
            if (totalPerDay > 60) {
                totalPerDay = 60;
            }
            tempFee = nextFee;

        }

        totalFee += totalPerDay;

        return new Tax(taxRequestInput.getRegistrationNumber(), vehicleType, events, totalFee);
    }

    private int calculateTaxForOneDay(LocalDateTime intervalStart, int totalFee, int nextFee, int tempFee, LocalDateTime date) {

        if (date.isBefore(intervalStart) || date.isEqual(intervalStart)) {

            if (totalFee > 0) {
                totalFee -= tempFee;
            }

            if (nextFee >= tempFee) {
                tempFee = nextFee;
            }

            totalFee += tempFee;
        } else {
            totalFee += nextFee;
        }

        return totalFee;
    }

    private boolean IsTollFreeVehicle(String vehicle) {
        return vehicle != null && taxExemptedVehicles.getVehicles().contains(vehicle);
    }

    private int getTollFee(LocalDateTime date, String vehicle) {

        if (IsTollFreeDate(date) || IsTollFreeVehicle(vehicle)) {
            return 0;
        }

        List<TimeCost> timeCosts = this.timeCosts.getTimeCosts();

        for (TimeCost timeCost : timeCosts) {
            LocalDateTime startTime = LocalDateTime.of(date.toLocalDate(), LocalTime.parse(timeCost.getStartTime()));
            LocalDateTime endTime = LocalDateTime.of(date.toLocalDate(), LocalTime.parse(timeCost.getEndTime()));

            if ((date.isAfter(startTime) || date.isEqual(startTime)) && (date.isBefore(endTime) || date.minusSeconds(date.getSecond()
            ).isEqual(endTime))) {
                return timeCost.getPrice();
            }
        }
        return 0;
    }

    private Boolean IsTollFreeDate(LocalDateTime date) {
        int day = date.getDayOfMonth();
        int month = date.getMonthValue();
        int dayOfWeek = date.getDayOfWeek().getValue();
        dayOfWeek = dayOfWeek < 7 ? dayOfWeek + 1 : 1;
        if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
            return true;
        }
        int year = date.getYear();
        List<FreeDatesInYear> freeDateInYears = tollFreeDates.getYears();
        for (FreeDatesInYear y : freeDateInYears) {
            if (year == y.getYear()) {
                List<FreeDatesInMonth> freeDayFreeDateInMonths = y.getMonths();
                for (FreeDatesInMonth freeDatesInMonth : freeDayFreeDateInMonths) {
                    if (month == freeDatesInMonth.getMonth()) {
                        List<Integer> freeDays = freeDatesInMonth.getDays();
                        if (freeDays.isEmpty() || freeDays.contains(day) || freeDays.contains(day + 1)) {
                            return true;
                        }

                    }
                }
            }
        }
        return false;
    }
}
