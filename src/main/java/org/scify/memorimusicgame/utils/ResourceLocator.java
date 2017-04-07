package org.scify.memorimusicgame.utils;

import org.scify.memorimusicgame.fx.FXAudioEngine;
import org.scify.memorimusicgame.helper.MemoriConfiguration;

import java.net.URL;

public class ResourceLocator {
    private String rootDataPath;
    private String rootDataPathDefault;
    private MemoriConfiguration configuration;

    public ResourceLocator() {
        configuration = new MemoriConfiguration();
        this.rootDataPath = "/" + configuration.getProjectProperty("DATA_PACK") + "/";
        this.rootDataPathDefault = "/" + configuration.getProjectProperty("DATA_PACK_DEFAULT") + "/";
    }



    /**
     * Given a path that represents a resource, tries to find if the resource is available in the current data pack
     * If not available, loads the corresponding file from the default pack
     *
     * @param path the path of the desired file
     * @param fileName the name of the desired file
     * @return the resource path in which the file is available (current data pack or default data pack)
     */
    public String getCorrectPathForFile(String path, String fileName) {
        String filePath = path + fileName;

        String file = this.rootDataPath + filePath;

        URL fileURL = FXAudioEngine.class.getResource(file);
        if (fileURL == null) {
            file = this.rootDataPathDefault + filePath;
            System.out.println("File " + this.rootDataPath + path + fileName + " not found. Loaded default: " + file);
        } else {
            System.out.println("Got existing file: " + file);
        }
        return file;
    }
}
