package com.whalefall.learncases.spel;

public enum TypeEnum {
    TYPE1("SPRING"),
    TYPE2("WINNER");
    private final String type;

    TypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
