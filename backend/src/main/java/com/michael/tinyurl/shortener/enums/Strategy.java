package com.michael.tinyurl.shortener.enums;

public enum Strategy {
    UNIQUE(false),
    MINIMIZE(true);

    private final boolean isDefault;

    Strategy(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public boolean isDefault() {
        return isDefault;
    }
}
