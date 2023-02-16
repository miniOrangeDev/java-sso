package com.miniorange.app.helpers;

import java.util.Properties;

public class MoSAMLSettings {
    static Properties properties;

    public MoSAMLSettings(Properties p) {
        properties = new Properties(p);
    }

    public String getIdpEntityId() {
        return properties.getProperty("idpEntityId");
    }

    public String getSamlLoginUrl() {
        return properties.getProperty("samlLoginUrl");
    }

    public String getNameIDFormat() {
        return properties.getProperty("nameIDFormat");
    }

    public String getSpBaseUrl() {
        return properties.getProperty("spBaseUrl");
    }

    public String getSpEntityId() {
        return properties.getProperty("spEntityId");
    }

    public String getAcsUrl() {
        return properties.getProperty("acsUrl");
    }

    public String getApplicationUrl() {
        return properties.getProperty("applicationUrl");
    }
}

