/**
 * LaborDayRule class for the Tool Rental System application.
 *
 * This class implements the HolidayRule interface to define the rules for determining
 * if a given date is the Labor Day holiday. Labor Day is observed on the first Monday
 * of September.
 *
 * Methods:
 * - boolean isHoliday(LocalDate date): Determines if the provided date is Labor Day.
 *
 * Example usage:
 * - Used in the HolidayProcessor class to check if a date is Labor Day.
 *
 * @version 1.0
 */
package com.example.toolrental;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;

public class LaborDayRule implements HolidayRule {

    @Override
    public boolean isHoliday(LocalDate date) {
        LocalDate firstMondayOfSeptember = LocalDate.of(date.getYear(), Month.SEPTEMBER, 1)
                .with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
        return date.equals(firstMondayOfSeptember);
    }
}
