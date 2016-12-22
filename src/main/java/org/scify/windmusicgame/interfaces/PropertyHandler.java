package org.scify.windmusicgame.interfaces;

public interface PropertyHandler {

    /**
     * Get a variable from project.properties file
     * @param propertyName the name of the property
     * @return the value of the given property
     */
    String getPropertyByName(String propertyFile, String propertyName);
    /**
     * Sets a property identified by it's name, to a given value
     * @param propertyFilePath the properties file
     * @param propertyName the name of the property
     * @param propertyValue the value that the property will be set to.
     */
    void setPropertyByName(String propertyFilePath, String propertyName, String propertyValue);
}
