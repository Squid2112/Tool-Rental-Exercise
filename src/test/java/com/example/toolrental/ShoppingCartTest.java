/**
 * Test class for ShoppingCart in the Tool Rental System application.
 *
 * This class contains unit tests for the ShoppingCart class, ensuring
 * that tools can be added to the cart, rental agreements are correctly
 * generated, and charges are accurately calculated, including applicable
 * discounts and exemptions.
 *
 * Test cases:
 * - Adding different tools to the shopping cart and checking out.
 * - Verifying rental agreements for various tools (ladders, chainsaws, jackhammers).
 * - Ensuring correct application of holiday and weekend charges.
 * - Validating behavior with discounts and invalid input.
 * - Checking consolidated rental agreements for multiple tools.
 *
 * Each test ensures the proper functioning of shopping cart operations
 * and rental agreement generation.
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

public class ShoppingCartTest {

    private HolidayProcessor holidayProcessor;
    private ChargeProcessor chargeProcessor;

    @BeforeEach
    public void setUp() throws IOException {
        holidayProcessor = new HolidayProcessor();
        holidayProcessor.addHolidayRule(new IndependenceDayRule());
        holidayProcessor.addHolidayRule(new LaborDayRule());

        List<ChargeRule> chargeRules = ChargeProcessorConfig.getChargeRules(ConfigFileType.JSON, "tools", holidayProcessor);
        chargeProcessor = new ChargeProcessor(chargeRules, holidayProcessor);
    }

    @Test
    public void testAddToolAndCheckout_Ladder() {
        ShoppingCart cart = new ShoppingCart(chargeProcessor);

        LocalDate checkOutDate = LocalDate.of(2024, 7, 4); // Independence Day
        cart.addTool("LADW", "Ladder", "Werner", 2, 0, 1, checkOutDate);

        List<RentalAgreement> agreements = cart.checkout();
        assertNotNull(agreements);
        assertEquals(1, agreements.size());

        RentalAgreement agreement = agreements.get(0);
        assertEquals("LADW", agreement.getToolCode());
        assertEquals("Ladder", agreement.getToolType());
        assertEquals("Werner", agreement.getToolBrand());
        assertEquals(2, agreement.getRentalDays());
        assertEquals(checkOutDate, agreement.getCheckOutDate());
        assertEquals(checkOutDate.plusDays(agreement.getRentalDays() - 1), agreement.getDueDate());
        assertEquals(BigDecimal.valueOf(1.99), agreement.getDailyRentalCharge());
        assertEquals(1, agreement.getChargeDays());
        assertEquals(BigDecimal.valueOf(1.99), agreement.getPreDiscountCharge());
        assertEquals(BigDecimal.ZERO.setScale(2), agreement.getDiscountAmount());
        assertEquals(BigDecimal.valueOf(1.99), agreement.getFinalCharge());

        agreement.printAgreement(1);
    }

    @Test
    public void testAddToolAndCheckout_Chainsaw() {
        ShoppingCart cart = new ShoppingCart(chargeProcessor);

        LocalDate checkOutDate = LocalDate.of(2024, 7, 3); // Day before Independence Day
        cart.addTool("CHNS", "Chainsaw", "Stihl", 3, 0, 1, checkOutDate);

        List<RentalAgreement> agreements = cart.checkout();
        assertNotNull(agreements);
        assertEquals(1, agreements.size());

        RentalAgreement agreement = agreements.get(0);
        assertEquals("CHNS", agreement.getToolCode());
        assertEquals("Chainsaw", agreement.getToolType());
        assertEquals("Stihl", agreement.getToolBrand());
        assertEquals(3, agreement.getRentalDays());
        assertEquals(checkOutDate, agreement.getCheckOutDate());
        assertEquals(checkOutDate.plusDays(agreement.getRentalDays() - 1), agreement.getDueDate());
        assertEquals(BigDecimal.valueOf(1.49), agreement.getDailyRentalCharge());
        assertEquals(3, agreement.getChargeDays()); // Charge days should be 3 (July 3, 4, 5)
        assertEquals(BigDecimal.valueOf(4.47), agreement.getPreDiscountCharge());
        assertEquals(BigDecimal.ZERO.setScale(2), agreement.getDiscountAmount());
        assertEquals(BigDecimal.valueOf(4.47), agreement.getFinalCharge());

        agreement.printAgreement(1);
    }

    @Test
    public void testAddToolAndCheckout_Jackhammer() {
        ShoppingCart cart = new ShoppingCart(chargeProcessor);

        LocalDate checkOutDate = LocalDate.of(2024, 8, 31); // Day before Labor Day weekend
        cart.addTool("JAKD", "Jackhammer", "DeWalt", 5, 10, 1, checkOutDate); // 10% discount

        List<RentalAgreement> agreements = cart.checkout();
        assertNotNull(agreements);
        assertEquals(1, agreements.size());

        RentalAgreement agreement = agreements.get(0);
        assertEquals("JAKD", agreement.getToolCode());
        assertEquals("Jackhammer", agreement.getToolType());
        assertEquals("DeWalt", agreement.getToolBrand());
        assertEquals(5, agreement.getRentalDays());
        assertEquals(checkOutDate, agreement.getCheckOutDate());
        assertEquals(checkOutDate.plusDays(agreement.getRentalDays() - 1), agreement.getDueDate());
        assertEquals(2, agreement.getChargeDays()); // Charge days should be 3 (Sep 1, 2, 3)
        assertEquals(BigDecimal.valueOf(2.99).setScale(2), agreement.getDailyRentalCharge().setScale(2));

        assertEquals(BigDecimal.valueOf(5.98).setScale(2), agreement.getPreDiscountCharge().setScale(2));
        assertEquals(BigDecimal.valueOf(0.60).setScale(2), agreement.getDiscountAmount().setScale(2));
        assertEquals(BigDecimal.valueOf(5.38).setScale(2), agreement.getFinalCharge().setScale(2));

        agreement.printAgreement(1);
    }

    @Test
    public void testAddMultipleTools() {
        ShoppingCart cart = new ShoppingCart(chargeProcessor);

        LocalDate checkOutDate = LocalDate.of(2024, 7, 4); // Independence Day
        cart.addTool("LADW", "Ladder", "Werner", 2, 0, 2, checkOutDate); // 2 ladders
        cart.addTool("CHNS", "Chainsaw", "Stihl", 3, 0, 1, checkOutDate); // 1 chainsaw

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
        assertEquals(BigDecimal.valueOf(1.99), ladderAgreement.getDailyRentalCharge());
        assertEquals(1, ladderAgreement.getChargeDays()); // Charge days should be 1
        assertEquals(BigDecimal.valueOf(3.98), ladderAgreement.getPreDiscountCharge()); // 2 ladders
        assertEquals(BigDecimal.ZERO.setScale(2), ladderAgreement.getDiscountAmount());
        assertEquals(BigDecimal.valueOf(3.98), ladderAgreement.getFinalCharge());

        RentalAgreement chainsawAgreement = agreements.get(1);
        assertEquals("CHNS", chainsawAgreement.getToolCode());
        assertEquals("Chainsaw", chainsawAgreement.getToolType());
        assertEquals("Stihl", chainsawAgreement.getToolBrand());
        assertEquals(3, chainsawAgreement.getRentalDays());
        assertEquals(checkOutDate, chainsawAgreement.getCheckOutDate());
        assertEquals(checkOutDate.plusDays(chainsawAgreement.getRentalDays() - 1), chainsawAgreement.getDueDate());
        assertEquals(BigDecimal.valueOf(1.49), chainsawAgreement.getDailyRentalCharge());
        assertEquals(2, chainsawAgreement.getChargeDays()); // Charge days should be 2 (July 5, 6)
        assertEquals(BigDecimal.valueOf(2.98), chainsawAgreement.getPreDiscountCharge());
        assertEquals(BigDecimal.ZERO.setScale(2), chainsawAgreement.getDiscountAmount());
        assertEquals(BigDecimal.valueOf(2.98), chainsawAgreement.getFinalCharge());

        ladderAgreement.printAgreement(1);
        chainsawAgreement.printAgreement(1);
    }

    @Test
    public void testDiscountedRental() {
        ShoppingCart cart = new ShoppingCart(chargeProcessor);

        LocalDate checkOutDate = LocalDate.of(2024, 7, 4); // Independence Day
        cart.addTool("LADW", "Ladder", "Werner", 3, 50, 1, checkOutDate); // 50% discount

        List<RentalAgreement> agreements = cart.checkout();
        assertNotNull(agreements);
        assertEquals(1, agreements.size());

        RentalAgreement agreement = agreements.get(0);
        assertEquals("LADW", agreement.getToolCode());
        assertEquals("Ladder", agreement.getToolType());
        assertEquals("Werner", agreement.getToolBrand());
        assertEquals(3, agreement.getRentalDays());
        assertEquals(checkOutDate, agreement.getCheckOutDate());
        assertEquals(checkOutDate.plusDays(agreement.getRentalDays() - 1), agreement.getDueDate());
        assertEquals(BigDecimal.valueOf(1.99), agreement.getDailyRentalCharge());
        assertEquals(2, agreement.getChargeDays()); // Charge days should be 2 (July 5, 6)
        assertEquals(BigDecimal.valueOf(3.98), agreement.getPreDiscountCharge());
        assertEquals(BigDecimal.valueOf(1.99), agreement.getDiscountAmount());
        assertEquals(BigDecimal.valueOf(1.99), agreement.getFinalCharge());

        agreement.printAgreement(1);
    }

    @Test
    public void testSingleDayRental() {
        ShoppingCart cart = new ShoppingCart(chargeProcessor);

        LocalDate checkOutDate = LocalDate.of(2024, 7, 4); // Independence Day
        cart.addTool("LADW", "Ladder", "Werner", 1, 0, 1, checkOutDate);

        List<RentalAgreement> agreements = cart.checkout();
        assertNotNull(agreements);
        assertEquals(1, agreements.size());

        RentalAgreement agreement = agreements.get(0);
        assertEquals("LADW", agreement.getToolCode());
        assertEquals("Ladder", agreement.getToolType());
        assertEquals("Werner", agreement.getToolBrand());
        assertEquals(1, agreement.getRentalDays());
        assertEquals(checkOutDate, agreement.getCheckOutDate());
        assertEquals(checkOutDate.plusDays(agreement.getRentalDays() - 1), agreement.getDueDate());
        assertEquals(BigDecimal.valueOf(1.99), agreement.getDailyRentalCharge());
        assertEquals(0, agreement.getChargeDays()); // Charge days should be 0 (July 4)
        assertEquals(BigDecimal.ZERO.setScale(2), agreement.getPreDiscountCharge());
        assertEquals(BigDecimal.ZERO.setScale(2), agreement.getDiscountAmount());
        assertEquals(BigDecimal.ZERO.setScale(2), agreement.getFinalCharge());

        agreement.printAgreement(1);
    }

    @Test
    public void testFullDiscountRental() {
        ShoppingCart cart = new ShoppingCart(chargeProcessor);

        LocalDate checkOutDate = LocalDate.of(2024, 7, 4); // Independence Day
        cart.addTool("LADW", "Ladder", "Werner", 3, 100, 1, checkOutDate); // 100% discount

        List<RentalAgreement> agreements = cart.checkout();
        assertNotNull(agreements);
        assertEquals(1, agreements.size());

        RentalAgreement agreement = agreements.get(0);
        assertEquals("LADW", agreement.getToolCode());
        assertEquals("Ladder", agreement.getToolType());
        assertEquals("Werner", agreement.getToolBrand());
        assertEquals(3, agreement.getRentalDays());
        assertEquals(checkOutDate, agreement.getCheckOutDate());
        assertEquals(checkOutDate.plusDays(agreement.getRentalDays() - 1), agreement.getDueDate());
        assertEquals(BigDecimal.valueOf(1.99), agreement.getDailyRentalCharge());
        assertEquals(2, agreement.getChargeDays()); // Charge days should be 2 (July 5, 6)
        assertEquals(BigDecimal.valueOf(3.98), agreement.getPreDiscountCharge());
        assertEquals(BigDecimal.valueOf(3.98), agreement.getDiscountAmount());
        assertEquals(BigDecimal.ZERO.setScale(2), agreement.getFinalCharge());

        agreement.printAgreement(1);
    }

    @Test
    public void testMultipleToolsHolidayWeekend() {
        ShoppingCart cart = new ShoppingCart(chargeProcessor);

        LocalDate checkOutDate = LocalDate.of(2024, 8, 29); // Thursday before Labor Day weekend
        cart.addTool("JAKD", "Jackhammer", "DeWalt", 7, 0, 1, checkOutDate); // 1 jackhammer
        cart.addTool("CHNS", "Chainsaw", "Stihl", 7, 0, 1, checkOutDate); // 1 chainsaw

        List<RentalAgreement> agreements = cart.checkout();
        assertNotNull(agreements);
        assertEquals(2, agreements.size());

        RentalAgreement jackhammerAgreement = agreements.get(0);
        assertEquals("JAKD", jackhammerAgreement.getToolCode());
        assertEquals("Jackhammer", jackhammerAgreement.getToolType());
        assertEquals("DeWalt", jackhammerAgreement.getToolBrand());
        assertEquals(7, jackhammerAgreement.getRentalDays());
        assertEquals(checkOutDate, jackhammerAgreement.getCheckOutDate());
        assertEquals(checkOutDate.plusDays(jackhammerAgreement.getRentalDays() - 1), jackhammerAgreement.getDueDate());
        assertEquals(BigDecimal.valueOf(2.99), jackhammerAgreement.getDailyRentalCharge());
        assertEquals(4, jackhammerAgreement.getChargeDays()); // Charge days should be 4 (Aug 29, 30, Sep 3, 4)
        assertEquals(BigDecimal.valueOf(11.96), jackhammerAgreement.getPreDiscountCharge());
        assertEquals(BigDecimal.ZERO.setScale(2), jackhammerAgreement.getDiscountAmount());
        assertEquals(BigDecimal.valueOf(11.96), jackhammerAgreement.getFinalCharge());

        RentalAgreement chainsawAgreement = agreements.get(1);
        assertEquals("CHNS", chainsawAgreement.getToolCode());
        assertEquals("Chainsaw", chainsawAgreement.getToolType());
        assertEquals("Stihl", chainsawAgreement.getToolBrand());
        assertEquals(7, chainsawAgreement.getRentalDays());
        assertEquals(checkOutDate, chainsawAgreement.getCheckOutDate());
        assertEquals(checkOutDate.plusDays(chainsawAgreement.getRentalDays() - 1), chainsawAgreement.getDueDate());
        assertEquals(BigDecimal.valueOf(1.49), chainsawAgreement.getDailyRentalCharge());
        assertEquals(5, chainsawAgreement.getChargeDays()); // Charge days should be 5 (Aug 29, 30, 31, Sep 1, 2)
        assertEquals(BigDecimal.valueOf(7.45), chainsawAgreement.getPreDiscountCharge());
        assertEquals(BigDecimal.ZERO.setScale(2), chainsawAgreement.getDiscountAmount());
        assertEquals(BigDecimal.valueOf(7.45), chainsawAgreement.getFinalCharge());

        jackhammerAgreement.printAgreement(1);
        chainsawAgreement.printAgreement(1);
        cart.printConsolidatedAgreement();
    }

    @Test
    public void testMultipleToolsHolidayWeekendWithDiscount() {
        ShoppingCart cart = new ShoppingCart(chargeProcessor);

        LocalDate checkOutDate = LocalDate.of(2024, 8, 29); // Thursday before Labor Day weekend
        cart.addTool("JAKD", "Jackhammer", "DeWalt", 7, 20, 1, checkOutDate); // 1 jackhammer with 20% discount
        cart.addTool("CHNS", "Chainsaw", "Stihl", 7, 10, 1, checkOutDate); // 1 chainsaw with 10% discount

        List<RentalAgreement> agreements = cart.checkout();
        assertNotNull(agreements);
        assertEquals(2, agreements.size());

        RentalAgreement jackhammerAgreement = agreements.get(0);
        assertEquals("JAKD", jackhammerAgreement.getToolCode());
        assertEquals("Jackhammer", jackhammerAgreement.getToolType());
        assertEquals("DeWalt", jackhammerAgreement.getToolBrand());
        assertEquals(7, jackhammerAgreement.getRentalDays());
        assertEquals(checkOutDate, jackhammerAgreement.getCheckOutDate());
        assertEquals(checkOutDate.plusDays(jackhammerAgreement.getRentalDays() - 1), jackhammerAgreement.getDueDate());
        assertEquals(BigDecimal.valueOf(2.99), jackhammerAgreement.getDailyRentalCharge());
        assertEquals(4, jackhammerAgreement.getChargeDays()); // Charge days should be 4 (Aug 29, 30, Sep 3, 4)
        assertEquals(BigDecimal.valueOf(11.96), jackhammerAgreement.getPreDiscountCharge());
        assertEquals(BigDecimal.valueOf(2.39).setScale(2), jackhammerAgreement.getDiscountAmount());
        assertEquals(BigDecimal.valueOf(9.57).setScale(2), jackhammerAgreement.getFinalCharge());

        RentalAgreement chainsawAgreement = agreements.get(1);
        assertEquals("CHNS", chainsawAgreement.getToolCode());
        assertEquals("Chainsaw", chainsawAgreement.getToolType());
        assertEquals("Stihl", chainsawAgreement.getToolBrand());
        assertEquals(7, chainsawAgreement.getRentalDays());
        assertEquals(checkOutDate, chainsawAgreement.getCheckOutDate());
        assertEquals(checkOutDate.plusDays(chainsawAgreement.getRentalDays() - 1), chainsawAgreement.getDueDate());
        assertEquals(BigDecimal.valueOf(1.49), chainsawAgreement.getDailyRentalCharge());
        assertEquals(5, chainsawAgreement.getChargeDays()); // Charge days should be 5 (Aug 29, 30, 31, Sep 1, 2)
        assertEquals(BigDecimal.valueOf(7.45), chainsawAgreement.getPreDiscountCharge());
        assertEquals(BigDecimal.valueOf(0.75).setScale(2), chainsawAgreement.getDiscountAmount());
        assertEquals(BigDecimal.valueOf(6.70).setScale(2), chainsawAgreement.getFinalCharge());

        jackhammerAgreement.printAgreement(1);
        chainsawAgreement.printAgreement(1);
        cart.printConsolidatedAgreement();
    }

    @Test
    public void testInvalidDiscount() {
        ShoppingCart cart = new ShoppingCart(chargeProcessor);

        LocalDate checkOutDate = LocalDate.of(2024, 7, 4); // Independence Day
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> cart.addTool("LADW", "Ladder", "Werner", 2, 110, 1, checkOutDate), // Invalid discount
                "Expected addTool() to throw, but it didn't"
        );
        assertTrue(thrown.getMessage().contains("Discount percent must be between 0 and 100"));
    }

    @Test
    public void testInvalidRentalDays() {
        ShoppingCart cart = new ShoppingCart(chargeProcessor);

        LocalDate checkOutDate = LocalDate.of(2024, 7, 4); // Independence Day
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> cart.addTool("LADW", "Ladder", "Werner", 0, 0, 1, checkOutDate), // Invalid rental days
                "Expected addTool() to throw, but it didn't"
        );
        assertTrue(thrown.getMessage().contains("Rental day count must be 1 or greater"));
    }

    @Test
    public void testPrintConsolidatedAgreement() {
        ShoppingCart cart = new ShoppingCart(chargeProcessor);

        LocalDate checkOutDate = LocalDate.of(2024, 7, 4); // Independence Day
        cart.addTool("LADW", "Ladder", "Werner", 2, 0, 2, checkOutDate); // 2 ladders
        cart.addTool("CHNS", "Chainsaw", "Stihl", 3, 0, 1, checkOutDate); // 1 chainsaw

        cart.printConsolidatedAgreement();
    }
}
