package com.whalefall.learncases.spel;

import lombok.Getter;

@Getter
public enum TypeEnum {
    TYPE1("SPRING"),
    TYPE2("WINNER");
    private final String season;

    TypeEnum(String season) {
        this.season = season;
    }

}
