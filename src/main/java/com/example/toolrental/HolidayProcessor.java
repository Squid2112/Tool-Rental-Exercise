/**
 * HolidayProcessor class for the Tool Rental System application.
 *
 * This class is responsible for managing and determining holidays based on provided holiday rules.
 * It maintains a list of HolidayRule objects and checks if a given date is a holiday according to these rules.
 *
 * Methods:
 * - addHolidayRule(HolidayRule rule): Adds a holiday rule to the processor.
 * - isHoliday(LocalDate date): Checks if a given date is a holiday based on the configured holiday rules.
 *
 * Example usage:
 * - Used in the ChargeProcessor and RentalAgreement classes to determine if a date is a holiday, affecting charge calculations.
 *
 * @version 1.0
 */
package com.example.toolrental;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HolidayProcessor {

    private final List<HolidayRule> holidayRules = new ArrayList<>();

    public void addHolidayRule(HolidayRule rule) {
        holidayRules.add(rule);
    }

    public boolean isHoliday(LocalDate date) {
        for (HolidayRule rule : holidayRules) {
            if (rule.isHoliday(date)) {
                return true;
            }
        }
        return false;
    }
}
