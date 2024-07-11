/**
 * ChargeProcessor class for the Tool Rental System application.
 *
 * This class is responsible for calculating the rental charges for tools
 * based on the rental period, applicable holidays, and specific charge
 * rules for each tool type. It integrates with the HolidayProcessor to
 * exclude holidays from charge calculations when appropriate.
 *
 * Key functionalities:
 * - Initialization with a list of ChargeRule objects and a HolidayProcessor.
 * - Logging the charge rules during initialization for transparency.
 * - Calculating the total rental charge for a given rental period and
 *   charge rule.
 *
 * Components:
 * - List of ChargeRule: Defines the rental charges based on the type
 *   of tool and the day (weekday, weekend, holiday).
 * - HolidayProcessor: Checks if a specific date is a holiday to determine
 *   if a charge should be applied.
 *
 * Example usage:
 * - Calculate the total charge for a rental period by excluding holidays
 *   and applying appropriate charges for weekdays and weekends.
 *
 * @version 1.0
 */
package com.example.toolrental;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChargeProcessor {

    private static final Logger logger = LoggerConfig.getLogger();
    private final List<ChargeRule> chargeRules;
    private final HolidayProcessor holidayProcessor;

    public ChargeProcessor(List<ChargeRule> chargeRules, HolidayProcessor holidayProcessor) {
        this.chargeRules = chargeRules;
        this.holidayProcessor = holidayProcessor;
        logChargeRules();
    }

    private void logChargeRules() {
        for (ChargeRule rule : chargeRules) {
            logger.log(Level.INFO, "ChargeProcessor initialized with rule: {0}, {1}, {2}", new Object[]{rule.getToolCode(), rule.getToolType(), rule.getBrand()});
        }
    }

    public HolidayProcessor getHolidayProcessor() {
        return holidayProcessor;
    }

    public List<ChargeRule> getChargeRules() {
        return chargeRules;
    }

    public BigDecimal calculateTotalCharge(LocalDate startDate, LocalDate endDate, ChargeRule chargeRule) {
        BigDecimal totalCharge = BigDecimal.ZERO;
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            if (!holidayProcessor.isHoliday(date)) {
                BigDecimal charge = chargeRule.getCharge(date);
                logger.log(Level.INFO, "Date: {0} - Charge: {1}", new Object[]{date, charge});
                totalCharge = totalCharge.add(charge);
            }
        }
        logger.log(Level.INFO, "Total Charge: {0}", totalCharge);
        return totalCharge.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
