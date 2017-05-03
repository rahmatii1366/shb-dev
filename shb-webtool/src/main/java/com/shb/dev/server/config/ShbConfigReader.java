package com.shb.dev.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @author Mohammad Rahmati, 5/3/2017 8:01 AM
 */
public class ShbConfigReader {
    private static final Logger logger =
            Logger.getLogger(
                    ShbConfigReader.class);

    public static ShbConfig createFromJson(
            String configFilePath)
            throws Exception {
        return createFromJson(
                new FileInputStream(
                        new File(configFilePath)));
    }

    public static ShbConfig createFromJson(
            File configFile)
            throws Exception {
        return createFromJson(new FileInputStream(configFile));
    }

    public static ShbConfig createFromJson(
            InputStream inputStream)
            throws Exception {
        if(inputStream == null)
            throw new Exception("input stream is null");

        ShbConfig shbConfig =
                new ShbConfig();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            shbConfig.configMap = objectMapper
                    .readValue(
                            inputStream, Map.class);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return shbConfig;
    }
}
