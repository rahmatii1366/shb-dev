package com.shb.dev.server.http;

/**
 * @author Mohammad Rahmati, 4/23/2017 12:24 PM
 */
public enum ShbHttpServerType {
    NETTY("netty"),
    JETTY("jetty"),
    UNKNOWN("unknown");

    private String name;

    ShbHttpServerType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ShbHttpServerType fromName(
            String name) {
        if(name == null || name.isEmpty())
            return UNKNOWN;
        for(ShbHttpServerType s
                : ShbHttpServerType.values()) {
            if(s.name.equalsIgnoreCase(name))
                return s;
        }
        return UNKNOWN;
    }
}
