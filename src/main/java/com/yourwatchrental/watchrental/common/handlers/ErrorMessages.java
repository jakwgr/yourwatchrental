package com.yourwatchrental.watchrental.common.handlers;

import lombok.Getter;

import java.util.UUID;

@Getter
public enum ErrorMessages {
    BRANCH_NOT_FOUND("This branch does not exist"),
    SAME_PHONE_NUMBER("This phone number is already used"),
    SAME_EMAIL("This email is already used");

    private final String message;

    ErrorMessages(String message){
        this.message = message;
    }

    public String getMessageWithId(UUID id) {
        return this.message + (" ID : '") + id + ("'");
    }
}
