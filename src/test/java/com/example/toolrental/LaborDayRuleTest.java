/**
 * Unit tests for the LaborDayRule class.
 *
 * This class provides test cases to verify the correct functionality of the LaborDayRule class,
 * which determines if a given date is the Labor Day holiday.
 *
 * Tests:
 * - testLaborDay: Verifies that the first Monday in September is correctly identified as Labor Day.
 * - testNonLaborDay: Verifies that dates not matching the first Monday in September are not identified as Labor Day.
 *
 * Example usage:
 * - Ensures that the LaborDayRule class correctly identifies Labor Day for various dates.
 *
 * @version 1.0
 */
package com.example.toolrental;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LaborDayRuleTest {

    private LaborDayRule laborDayRule;

    @BeforeEach
    public void setUp() {
        laborDayRule = new LaborDayRule();
    }

    @Test
    public void testLaborDay() {
        LocalDate laborDay2024 = LocalDate.of(2024, Month.SEPTEMBER, 2);
        assertTrue(laborDayRule.isHoliday(laborDay2024), "September 2, 2024 should be Labor Day");

        LocalDate laborDay2023 = LocalDate.of(2023, Month.SEPTEMBER, 4);
        assertTrue(laborDayRule.isHoliday(laborDay2023), "September 4, 2023 should be Labor Day");

        LocalDate laborDay2022 = LocalDate.of(2022, Month.SEPTEMBER, 5);
        assertTrue(laborDayRule.isHoliday(laborDay2022), "September 5, 2022 should be Labor Day");

        LocalDate notLaborDay2024 = LocalDate.of(2024, Month.SEPTEMBER, 9);
        assertFalse(laborDayRule.isHoliday(notLaborDay2024), "September 9, 2024 should not be Labor Day");
    }

    @Test
    public void testNonLaborDay() {
        LocalDate randomDate2024 = LocalDate.of(2024, Month.SEPTEMBER, 10);
        assertFalse(laborDayRule.isHoliday(randomDate2024), "September 10, 2024 should not be Labor Day");

        LocalDate randomDate2023 = LocalDate.of(2023, Month.SEPTEMBER, 12);
        assertFalse(laborDayRule.isHoliday(randomDate2023), "September 12, 2023 should not be Labor Day");
    }
}
