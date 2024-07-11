/**
 * Test class for ChargeProcessor in the Tool Rental System application.
 *
 * This class contains unit tests for the ChargeProcessor class, ensuring
 * that the total charges are correctly calculated based on the rental rules
 * for various tools and dates.
 *
 * Test cases:
 * - Calculating the total charge for a ladder rental that includes a holiday.
 * - Calculating the total charge for a chainsaw rental that includes a holiday.
 * - Calculating the total charge for a DeWalt jackhammer rental that includes a holiday.
 * - Calculating the total charge for a Ridgid jackhammer rental that includes a holiday.
 *
 * Each test verifies the total charge values for different date ranges (including holidays and weekends)
 * for the tools configured in the test files.
 *
 * @version 1.0
 */
package com.example.toolrental;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ChargeProcessorTest {

    private HolidayProcessor holidayProcessor;
    private ChargeProcessor chargeProcessor;

    @BeforeEach
    public void setUp() throws IOException {
        holidayProcessor = new HolidayProcessor();
        holidayProcessor.addHolidayRule(new IndependenceDayRule());
        holidayProcessor.addHolidayRule(new LaborDayRule());

        List<ChargeRule> chargeRules = ChargeProcessorConfig.getChargeRules(ConfigFileType.JSON, "tools", holidayProcessor);
        chargeProcessor = new ChargeProcessor(chargeRules, holidayProcessor);

        // Log loaded charge rules
        System.out.println("Loaded charge rules:");
        for (ChargeRule rule : chargeRules) {
            System.out.println("Tool Code: " + rule.getToolCode() + ", Tool Type: " + rule.getToolType() + ", Brand: " + rule.getBrand());
        }
    }

    @Test
    public void testCalculateTotalCharge_Ladder_WithHoliday() {
        LocalDate startDate = LocalDate.of(2024, 7, 4); // Independence Day
        LocalDate endDate = LocalDate.of(2024, 7, 5); // The day after

        // Find the rule for the ladder
        ChargeRule ladderRule = chargeProcessor.getChargeRules().stream()
                .filter(rule -> "LADW".equals(rule.getToolCode()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Charge rule not found for tool code: LADW"));

        BigDecimal totalCharge = chargeProcessor.calculateTotalCharge(startDate, endDate, ladderRule);

        // The first day is a holiday (no charge), the second day is a regular weekday (charge applies)
        assertEquals(BigDecimal.valueOf(1.99).setScale(2), totalCharge);
    }

    @Test
    public void testCalculateTotalCharge_Chainsaw_WithHoliday() {
        LocalDate startDate = LocalDate.of(2024, 7, 4); // Independence Day
        LocalDate endDate = LocalDate.of(2024, 7, 6); // Two days after

        // Find the rule for the chainsaw
        ChargeRule chainsawRule = chargeProcessor.getChargeRules().stream()
                .filter(rule -> "CHNS".equals(rule.getToolCode()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Charge rule not found for tool code: CHNS"));

        BigDecimal totalCharge = chargeProcessor.calculateTotalCharge(startDate, endDate, chainsawRule);

        // The first day is a holiday (charge applies), the second and third days are weekend (no charge)
        assertEquals(BigDecimal.valueOf(1.49).setScale(2), totalCharge);
    }

    @Test
    public void testCalculateTotalCharge_Jackhammer_DeWalt_WithHoliday() {
        LocalDate startDate = LocalDate.of(2024, 7, 4); // Independence Day
        LocalDate endDate = LocalDate.of(2024, 7, 5); // The day after

        // Find the rule for the DeWalt jackhammer
        ChargeRule jackhammerRule = chargeProcessor.getChargeRules().stream()
                .filter(rule -> "JAKD".equals(rule.getToolCode()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Charge rule not found for tool code: JAKD"));

        BigDecimal totalCharge = chargeProcessor.calculateTotalCharge(startDate, endDate, jackhammerRule);

        // The first day is a holiday (no charge), the second day is a regular weekday (charge applies)
        assertEquals(BigDecimal.valueOf(2.99).setScale(2), totalCharge);
    }

    @Test
    public void testCalculateTotalCharge_Jackhammer_Ridgid_WithHoliday() {
        LocalDate startDate = LocalDate.of(2024, 7, 4); // Independence Day
        LocalDate endDate = LocalDate.of(2024, 7, 5); // The day after

        // Find the rule for the Ridgid jackhammer
        ChargeRule jackhammerRule = chargeProcessor.getChargeRules().stream()
                .filter(rule -> "JAKR".equals(rule.getToolCode()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Charge rule not found for tool code: JAKR"));

        BigDecimal totalCharge = chargeProcessor.calculateTotalCharge(startDate, endDate, jackhammerRule);

        // The first day is a holiday (no charge), the second day is a regular weekday (charge applies)
        assertEquals(BigDecimal.valueOf(2.99).setScale(2), totalCharge);
    }
}
