/**
 * Tool class representing a rental tool in the Tool Rental System application.
 *
 * This class holds information about a tool, including its code, type, and brand.
 * It is used to manage tool details within the rental system.
 *
 * Example usage:
 * - Used to create tool instances with specific details such as tool code, type, and brand.
 *
 * @version 1.0
 */
package com.example.toolrental;

public class Tool {

    private final String toolCode;
    private final String toolType;
    private final String brand;

    // Constructor
    public Tool(String toolCode, String toolType, String brand) {
        this.toolCode = toolCode;
        this.toolType = toolType;
        this.brand = brand;
    }

    // Getters and Setters
    public String getToolCode() {
        return toolCode;
    }

    public String getToolType() {
        return toolType;
    }

    public String getBrand() {
        return brand;
    }
}
