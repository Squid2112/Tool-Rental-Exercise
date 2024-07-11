/**
 * ToolChargeConfig class for configuring tool charges in the Tool Rental System application.
 *
 * This class holds the configuration for tool charges, including weekday, weekend, and holiday charges.
 * It is used to manage and load tool charge configurations from external sources.
 *
 * Example usage:
 * - Used to store and retrieve tool charge details such as tool code, type, brand, and charges.
 *
 * @version 1.0
 */
package com.example.toolrental;

import java.math.BigDecimal;
import java.util.List;

public class ToolChargeConfig {

    private List<ToolCharge> tools;

    public List<ToolCharge> getTools() {
        return tools;
    }

    public void setTools(List<ToolCharge> tools) {
        this.tools = tools;
    }

    public static class ToolCharge {

        private String toolCode;
        private String toolType;
        private String brand;
        private BigDecimal weekdayCharge;
        private BigDecimal weekendCharge;
        private BigDecimal holidayCharge;

        public String getToolCode() {
            return toolCode;
        }

        public void setToolCode(String toolCode) {
            this.toolCode = toolCode;
        }

        public String getToolType() {
            return toolType;
        }

        public void setToolType(String toolType) {
            this.toolType = toolType;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public BigDecimal getWeekdayCharge() {
            return weekdayCharge;
        }

        public void setWeekdayCharge(BigDecimal weekdayCharge) {
            this.weekdayCharge = weekdayCharge;
        }

        public BigDecimal getWeekendCharge() {
            return weekendCharge;
        }

        public void setWeekendCharge(BigDecimal weekendCharge) {
            this.weekendCharge = weekendCharge;
        }

        public BigDecimal getHolidayCharge() {
            return holidayCharge;
        }

        public void setHolidayCharge(BigDecimal holidayCharge) {
            this.holidayCharge = holidayCharge;
        }
    }
}
