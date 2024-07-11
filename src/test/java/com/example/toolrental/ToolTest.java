/**
 * Test class for the Tool class in the Tool Rental System application.
 *
 * This class contains unit tests for the Tool class, verifying that
 * tool objects are correctly instantiated and their properties are
 * accurately accessed.
 *
 * Test cases:
 * - Testing the Tool constructor to ensure objects are created successfully.
 * - Verifying the correct retrieval of tool code, tool type, and brand.
 *
 * Each test ensures the proper functioning of the Tool class and its methods.
 *
 * @version 1.0
 */
package com.example.toolrental;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ToolTest {

    private Tool tool;

    @BeforeEach
    public void setUp() {
        tool = new Tool("CHNS", "Chainsaw", "Stihl");
    }

    @Test
    public void testConstructor() {
        Tool tmpTool = new Tool("LADW", "Ladder", "Werner");
        assertNotNull(tmpTool);
    }

    @Test
    public void testGetToolCode() {
        assertEquals("CHNS", tool.getToolCode());
    }

    @Test
    public void testGetToolType() {
        assertEquals("Chainsaw", tool.getToolType());
    }

    @Test
    public void testGetBrand() {
        assertEquals("Stihl", tool.getBrand());
    }
}
