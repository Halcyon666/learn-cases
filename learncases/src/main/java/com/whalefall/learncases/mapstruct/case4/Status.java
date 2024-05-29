package com.whalefall.learncases.mapstruct.case4;

import lombok.Getter;

/**
 * @author Halcyon
 * @date 2024/5/29 20:58
 * @since 1.0.0
 */
@SuppressWarnings("unused")
@Getter
public enum Status {
    A("active"),
    C("closed"),
    ;
    private final String accountStatus;

    Status(String status) {
        this.accountStatus = status;
    }

    public static Status getStatus(String status) {
        return switch (status) {
            case "active" -> Status.A;
            case "closed" -> Status.C;
            default -> null;
        };
    }


}
