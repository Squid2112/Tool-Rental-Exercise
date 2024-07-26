/**
 * ChargeRule class for the Tool Rental System application.
 *
 * This class represents the charging rules for a specific tool, including weekday,
 * weekend, and holiday charges. It is used by the ChargeProcessor to determine the
 * applicable charges for a tool based on the rental dates.
 *
 * Key functionalities:
 * - Stores charge rates for weekdays, weekends, and holidays.
 * - Determines if a charge applies to a given date based on the charge rules and holiday status.
 * - Retrieves the applicable charge for a given date.
 *
 * Components:
 * - toolCode: Unique identifier for the tool.
 * - toolType: Type of the tool.
 * - toolBrand: Brand of the tool.
 * - weekdayCharge: Charge rate for weekdays.
 * - weekendCharge: Charge rate for weekends.
 * - holidayCharge: Charge rate for holidays.
 * - holidayProcessor: Processor to determine if a given date is a holiday.
 *
 * Example usage:
 * - Used by the ChargeProcessor to calculate the total rental charge for a tool over a rental period.
 *
 * @version 1.0
 */
package com.example.toolrental;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChargeRule {

    private static final Logger logger = LoggerConfig.getLogger();

    private final String toolCode;
    private final String toolType;
    private final String toolBrand;
    private final BigDecimal weekdayCharge;
    private final BigDecimal weekendCharge;
    private final BigDecimal holidayCharge;
    private final HolidayProcessor holidayProcessor;

    public ChargeRule(String toolCode, String toolType, String toolBrand, BigDecimal weekdayCharge, BigDecimal weekendCharge, BigDecimal holidayCharge, HolidayProcessor holidayProcessor) {
        this.toolCode = toolCode;
        this.toolType = toolType;
        this.toolBrand = toolBrand;
        this.weekdayCharge = weekdayCharge;
        this.weekendCharge = weekendCharge;
        this.holidayCharge = holidayCharge;
        this.holidayProcessor = holidayProcessor;
    }

    public String getToolCode() {
        return toolCode;
    }

    public String getToolType() {
        return toolType;
    }

    public String getToolBrand() {
        return toolBrand;
    }

    public BigDecimal getWeekdayCharge() {
        return weekdayCharge;
    }

    public BigDecimal getWeekendCharge() {
        return weekendCharge;
    }

    public BigDecimal getHolidayCharge() {
        return holidayCharge;
    }

    public boolean applies(LocalDate date) {
        if (holidayProcessor.isHoliday(date)) {
            if (holidayCharge.compareTo(BigDecimal.ZERO) > 0) {
                return true;
            } else {
                return false;
            }
        }

        if ((date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) && weekendCharge.compareTo(BigDecimal.ZERO) > 0) {
            return true;
        }

        return !((date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY)) && weekdayCharge.compareTo(BigDecimal.ZERO) > 0;
    }

    public BigDecimal getCharge(LocalDate date) {
        BigDecimal charge;

        if (holidayProcessor.isHoliday(date)) {
            charge = holidayCharge;
        } else if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            charge = weekendCharge;
        } else {
            charge = weekdayCharge;
        }

        logger.log(Level.INFO, "Date: {0}, Charge: {1}, Tool Code: {2}, Tool Type: {3}, Tool Brand: {4}", new Object[]{date, charge, toolCode, toolType, toolBrand});
        return charge;
    }
}
