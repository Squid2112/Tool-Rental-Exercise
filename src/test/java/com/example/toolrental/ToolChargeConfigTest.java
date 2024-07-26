/**
 * ToolChargeConfigTest class for unit testing the ToolChargeConfig class.
 *
 * This test class verifies the functionality of the ToolChargeConfig class, ensuring
 * that tool charge configurations can be set and retrieved correctly.
 *
 * Tests:
 * - testSetAndGetTools: Verifies that the list of ToolCharge objects can be set and retrieved
 *   correctly, and that the properties of the ToolCharge objects (tool code, tool type, brand,
 *   weekday charge, weekend charge, and holiday charge) are stored and returned as expected.
 *
 * Example usage:
 * - This test class is used to validate the integrity of the ToolChargeConfig class within
 *   the Tool Rental System application.
 *
 * @version 1.0
 */
package com.example.toolrental;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ToolChargeConfigTest {

    private ToolChargeConfig toolChargeConfig;

    @BeforeEach
    public void setUp() {
        toolChargeConfig = new ToolChargeConfig();
    }

    @Test
    public void testSetAndGetTools() {
        ToolChargeConfig.ToolCharge toolCharge1 = new ToolChargeConfig.ToolCharge();
        toolCharge1.setToolCode("LADW");
        toolCharge1.setToolType("Ladder");
        toolCharge1.setToolBrand("Werner");
        toolCharge1.setWeekdayCharge(BigDecimal.valueOf(1.99));
        toolCharge1.setWeekendCharge(BigDecimal.valueOf(1.99));
        toolCharge1.setHolidayCharge(BigDecimal.ZERO);

        ToolChargeConfig.ToolCharge toolCharge2 = new ToolChargeConfig.ToolCharge();
        toolCharge2.setToolCode("CHNS");
        toolCharge2.setToolType("Chainsaw");
        toolCharge2.setToolBrand("Stihl");
        toolCharge2.setWeekdayCharge(BigDecimal.valueOf(1.49));
        toolCharge2.setWeekendCharge(BigDecimal.ZERO);
        toolCharge2.setHolidayCharge(BigDecimal.valueOf(1.49));

        List<ToolChargeConfig.ToolCharge> tools = Arrays.asList(toolCharge1, toolCharge2);
        toolChargeConfig.setTools(tools);

        List<ToolChargeConfig.ToolCharge> retrievedTools = toolChargeConfig.getTools();
        assertNotNull(retrievedTools);
        assertEquals(2, retrievedTools.size());

        ToolChargeConfig.ToolCharge retrievedToolCharge1 = retrievedTools.get(0);
        assertEquals("LADW", retrievedToolCharge1.getToolCode());
        assertEquals("Ladder", retrievedToolCharge1.getToolType());
        assertEquals("Werner", retrievedToolCharge1.getToolBrand());
        assertEquals(BigDecimal.valueOf(1.99), retrievedToolCharge1.getWeekdayCharge());
        assertEquals(BigDecimal.valueOf(1.99), retrievedToolCharge1.getWeekendCharge());
        assertEquals(BigDecimal.ZERO, retrievedToolCharge1.getHolidayCharge());

        ToolChargeConfig.ToolCharge retrievedToolCharge2 = retrievedTools.get(1);
        assertEquals("CHNS", retrievedToolCharge2.getToolCode());
        assertEquals("Chainsaw", retrievedToolCharge2.getToolType());
        assertEquals("Stihl", retrievedToolCharge2.getToolBrand());
        assertEquals(BigDecimal.valueOf(1.49), retrievedToolCharge2.getWeekdayCharge());
        assertEquals(BigDecimal.ZERO, retrievedToolCharge2.getWeekendCharge());
        assertEquals(BigDecimal.valueOf(1.49), retrievedToolCharge2.getHolidayCharge());
    }
}
