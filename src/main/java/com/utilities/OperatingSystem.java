package com.utilities;

public enum OperatingSystem {
    WINDOWS, LINUX, MAC, OTHER;

    public static OperatingSystem getCurrentOS() {
        final String name = System.getProperty("os.name");

        if (name == null) {
            throw new IllegalStateException("It is not possible retrieve Operating System from current virtual machine!");
        }

        final OperatingSystem type;

        if (name.contains("Mac")) {
            type = OperatingSystem.MAC;
        } else if (name.contains("Windows")) {
            type = OperatingSystem.WINDOWS;
        } else if (name.contains("linux")) {
            type = OperatingSystem.LINUX;
        } else {
            type = OperatingSystem.OTHER;
        }
        return type;
    }
}
