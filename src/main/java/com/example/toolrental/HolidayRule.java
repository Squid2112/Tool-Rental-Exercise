/**
 * HolidayRule interface for the Tool Rental System application.
 *
 * This interface defines the contract for determining if a given date is a holiday.
 * Implementations of this interface will provide specific logic to check for holidays.
 *
 * Method:
 * - boolean isHoliday(LocalDate date): Determines if the provided date is a holiday.
 *
 * Example usage:
 * - Implemented by specific holiday rule classes such as IndependenceDayRule and LaborDayRule.
 * - Used in the HolidayProcessor class to aggregate multiple holiday rules.
 *
 * @version 1.0
 */
package com.example.toolrental;

import java.time.LocalDate;

public interface HolidayRule {

    boolean isHoliday(LocalDate date);
}
