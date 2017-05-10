package com.shb.dev.server.role;

/**
 * @author Mohammad Rahmati, 4/22/2017 2:41 PM
 */
public enum ShbRoleType {
    ADMIN("ADMIN", (byte)0xFF),
    USER("USER", (byte)0x03),
    GUEST("GUEST", (byte)0x01);

    private String name;
    private byte scheme;

    private ShbRoleType(
            String name, byte scheme) {
        this.name = name;
        this.scheme = scheme;
    }

    public String getName() {
        return name;
    }

    public int getScheme() {
        return scheme;
    }

    public static ShbRoleType getFromName(String name)
            throws Exception {
        if(name == null || name.isEmpty())
            return GUEST;
        for (ShbRoleType ruleType : ShbRoleType.values()) {
            if(ruleType.name.equalsIgnoreCase(name))
                return ruleType;
        }
        throw new Exception("this type not exist");
    }

    public boolean isValid(ShbRoleType ruleType) {
        if((scheme & ruleType.scheme) != scheme)
            return false;
        return true;
    }
}
