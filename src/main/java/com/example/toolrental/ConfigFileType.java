/**
 * ConfigFileType enum for the Tool Rental System application.
 *
 * This enum represents the types of configuration files that can be used
 * for loading tool charge rules. The supported configuration file types
 * are JSON and YAML.
 *
 * Enum values:
 * - JSON: Represents a configuration file in JSON format.
 * - YAML: Represents a configuration file in YAML format.
 *
 * Example usage:
 * - Used by the ChargeProcessorConfig class to determine the format of the configuration file to be loaded.
 *
 * @version 1.0
 */
package com.example.toolrental;

public enum ConfigFileType {
    JSON,
    YAML
}
