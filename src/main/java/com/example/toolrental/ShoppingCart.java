/**
 * ShoppingCart class for the Tool Rental System application.
 *
 * This class manages the tools selected for rental, calculates the charges
 * based on rental periods, applies discounts, and generates rental agreements.
 * It also supports printing individual and consolidated rental agreements to
 * the console and logging the details for record-keeping.
 *
 * Example usage:
 * - Used to add tools to a rental cart, calculate charges, and generate rental agreements.
 *
 * @version 1.0
 */
package com.example.toolrental;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ShoppingCart {

    private final List<ToolRental> rentals = new ArrayList<>();
    private final ChargeProcessor chargeProcessor;

    public ShoppingCart(ChargeProcessor chargeProcessor) {
        this.chargeProcessor = chargeProcessor;
    }

    public void addTool(String toolCode, String toolType, String brand, int rentalDays, int discountPercent, int quantity, LocalDate checkOutDate) {
        if (rentalDays < 1) {
            throw new IllegalArgumentException("Rental day count must be 1 or greater");
        }
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Discount percent must be between 0 and 100");
        }

        rentals.add(new ToolRental(toolCode, toolType, brand, rentalDays, discountPercent, quantity, checkOutDate));
    }

    public List<RentalAgreement> checkout() {
        List<RentalAgreement> agreements = new ArrayList<>();
        for (ToolRental rental : rentals) {
            RentalAgreement agreement = new RentalAgreement(
                    rental.getToolCode(),
                    rental.getToolType(),
                    rental.getBrand(),
                    rental.getRentalDays(),
                    rental.getCheckOutDate(),
                    rental.getDiscountPercent(),
                    rental.getQuantity(),
                    chargeProcessor
            );
            agreements.add(agreement);
        }
        return agreements;
    }

    public void printConsolidatedAgreement() {
        if (rentals.isEmpty()) {
            System.out.println("No tools in the cart.");
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");

        StringBuilder agreement = new StringBuilder();
        agreement.append("\n********** Consolidated Rental Agreement **********\n");

        BigDecimal totalPreDiscountCharge = BigDecimal.ZERO;
        BigDecimal totalDiscountAmount = BigDecimal.ZERO;
        BigDecimal totalFinalCharge = BigDecimal.ZERO;

        List<RentalAgreement> agreements = checkout();
        for (int i = 0; i < agreements.size(); i++) {
            RentalAgreement rentalAgreement = agreements.get(i);
            agreement.append(String.format("\n---------- Tool %d Information (%s - %s) ----------%n", i + 1, rentalAgreement.getToolCode(), rentalAgreement.getToolType()));
            agreement.append(String.format("Tool code: %s%n", rentalAgreement.getToolCode()));
            agreement.append(String.format("Tool type: %s%n", rentalAgreement.getToolType()));
            agreement.append(String.format("Tool brand: %s%n", rentalAgreement.getToolBrand()));
            agreement.append(String.format("Rental days: %d%n", rentalAgreement.getRentalDays()));
            agreement.append(String.format("Check out date: %s%n", rentalAgreement.getCheckOutDate().format(formatter)));
            agreement.append(String.format("Due date: %s%n", rentalAgreement.getDueDate().plusDays(1).format(formatter)));
            agreement.append("\n");
            agreement.append("------------ Charges -----------------\n");
            agreement.append(String.format("Daily rental charge per tool: $%.2f%n", rentalAgreement.getDailyRentalCharge()));
            agreement.append(String.format("Quantity: %d%n", rentalAgreement.getQuantity()));
            agreement.append(String.format("Charge days: %d%n", rentalAgreement.getChargeDays()));
            agreement.append(String.format("Pre-discount charge: $%.2f%n", rentalAgreement.getPreDiscountCharge()));
            agreement.append(String.format("Discount percent: %d%%%n", rentalAgreement.getDiscountPercent()));
            agreement.append(String.format("Discount amount: $%.2f%n", rentalAgreement.getDiscountAmount()));
            agreement.append(String.format("Final charge: $%.2f%n", rentalAgreement.getFinalCharge()));

            totalPreDiscountCharge = totalPreDiscountCharge.add(rentalAgreement.getPreDiscountCharge());
            totalDiscountAmount = totalDiscountAmount.add(rentalAgreement.getDiscountAmount());
            totalFinalCharge = totalFinalCharge.add(rentalAgreement.getFinalCharge());

            if (i < agreements.size() - 1) {
                agreement.append("\n------------------------------\n");
            }
        }

        agreement.append("\n");
        agreement.append("\n============== Summary ==============\n");
        agreement.append(String.format("Total pre-discount charge: $%.2f%n", totalPreDiscountCharge));
        agreement.append(String.format("Total discount amount: $%.2f%n", totalDiscountAmount));
        agreement.append(String.format("Total final charge: $%.2f%n", totalFinalCharge));
        agreement.append("**************************************\n");

        // Print to console
        System.out.println(agreement.toString());

        // Log the agreement
        Logger logger = LoggerConfig.getLogger();
        logger.info(agreement.toString());
    }

    private static class ToolRental {

        private final String toolCode;
        private final String toolType;
        private final String brand;
        private final int rentalDays;
        private final int discountPercent;
        private final int quantity;
        private final LocalDate checkOutDate;

        public ToolRental(String toolCode, String toolType, String brand, int rentalDays, int discountPercent, int quantity, LocalDate checkOutDate) {
            this.toolCode = toolCode;
            this.toolType = toolType;
            this.brand = brand;
            this.rentalDays = rentalDays;
            this.discountPercent = discountPercent;
            this.quantity = quantity;
            this.checkOutDate = checkOutDate;
        }

        public String getToolCode() {
            return toolCode;
        }

        public String getToolType() {
            return toolType;
        }

        public String getBrand() {
            return brand;
        }

        public int getRentalDays() {
            return rentalDays;
        }

        public int getDiscountPercent() {
            return discountPercent;
        }

        public int getQuantity() {
            return quantity;
        }

        public LocalDate getCheckOutDate() {
            return checkOutDate;
        }
    }
}
