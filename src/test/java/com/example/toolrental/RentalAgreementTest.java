/**
 * Test class for RentalAgreement in the Tool Rental System application.
 *
 * This class contains unit tests for the RentalAgreement class, ensuring
 * that rental agreements are correctly generated with accurate charges
 * and discounts based on various rental scenarios.
 *
 * Test cases:
 * - Validating rental agreements for different tools (ladders, chainsaws, jackhammers).
 * - Checking correct application of holiday and weekend charges.
 * - Ensuring proper handling of discounts.
 * - Verifying behavior with invalid input (e.g., invalid rental days or discount percentages).
 *
 * Each test ensures the proper functioning of rental agreement generation and charge calculations.
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RentalAgreementTest {

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
            System.out.println("Tool Code: " + rule.getToolCode() + ", Tool Type: " + rule.getToolType() + ", Tool Brand: " + rule.getToolBrand());
        }
    }

    @Test
    public void testLadderRental() {
        LocalDate checkOutDate = LocalDate.of(2024, 7, 4); // Independence Day
        RentalAgreement agreement = new RentalAgreement("LADW", "Ladder", "Werner", 2, checkOutDate, 0, 1, chargeProcessor);

        assertNotNull(agreement);
        assertEquals("LADW", agreement.getToolCode());
        assertEquals("Ladder", agreement.getToolType());
        assertEquals("Werner", agreement.getToolBrand());
        assertEquals(2, agreement.getRentalDays());
        assertEquals(checkOutDate, agreement.getCheckOutDate());
        assertEquals(checkOutDate.plusDays(agreement.getRentalDays() - 1), agreement.getDueDate());
        assertEquals(BigDecimal.valueOf(1.99).setScale(2), agreement.getDailyRentalCharge().setScale(2));
        assertEquals(1, agreement.getChargeDays());
        assertEquals(BigDecimal.valueOf(1.99).setScale(2), agreement.getPreDiscountCharge().setScale(2));
        assertEquals(BigDecimal.ZERO.setScale(2), agreement.getDiscountAmount().setScale(2));
        assertEquals(BigDecimal.valueOf(1.99).setScale(2), agreement.getFinalCharge().setScale(2));
    }

    @Test
    public void testChainsawRental() {
        LocalDate checkOutDate = LocalDate.of(2024, 7, 3); // Day before Independence Day
        RentalAgreement agreement = new RentalAgreement("CHNS", "Chainsaw", "Stihl", 3, checkOutDate, 0, 1, chargeProcessor);

        assertNotNull(agreement);
        assertEquals("CHNS", agreement.getToolCode());
        assertEquals("Chainsaw", agreement.getToolType());
        assertEquals("Stihl", agreement.getToolBrand());
        assertEquals(3, agreement.getRentalDays());
        assertEquals(checkOutDate, agreement.getCheckOutDate());
        assertEquals(checkOutDate.plusDays(agreement.getRentalDays() - 1), agreement.getDueDate());
        assertEquals(BigDecimal.valueOf(1.49).setScale(2), agreement.getDailyRentalCharge().setScale(2));
        assertEquals(3, agreement.getChargeDays()); // Charge days should be 2 (July 45, 6)
        assertEquals(BigDecimal.valueOf(4.47).setScale(2), agreement.getPreDiscountCharge().setScale(2));
        assertEquals(BigDecimal.ZERO.setScale(2), agreement.getDiscountAmount().setScale(2));
        assertEquals(BigDecimal.valueOf(4.47).setScale(2), agreement.getFinalCharge().setScale(2));
    }

    @Test
    public void testJackhammerRentalWithDiscount() {
        LocalDate checkOutDate = LocalDate.of(2024, 8, 31); // Day before Labor Day weekend
        RentalAgreement agreement = new RentalAgreement("JAKD", "Jackhammer", "DeWalt", 5, checkOutDate, 10, 1, chargeProcessor); // 10% discount

        assertNotNull(agreement);
        assertEquals("JAKD", agreement.getToolCode());
        assertEquals("Jackhammer", agreement.getToolType());
        assertEquals("DeWalt", agreement.getToolBrand());
        assertEquals(5, agreement.getRentalDays());
        assertEquals(checkOutDate, agreement.getCheckOutDate());
        assertEquals(checkOutDate.plusDays(agreement.getRentalDays() - 1), agreement.getDueDate());
        assertEquals(BigDecimal.valueOf(2.99).setScale(2), agreement.getDailyRentalCharge().setScale(2));
        assertEquals(2, agreement.getChargeDays()); // Charge days should be 3 (Sep 1, 3)
        assertEquals(BigDecimal.valueOf(5.98).setScale(2), agreement.getPreDiscountCharge().setScale(2));
        assertEquals(BigDecimal.valueOf(0.60).setScale(2), agreement.getDiscountAmount().setScale(2));
        assertEquals(BigDecimal.valueOf(5.38).setScale(2), agreement.getFinalCharge().setScale(2));
    }

    @Test
    public void testMultipleLaddersRental() {
        LocalDate checkOutDate = LocalDate.of(2024, 7, 4); // Independence Day
        RentalAgreement agreement = new RentalAgreement("LADW", "Ladder", "Werner", 2, checkOutDate, 0, 2, chargeProcessor); // Renting 2 ladders

        assertNotNull(agreement);
        assertEquals("LADW", agreement.getToolCode());
        assertEquals("Ladder", agreement.getToolType());
        assertEquals("Werner", agreement.getToolBrand());
        assertEquals(2, agreement.getRentalDays());
        assertEquals(checkOutDate, agreement.getCheckOutDate());
        assertEquals(checkOutDate.plusDays(agreement.getRentalDays() - 1), agreement.getDueDate());
        assertEquals(BigDecimal.valueOf(1.99).setScale(2), agreement.getDailyRentalCharge().setScale(2));
        assertEquals(1, agreement.getChargeDays()); // Charge days should be 1
        assertEquals(BigDecimal.valueOf(3.98).setScale(2), agreement.getPreDiscountCharge().setScale(2)); // 2 ladders
        assertEquals(BigDecimal.ZERO.setScale(2), agreement.getDiscountAmount().setScale(2));
        assertEquals(BigDecimal.valueOf(3.98).setScale(2), agreement.getFinalCharge().setScale(2));
    }

    @Test
    public void testMultipleToolsRental() {
        LocalDate checkOutDate = LocalDate.of(2024, 7, 4); // Independence Day

        ShoppingCart cart = new ShoppingCart(chargeProcessor);
        cart.addTool("LADW", "Ladder", "Werner", 2, 0, 2, checkOutDate); // Renting 2 ladders
        cart.addTool("CHNS", "Chainsaw", "Stihl", 3, 0, 1, checkOutDate); // Renting 1 chainsaw

        List<RentalAgreement> agreements = cart.checkout();
        assertNotNull(agreements);
        assertEquals(2, agreements.size());

        RentalAgreement ladderAgreement = agreements.get(0);
        assertEquals("LADW", ladderAgreement.getToolCode());
        assertEquals("Ladder", ladderAgreement.getToolType());
        assertEquals("Werner", ladderAgreement.getToolBrand());
        assertEquals(2, ladderAgreement.getRentalDays());
        assertEquals(checkOutDate, ladderAgreement.getCheckOutDate());
        assertEquals(checkOutDate.plusDays(ladderAgreement.getRentalDays() - 1), ladderAgreement.getDueDate());
        assertEquals(BigDecimal.valueOf(1.99).setScale(2), ladderAgreement.getDailyRentalCharge().setScale(2));
        assertEquals(1, ladderAgreement.getChargeDays()); // Charge days should be 1
        assertEquals(BigDecimal.valueOf(3.98).setScale(2), ladderAgreement.getPreDiscountCharge().setScale(2)); // 2 ladders
        assertEquals(BigDecimal.ZERO.setScale(2), ladderAgreement.getDiscountAmount().setScale(2));
        assertEquals(BigDecimal.valueOf(3.98).setScale(2), ladderAgreement.getFinalCharge().setScale(2));

        RentalAgreement chainsawAgreement = agreements.get(1);
        assertEquals("CHNS", chainsawAgreement.getToolCode());
        assertEquals("Chainsaw", chainsawAgreement.getToolType());
        assertEquals("Stihl", chainsawAgreement.getToolBrand());
        assertEquals(3, chainsawAgreement.getRentalDays());
        assertEquals(checkOutDate, chainsawAgreement.getCheckOutDate());
        assertEquals(checkOutDate.plusDays(chainsawAgreement.getRentalDays() - 1), chainsawAgreement.getDueDate());
        assertEquals(BigDecimal.valueOf(1.49).setScale(2), chainsawAgreement.getDailyRentalCharge().setScale(2));
        assertEquals(2, chainsawAgreement.getChargeDays()); // Charge days should be 2 (July 5, 6)
        assertEquals(BigDecimal.valueOf(2.98).setScale(2), chainsawAgreement.getPreDiscountCharge().setScale(2));
        assertEquals(BigDecimal.ZERO.setScale(2), chainsawAgreement.getDiscountAmount().setScale(2));
        assertEquals(BigDecimal.valueOf(2.98).setScale(2), chainsawAgreement.getFinalCharge().setScale(2));
    }

    @Test
    public void testInvalidDiscount() {
        LocalDate checkOutDate = LocalDate.of(2024, 7, 4); // Independence Day
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> new RentalAgreement("LADW", "Ladder", "Werner", 2, checkOutDate, 110, 1, chargeProcessor) // Invalid discount percentage (over 100)
        );
        assertTrue(thrown.getMessage().contains("Invalid discount percentage"));
    }

    @Test
    public void testInvalidRentalDays() {
        LocalDate checkOutDate = LocalDate.of(2024, 7, 4); // Independence Day
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> new RentalAgreement("LADW", "Ladder", "Werner", 0, checkOutDate, 0, 1, chargeProcessor) // Invalid rental days (zero)
        );
        assertTrue(thrown.getMessage().contains("Invalid rental days"));
    }
}
