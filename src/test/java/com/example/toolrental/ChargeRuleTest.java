/**
 * ChargeRuleTest class for the Tool Rental System application.
 *
 * This class contains unit tests for the ChargeRule class, verifying the correct
 * application of charges based on different dates and conditions, including weekdays,
 * weekends, and holidays.
 *
 * Key functionalities tested:
 * - Correct retrieval of weekday, weekend, and holiday charges.
 * - Correct determination of applicable charges based on rental dates.
 * - Correct retrieval of tool attributes such as tool code, tool type, and brand.
 *
 * Tests included:
 * - testWeekdayCharge: Verifies the charge for a weekday.
 * - testWeekendCharge: Verifies the charge for a weekend.
 * - testHolidayCharge: Verifies the charge for a holiday with zero charge.
 * - testAppliesMethod: Tests the applies method for different dates and conditions.
 * - testMultipleHolidays: Verifies the applies method for multiple holidays.
 * - testGetToolCode: Verifies the retrieval of the tool code.
 * - testGetToolType: Verifies the retrieval of the tool type.
 * - testGetBrand: Verifies the retrieval of the tool brand.
 * - testGetWeekdayCharge: Verifies the retrieval of the weekday charge.
 * - testGetWeekendCharge: Verifies the retrieval of the weekend charge.
 * - testGetHolidayCharge: Verifies the retrieval of the holiday charge.
 *
 * @version 1.0
 */
package com.example.toolrental;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ChargeRuleTest {

    private HolidayProcessor holidayProcessor;
    private ChargeRule chargeRule;

    @BeforeEach
    public void setUp() {
        holidayProcessor = new HolidayProcessor();
        holidayProcessor.addHolidayRule(new IndependenceDayRule());
        holidayProcessor.addHolidayRule(new LaborDayRule());

        chargeRule = new ChargeRule(
                "LADW",
                "Ladder",
                "Werner",
                BigDecimal.valueOf(1.99),
                BigDecimal.valueOf(1.49),
                BigDecimal.valueOf(0.0),
                holidayProcessor
        );
    }

    @Test
    public void testWeekdayCharge() {
        LocalDate weekday = LocalDate.of(2024, 7, 2); // A Tuesday
        assertEquals(BigDecimal.valueOf(1.99).setScale(2), chargeRule.getCharge(weekday).setScale(2));
    }

    @Test
    public void testWeekendCharge() {
        LocalDate weekend = LocalDate.of(2024, 7, 6); // A Saturday
        assertEquals(BigDecimal.valueOf(1.49).setScale(2), chargeRule.getCharge(weekend).setScale(2));
    }

    @Test
    public void testHolidayCharge() {
        LocalDate holiday = LocalDate.of(2024, 7, 4); // Independence Day
        assertEquals(BigDecimal.valueOf(0.0).setScale(2), chargeRule.getCharge(holiday).setScale(2));
    }

    @Test
    public void testAppliesMethod() {
        LocalDate weekday = LocalDate.of(2024, 7, 2); // A Tuesday
        assertTrue(chargeRule.applies(weekday));

        LocalDate weekend = LocalDate.of(2024, 7, 6); // A Saturday
        assertTrue(chargeRule.applies(weekend));

        LocalDate holiday = LocalDate.of(2024, 7, 4); // Independence Day
        assertFalse(chargeRule.applies(holiday)); // This specific charge rule has a zero holiday charge
    }

    @Test
    public void testMultipleHolidays() {
        LocalDate holiday = LocalDate.of(2024, 9, 2); // Labor Day
        assertFalse(chargeRule.applies(holiday)); // This specific charge rule has a zero holiday charge
    }

    @Test
    public void testGetToolCode() {
        assertEquals("LADW", chargeRule.getToolCode());
    }

    @Test
    public void testGetToolType() {
        assertEquals("Ladder", chargeRule.getToolType());
    }

    @Test
    public void testGetBrand() {
        assertEquals("Werner", chargeRule.getBrand());
    }

    @Test
    public void testGetWeekdayCharge() {
        assertEquals(BigDecimal.valueOf(1.99), chargeRule.getWeekdayCharge());
    }

    @Test
    public void testGetWeekendCharge() {
        assertEquals(BigDecimal.valueOf(1.49), chargeRule.getWeekendCharge());
    }

    @Test
    public void testGetHolidayCharge() {
        assertTrue(BigDecimal.ZERO.compareTo(chargeRule.getHolidayCharge()) == 0);
    }
}
