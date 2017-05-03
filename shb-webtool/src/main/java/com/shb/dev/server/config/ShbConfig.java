package com.shb.dev.server.config;

import java.util.List;
import java.util.Map;

/**
 * @author Mohammad Rahmati, 5/3/2017 7:59 AM
 */
public class ShbConfig {
    protected Map<String, Object> configMap = null;

    protected ShbConfig() {}

    protected ShbConfig(Map<String, Object> configMap) {
        this.configMap = configMap;
    }

    protected String getString(String key) {
        if(configMap == null)
            return null;
        Object o = configMap.get(key);
        if(o instanceof String)
            return (String)o;
        return null;
    }

    protected List<String> getList(String key) {
        if(configMap == null)
            return null;
        Object o = configMap.get(key);
        if(o instanceof List)
            return (List<String>) o;
        return null;
    }

    protected Map<String, Object> getMap(String key) {
        if(configMap == null)
            return null;
        Object o = configMap.get(key);
        if(o instanceof Map)
            return (Map<String, Object>) o;
        return null;
    }

    protected ShbConfig getShbConfig(String key) {
        if(configMap == null)
            return null;
        Object o = configMap.get(key);
        if(o instanceof Map)
            return new ShbConfig((Map<String, Object>) o);
        return null;
    }

    public void reconfigure(ShbConfig shbConfig) {
        this.configMap = shbConfig.configMap;
    }

    public void reconfigure(Map<String, Object> configMap) {
        this.configMap = configMap;
    }
}
