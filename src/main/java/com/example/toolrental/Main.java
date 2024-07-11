/**
 * Main class for the Tool Rental System application.
 *
 * This class serves as the entry point for the tool rental application.
 * It initializes the necessary components such as the HolidayProcessor,
 * ChargeProcessor, and ShoppingCart. It also loads charge rules from
 * a configuration file and sets up holiday rules.
 *
 * The main method demonstrates the process of adding tools to the shopping
 * cart, checking out, and printing individual rental agreements and a
 * consolidated rental agreement to the console.
 *
 * Components initialized:
 * - HolidayProcessor: Manages holiday rules and checks for holidays.
 * - ChargeProcessor: Handles the calculation of rental charges based on
 *   tool type, rental period, and applicable discounts.
 * - ShoppingCart: Manages the collection of tools to be rented in a
 *   single transaction.
 *
 * Example use case:
 * - Adding multiple tools to the shopping cart.
 * - Checking out and generating rental agreements.
 * - Printing individual and consolidated rental agreements.
 *
 * Note: The configuration for charge rules is loaded from a JSON file.
 *
 * @version 1.0
 */
package com.example.toolrental;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            // Set up the logger configuration
            LoggerConfig.setup();
        } catch (IOException e) {
            System.err.println("Error setting up logger: " + e.getMessage());
            return;
        }

        try {
            // Initialize HolidayProcessor and add holiday rules
            HolidayProcessor holidayProcessor = new HolidayProcessor();
            holidayProcessor.addHolidayRule(new IndependenceDayRule());
            holidayProcessor.addHolidayRule(new LaborDayRule());

            // Load Charge Rules from JSON configuration
            List<ChargeRule> chargeRules = ChargeProcessorConfig.getChargeRules(ConfigFileType.JSON, "tools", holidayProcessor);
            ChargeProcessor chargeProcessor = new ChargeProcessor(chargeRules, holidayProcessor);

            // Create a ShoppingCart instance
            ShoppingCart cart = new ShoppingCart(chargeProcessor);

            // Add tools to the shopping cart with example checkout date (July 2, 2024)
            LocalDate checkOutDate = LocalDate.of(2024, 7, 2); // Example checkout date before Independence Day
            cart.addTool("LADW", "Ladder", "Werner", 2, 0, 1, checkOutDate); // Add 1 ladder for 2 days without discount
            cart.addTool("CHNS", "Chainsaw", "Stihl", 3, 15, 1, checkOutDate); // Add 1 chainsaw for 3 days with 15% discount
            cart.addTool("JAKD", "Jackhammer", "DeWalt", 2, 0, 2, checkOutDate); // Add 2 DeWalt jackhammers for 2 days without discount
            cart.addTool("JAKR", "Jackhammer", "Ridgid", 3, 10, 1, checkOutDate); // Add 1 Ridgid jackhammer for 3 days with 10% discount
            cart.addTool("LADW", "Ladder", "Werner", 4, 10, 3, checkOutDate); // Add 3 ladders for 4 days with 10% discount

            // Checkout and print individual agreements
            List<RentalAgreement> agreements = cart.checkout();
            for (int i = 0; i < agreements.size(); i++) {
                RentalAgreement agreement = agreements.get(i);
                agreement.printAgreement(i + 1); // Print each agreement with its number
            }

            // Print consolidated agreement for all tools in the cart
            cart.printConsolidatedAgreement();

        } catch (IOException e) {
            System.err.println("Error processing charge rules: " + e.getMessage());
        }
    }
}
