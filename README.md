# Tool Rental System Application

## Overview

The Tool Rental System Application is designed to facilitate the rental of various tools. It allows users to select tools, calculate rental charges including discounts and exemptions, and generate detailed rental agreements. The application supports holidays and weekends charge rules and logs rental agreements for record-keeping.

## Features

- Add tools to a shopping cart for rental
- Calculate rental charges based on tool type, rental period, and applicable discounts
- Apply holiday and weekend exemptions
- Generate and print detailed rental agreements
- Log rental agreements for record-keeping

## Components

### Main Components

1. **ShoppingCart**
   - Manages the collection of tools selected for rental.
   - Handles adding tools, calculating charges, and generating rental agreements.

2. **ChargeProcessor**
   - Calculates rental charges based on predefined rules.
   - Applies discounts and exemptions for holidays and weekends.

3. **HolidayProcessor**
   - Manages holiday rules.
   - Determines if a given date is a holiday.

4. **RentalAgreement**
   - Generates detailed rental agreements based on rental details and charges.

5. **Logger**
   - Logs the rental agreements and other important events.
   - Provides console and file logging capabilities.

6. **Configuration Files**
   - JSON/YAML files that contain the charge rules and holiday definitions.

### Auxiliary Components

1. **ChargeRule**
   - Represents the charging rules for a specific tool, including weekday, weekend, and holiday charges.

2. **HolidayRule**
   - Interface for defining holiday rules.

3. **IndependenceDayRule**
   - Defines the rules for Independence Day holiday.

4. **LaborDayRule**
   - Defines the rules for Labor Day holiday.


## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- Maven

### Setup

1. Clone the repository:

  https://github.com/Squid2112/jg0724.git

2. Navigate to the project directory:

  cd tool-rental-system

3. Build the project using Maven:

  mvn clean install


### Running the Application

To run the application, use the following command:

  mvn exec -Dexec.mainClass="com.example.toolrental.Main"


### Running Tests

To run the unit tests, use the following command:

  mvn test


## Usage

The main method demonstrates the process of adding tools to the shopping cart, checking out, and printing individual rental agreements and a consolidated rental agreement to the console.

### Example Usage

```java
public class Main {

    public static void main(String[] args) {
        // Initialize and setup components
        // Add tools to the shopping cart
        // Checkout and print agreements
    }
}

LocalDate checkOutDate = LocalDate.of(2024, 7, 2); // Example checkout date
cart.addTool("LADW", "Ladder", "Werner", 2, 0, 1, checkOutDate);
cart.addTool("CHNS", "Chainsaw", "Stihl", 3, 15, 1, checkOutDate);

List<RentalAgreement> agreements = cart.checkout();
for (int i = 0; i < agreements.size(); i++) {
    RentalAgreement agreement = agreements.get(i);
    agreement.printAgreement(i + 1); // Pass the agreement number (starting from 1)
}

cart.printConsolidatedAgreement();
```

License
This project is licensed under the MIT License - see the LICENSE file for details.

Acknowledgements
JUnit 5
Maven
SnakeYAML
