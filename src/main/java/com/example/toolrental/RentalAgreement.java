/**
 * RentalAgreement class for the Tool Rental System application.
 *
 * This class represents a rental agreement for a tool, including details such as
 * tool information, rental period, charges, discounts, and final cost.
 * It includes methods for calculating chargeable days, pre-discount charges,
 * discount amounts, and final charges. The agreement details can be printed to
 * the console and logged for record-keeping.
 *
 * Example usage:
 * - Used in the ShoppingCart class to create rental agreements for tools added to the cart.
 *
 * @version 1.0
 */
package com.example.toolrental;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RentalAgreement {

    private static final Logger logger = LoggerConfig.getLogger();

    private final String toolCode;
    private final String toolType;
    private final String toolBrand;
    private final int rentalDays;
    private final LocalDate checkOutDate;
    private final LocalDate dueDate;
    private final BigDecimal dailyRentalCharge;
    private final BigDecimal preDiscountCharge;
    private final int discountPercent;
    private final BigDecimal discountAmount;
    private final BigDecimal finalCharge;
    private final int quantity;
    private int totalChargeDays;

    public RentalAgreement(String toolCode, String toolType, String toolBrand, int rentalDays, LocalDate checkOutDate, int discountPercent, int quantity, ChargeProcessor chargeProcessor) {
        this.toolCode = toolCode;
        this.toolType = toolType;
        this.toolBrand = toolBrand;
        this.rentalDays = rentalDays;
        this.checkOutDate = checkOutDate;
        this.dueDate = checkOutDate.plusDays(rentalDays - 1);
        this.discountPercent = discountPercent;
        this.quantity = quantity;

        // Validation
        if (rentalDays < 1) {
            throw new IllegalArgumentException("Invalid rental days");
        }
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Invalid discount percentage");
        }

        // Find the appropriate ChargeRule for this tool
        ChargeRule chargeRule = chargeProcessor.getChargeRules().stream()
                .filter(rule -> toolCode.equals(rule.getToolCode()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Charge rule not found for tool code: " + toolCode));

        // Calculating the daily rental charge based on the tool's charge rule
        LocalDate firstChargeableDay = findFirstChargeableDay(checkOutDate, chargeRule, chargeProcessor.getHolidayProcessor());
        this.dailyRentalCharge = chargeRule.getCharge(firstChargeableDay);
        logger.log(Level.INFO, "Daily Rental Charge: {0}", this.dailyRentalCharge);

        // Calculate chargeable days excluding holidays
        this.totalChargeDays = calculateChargeDays(checkOutDate, dueDate, chargeRule, chargeProcessor.getHolidayProcessor());
        this.preDiscountCharge = calculatePreDiscountCharge(this.totalChargeDays, this.dailyRentalCharge, this.quantity);
        this.discountAmount = calculateDiscountAmount(this.preDiscountCharge, this.discountPercent);
        this.finalCharge = calculateFinalCharge(this.preDiscountCharge, this.discountAmount);
    }

    private LocalDate findFirstChargeableDay(LocalDate startDate, ChargeRule chargeRule, HolidayProcessor holidayProcessor) {
        LocalDate date = startDate;
        while (holidayProcessor.isHoliday(date) || !chargeRule.applies(date)) {
            date = date.plusDays(1);
        }

        return date;
    }

    private int calculateChargeDays(LocalDate startDate, LocalDate endDate, ChargeRule chargeRule, HolidayProcessor holidayProcessor) {
        this.totalChargeDays = 0;

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            boolean isHoliday = holidayProcessor.isHoliday(date);
            boolean isWeekend = date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;

            // Determine if the day should be charged
            if ((isHoliday && chargeRule.getHolidayCharge().compareTo(BigDecimal.ZERO) > 0)
                    || (isWeekend && chargeRule.getWeekendCharge().compareTo(BigDecimal.ZERO) > 0)
                    || (!isHoliday && !isWeekend && chargeRule.getWeekdayCharge().compareTo(BigDecimal.ZERO) > 0)) {

                this.totalChargeDays++;
            }
        }

        return this.totalChargeDays;
    }

    private BigDecimal calculatePreDiscountCharge(int chargeDays, BigDecimal dailyRentalCharge, int quantity) {
        return dailyRentalCharge.multiply(BigDecimal.valueOf(chargeDays)).multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateDiscountAmount(BigDecimal preDiscountCharge, int discountPercent) {
        return preDiscountCharge.multiply(BigDecimal.valueOf(discountPercent)).divide(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateFinalCharge(BigDecimal preDiscountCharge, BigDecimal discountAmount) {
        return preDiscountCharge.subtract(discountAmount).setScale(2, RoundingMode.HALF_UP);
    }

    public void printAgreement(int agreementNumber) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");

        StringBuilder agreement = new StringBuilder();
        agreement.append(String.format("********** Rental Agreement %d **********%n", agreementNumber));
        agreement.append("---------- Tool Information ----------\n");
        agreement.append(String.format("Tool code: %s%n", toolCode));
        agreement.append(String.format("Tool type: %s%n", toolType));
        agreement.append(String.format("Tool brand: %s%n", toolBrand));
        agreement.append(String.format("Rental days: %d%n", rentalDays));
        agreement.append(String.format("Check out date: %s%n", checkOutDate.format(formatter)));
        agreement.append(String.format("Due date: %s%n", dueDate.plusDays(1).format(formatter)));
        agreement.append("--------------------------------------\n");
        agreement.append("------------ Charges -----------------\n");
        agreement.append(String.format("Daily rental charge per tool: $%.2f%n", dailyRentalCharge));
        agreement.append(String.format("Quantity: %d%n", quantity));
        agreement.append(String.format("Charge days: %d%n", totalChargeDays));
        agreement.append(String.format("Pre-discount charge: $%.2f%n", preDiscountCharge));
        agreement.append(String.format("Discount percent: %d%%%n", discountPercent));
        agreement.append(String.format("Discount amount: $%.2f%n", discountAmount));
        agreement.append(String.format("** Final charge: $%.2f **%n", finalCharge));
        agreement.append("**************************************\n");

        // Print to console
        System.out.println(agreement.toString());

        // Log the agreement
        logger.info(agreement.toString());
    }

    // Getters for testing purposes
    public String getToolCode() {
        return toolCode;
    }

    public String getToolType() {
        return toolType;
    }

    public String getToolBrand() {
        return toolBrand;
    }

    public int getRentalDays() {
        return rentalDays;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public BigDecimal getDailyRentalCharge() {
        return dailyRentalCharge;
    }

    public int getChargeDays() {
        return totalChargeDays;
    }

    public BigDecimal getPreDiscountCharge() {
        return preDiscountCharge;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public BigDecimal getFinalCharge() {
        return finalCharge;
    }

    public int getQuantity() {
        return quantity;
    }
}
