package org.scify.memorimusicgame.helper;

import org.scify.memorimusicgame.interfaces.PropertyHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by pisaris on 21/12/2016.
 */
public class PropertyHandlerImpl implements PropertyHandler {

    @Override
    public String getPropertyByName(String propertyFile, String propertyName) {
        Properties props = new Properties();
        File propertiesFile = new File(propertyFile);
        try {
            if (!propertiesFile.exists())
                propertiesFile.createNewFile();
            FileInputStream in = new FileInputStream(propertiesFile);
            props.load(in);
            return props.getProperty(String.valueOf(propertyName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setPropertyByName(String propertyFilePath, String propertyName, String propertyValue) {
        Properties props = new Properties();
        FileInputStream in = null;

        File propertyFile = new File(propertyFilePath);
        try {

            if(!propertyFile.exists())
                propertyFile.createNewFile();
            in = new FileInputStream(propertyFile);
            props.load(in);
            in.close();
            FileOutputStream out = new FileOutputStream(propertyFile);

            props.setProperty(propertyName, propertyValue);
            props.store(out, null);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
