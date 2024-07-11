/**
 * Unit tests for the IndependenceDayRule class.
 *
 * This class tests the functionality of the IndependenceDayRule class,
 * which implements the HolidayRule interface to determine if a given date
 * is Independence Day or the observed Independence Day.
 *
 * Tests included:
 * - testIndependenceDay: Verifies that July 4th is recognized as Independence Day.
 * - testObservedIndependenceDayOnSaturday: Verifies that July 3rd is recognized as the observed holiday when July 4th is on a Saturday.
 * - testObservedIndependenceDayOnSunday: Verifies that July 5th is recognized as the observed holiday when July 4th is on a Sunday.
 * - testNonHoliday: Verifies that a date that is not Independence Day or the observed Independence Day is not recognized as a holiday.
 *
 * @version 1.0
 */
package com.example.toolrental;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class IndependenceDayRuleTest {

    private final IndependenceDayRule independenceDayRule = new IndependenceDayRule();

    @Test
    public void testIndependenceDay() {
        LocalDate july4th = LocalDate.of(2024, Month.JULY, 4);
        assertTrue(independenceDayRule.isHoliday(july4th), "July 4, 2024 should be a holiday");
    }

    @Test
    public void testObservedIndependenceDayOnSaturday() {
        LocalDate july3rd = LocalDate.of(2020, Month.JULY, 3);
        assertTrue(independenceDayRule.isHoliday(july3rd), "July 3, 2020 should be the observed holiday");

        LocalDate july4th = LocalDate.of(2020, Month.JULY, 4);
        assertTrue(independenceDayRule.isHoliday(july4th), "July 4, 2020 should be a holiday");

        LocalDate july5th = LocalDate.of(2020, Month.JULY, 5);
        assertFalse(independenceDayRule.isHoliday(july5th), "July 5, 2020 should not be a holiday");
    }

    @Test
    public void testObservedIndependenceDayOnSunday() {
        LocalDate july4th = LocalDate.of(2021, Month.JULY, 4);
        assertTrue(independenceDayRule.isHoliday(july4th), "July 4, 2021 should be a holiday");

        LocalDate july5th = LocalDate.of(2021, Month.JULY, 5);
        assertTrue(independenceDayRule.isHoliday(july5th), "July 5, 2021 should be the observed holiday");

        LocalDate july3rd = LocalDate.of(2021, Month.JULY, 3);
        assertFalse(independenceDayRule.isHoliday(july3rd), "July 3, 2021 should not be a holiday");
    }

    @Test
    public void testNonHoliday() {
        LocalDate nonHoliday = LocalDate.of(2024, Month.JULY, 6);
        assertFalse(independenceDayRule.isHoliday(nonHoliday), "July 6, 2024 should not be a holiday");

        nonHoliday = LocalDate.of(2024, Month.JULY, 3);
        assertFalse(independenceDayRule.isHoliday(nonHoliday), "July 3, 2024 should not be a holiday");

        nonHoliday = LocalDate.of(2024, Month.JULY, 5);
        assertFalse(independenceDayRule.isHoliday(nonHoliday), "July 5, 2024 should not be a holiday");
    }
}
