/**
 * Test class for HolidayProcessor in the Tool Rental System application.
 *
 * This class contains unit tests for the HolidayProcessor class, ensuring
 * that the holidays are correctly identified based on the holiday rules
 * for Independence Day and Labor Day.
 *
 * Test cases:
 * - Checking if Independence Day and its observed dates are correctly identified as holidays.
 * - Checking if Labor Day (first Monday in September) is correctly identified as a holiday.
 * - Verifying that non-holiday dates are correctly identified as non-holidays.
 *
 * Each test ensures the proper functioning of holiday detection for various dates.
 *
 * @version 1.0
 */
package com.example.toolrental;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HolidayProcessorTest {

    private HolidayProcessor holidayProcessor;

    @BeforeEach
    public void setUp() {
        holidayProcessor = new HolidayProcessor();
        holidayProcessor.addHolidayRule(new IndependenceDayRule());
        holidayProcessor.addHolidayRule(new LaborDayRule());
    }

    @Test
    public void testIndependenceDay() {
        // Independence Day
        assertTrue(holidayProcessor.isHoliday(LocalDate.of(2024, 7, 4)), "2024-07-04 should be a holiday");
        // Independence Day observed on Monday
        assertTrue(holidayProcessor.isHoliday(LocalDate.of(2021, 7, 5)), "2021-07-05 should be a holiday (observed)");
        // Independence Day observed on Friday
        assertTrue(holidayProcessor.isHoliday(LocalDate.of(2020, 7, 3)), "2020-07-03 should be a holiday (observed)");
        // Non-holiday
        assertFalse(holidayProcessor.isHoliday(LocalDate.of(2022, 7, 5)), "2022-07-05 should not be a holiday");
    }

    @Test
    public void testLaborDay() {
        // First Monday in September 2024
        assertTrue(holidayProcessor.isHoliday(LocalDate.of(2024, 9, 2)), "2024-09-02 should be a holiday");
        // First Monday in September 2023
        assertTrue(holidayProcessor.isHoliday(LocalDate.of(2023, 9, 4)), "2023-09-04 should be a holiday");
        // Non-holiday
        assertFalse(holidayProcessor.isHoliday(LocalDate.of(2024, 9, 9)), "2024-09-09 should not be a holiday");
    }
}
