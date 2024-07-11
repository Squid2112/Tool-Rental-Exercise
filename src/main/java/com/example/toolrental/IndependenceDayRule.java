/**
 * IndependenceDayRule class for the Tool Rental System application.
 *
 * This class implements the HolidayRule interface to define the rules for determining
 * if a given date is the Independence Day holiday. It accounts for the observed holiday
 * if July 4th falls on a weekend (Saturday or Sunday).
 *
 * Methods:
 * - boolean isHoliday(LocalDate date): Determines if the provided date is Independence Day
 *   or the observed Independence Day.
 *
 * Example usage:
 * - Used in the HolidayProcessor class to check if a date is Independence Day.
 *
 * @version 1.0
 */
package com.example.toolrental;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

public class IndependenceDayRule implements HolidayRule {

    @Override
    public boolean isHoliday(LocalDate date) {
        LocalDate july4 = LocalDate.of(date.getYear(), Month.JULY, 4);
        DayOfWeek dayOfWeek = july4.getDayOfWeek();

        if (date.equals(july4)) {
            return true;
        }

        if (dayOfWeek == DayOfWeek.SATURDAY && date.equals(july4.minusDays(1))) {
            return true;
        }

        if (dayOfWeek == DayOfWeek.SUNDAY && date.equals(july4.plusDays(1))) {
            return true;
        }

        return false;
    }

}
