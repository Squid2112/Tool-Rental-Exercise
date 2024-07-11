/**
 * Test class for ChargeProcessorConfig in the Tool Rental System application.
 *
 * This class contains unit tests for the ChargeProcessorConfig class, ensuring
 * that charge rules are correctly loaded from JSON and YAML configuration files.
 *
 * Test cases:
 * - Loading charge rules from a JSON configuration file.
 * - Loading charge rules from a YAML configuration file.
 *
 * Each test verifies the charge values for different days (weekday, weekend, holiday)
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ChargeProcessorConfigTest {

    private HolidayProcessor holidayProcessor;

    @BeforeEach
    public void setUp() {
        holidayProcessor = new HolidayProcessor();
        holidayProcessor.addHolidayRule(new IndependenceDayRule());
        holidayProcessor.addHolidayRule(new LaborDayRule());
    }

    @Test
    public void testGetChargeRulesFromJson() throws IOException {
        List<ChargeRule> chargeRules = ChargeProcessorConfig.getChargeRules(ConfigFileType.JSON, "tools", holidayProcessor);

        assertNotNull(chargeRules);
        assertEquals(4, chargeRules.size());

        ChargeRule ladderRule = chargeRules.get(0);
        assertEquals(BigDecimal.valueOf(1.99), ladderRule.getCharge(LocalDate.of(2024, 7, 1))); // Weekday
        assertEquals(BigDecimal.valueOf(1.99), ladderRule.getCharge(LocalDate.of(2024, 7, 6))); // Weekend
        assertEquals(BigDecimal.valueOf(0.0), ladderRule.getCharge(LocalDate.of(2024, 7, 4))); // Holiday

        ChargeRule chainsawRule = chargeRules.get(1);
        assertEquals(BigDecimal.valueOf(1.49), chainsawRule.getCharge(LocalDate.of(2024, 7, 1))); // Weekday
        assertEquals(BigDecimal.valueOf(0.0), chainsawRule.getCharge(LocalDate.of(2024, 7, 6))); // Weekend
        assertEquals(BigDecimal.valueOf(1.49), chainsawRule.getCharge(LocalDate.of(2024, 7, 4))); // Holiday

        ChargeRule jackhammerRule = chargeRules.get(2);
        assertEquals(BigDecimal.valueOf(2.99), jackhammerRule.getCharge(LocalDate.of(2024, 7, 1))); // Weekday
        assertEquals(BigDecimal.valueOf(0.0), jackhammerRule.getCharge(LocalDate.of(2024, 7, 6))); // Weekend
        assertEquals(BigDecimal.valueOf(0.0), jackhammerRule.getCharge(LocalDate.of(2024, 7, 4))); // Holiday
    }

    @Test
    public void testGetChargeRulesFromYaml() throws IOException {
        List<ChargeRule> chargeRules = ChargeProcessorConfig.getChargeRules(ConfigFileType.YAML, "tools", holidayProcessor);

        assertNotNull(chargeRules);
        assertEquals(4, chargeRules.size());

        ChargeRule ladderRule = chargeRules.get(0);
        assertEquals(BigDecimal.valueOf(1.99), ladderRule.getCharge(LocalDate.of(2024, 7, 1))); // Weekday
        assertEquals(BigDecimal.valueOf(1.99), ladderRule.getCharge(LocalDate.of(2024, 7, 6))); // Weekend
        assertEquals(BigDecimal.valueOf(0.0), ladderRule.getCharge(LocalDate.of(2024, 7, 4))); // Holiday

        ChargeRule chainsawRule = chargeRules.get(1);
        assertEquals(BigDecimal.valueOf(1.49), chainsawRule.getCharge(LocalDate.of(2024, 7, 1))); // Weekday
        assertEquals(BigDecimal.valueOf(0.0), chainsawRule.getCharge(LocalDate.of(2024, 7, 6))); // Weekend
        assertEquals(BigDecimal.valueOf(1.49), chainsawRule.getCharge(LocalDate.of(2024, 7, 4))); // Holiday

        ChargeRule jackhammerRule = chargeRules.get(2);
        assertEquals(BigDecimal.valueOf(2.99), jackhammerRule.getCharge(LocalDate.of(2024, 7, 1))); // Weekday
        assertEquals(BigDecimal.valueOf(0.0), jackhammerRule.getCharge(LocalDate.of(2024, 7, 6))); // Weekend
        assertEquals(BigDecimal.valueOf(0.0), jackhammerRule.getCharge(LocalDate.of(2024, 7, 4))); // Holiday
    }
}
