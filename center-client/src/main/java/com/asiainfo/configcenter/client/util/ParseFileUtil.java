package com.asiainfo.configcenter.client.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ParseFileUtil {
    public static Properties parseFileToProperties(String fileName) throws IOException {
        Properties properties = new Properties();
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        properties.load(inputStream);
        return properties;
    }
}
