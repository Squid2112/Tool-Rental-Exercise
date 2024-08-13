/**
 * ChargeProcessorConfig class for the Tool Rental System application.
 *
 * This class is responsible for loading and parsing tool charge configuration
 * files in JSON or YAML format. It constructs a list of ChargeRule objects
 * based on the tool charges defined in the configuration file. The ChargeRule
 * objects are used by the ChargeProcessor to calculate rental charges.
 *
 * Key functionalities:
 * - Reading and parsing configuration files (JSON and YAML) to retrieve tool charge details.
 * - Constructing ChargeRule objects from the parsed tool charge data.
 * - Providing a method to get the list of ChargeRule objects for use in charge calculations.
 *
 * Components:
 * - CONFIG_PATH: Directory path where the configuration files are located.
 * - getChargeRules: Main method to read and parse the configuration file and create ChargeRule objects.
 * - getConfigFilePath: Helper method to build the file path based on the configuration type (JSON or YAML).
 * - readJsonConfig: Helper method to read and parse JSON configuration files.
 * - readYamlConfig: Helper method to read and parse YAML configuration files.
 *
 * Example usage:
 * - Load tool charge rules from a JSON or YAML configuration file to initialize the ChargeProcessor.
 *
 * @version 1.0
 */
package com.example.toolrental;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ChargeProcessorConfig {

    private static final String CONFIG_PATH = "src/main/resources/config/";

    public static List<ChargeRule> getChargeRules(ConfigFileType configType, String configFileName, HolidayProcessor holidayProcessor) throws IOException {
        ToolChargeConfig config;
        String configFilePath = getConfigFilePath(configType, configFileName);

        switch (configType) {
            case JSON:
                config = readJsonConfig(configFilePath);
                break;
            case YAML:
                config = readYamlConfig(configFilePath);
                break;
            default:
                throw new IllegalArgumentException("Unsupported config type: " + configType);
        }

        List<ChargeRule> chargeRules = new ArrayList<>();
        List<ToolChargeConfig.ToolCharge> toolCharges = config.getTools();

        for (ToolChargeConfig.ToolCharge toolCharge : toolCharges) {

            ChargeRule rule = new ChargeRule(
                    toolCharge.getToolCode(),
                    toolCharge.getToolType(),
                    toolCharge.getToolBrand(),
                    toolCharge.getWeekdayCharge(),
                    toolCharge.getWeekendCharge(),
                    toolCharge.getHolidayCharge(),
                    holidayProcessor
            );
            chargeRules.add(rule);

        }

        return chargeRules;
    }

    private static String getConfigFilePath(ConfigFileType configType, String configFileName) {
        switch (configType) {
            case JSON:
                return CONFIG_PATH + configFileName + ".json";
            case YAML:
                return CONFIG_PATH + configFileName + ".yaml";
            default:
                throw new IllegalArgumentException("Unsupported config type: " + configType);
        }
    }

    private static ToolChargeConfig readJsonConfig(String configFilePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(configFilePath), ToolChargeConfig.class);
    }

    private static ToolChargeConfig readYamlConfig(String configFilePath) throws IOException {
        Yaml yaml = new Yaml(new Constructor(ToolChargeConfig.class));
        InputStream inputStream = new FileInputStream(configFilePath);
        ToolChargeConfig toolChargeConfig;

        try {
            toolChargeConfig = yaml.load(inputStream);
        } finally {
            inputStream.close();
        }

        return toolChargeConfig;
    }
}
