package com.yourwatchrental.watchrental.common.handlers;

import lombok.Getter;

@Getter
public enum ErrorMessages {
    BRANCH_NOT_FOUND("This branch does not exist"),
    SAME_PHONE_NUMBER("This phone number is already used"),
    SAME_EMAIL("This email is already used"),
    ID_NOT_FOUND("This id does not exist"),
    USER_NOT_FOUND("This user does not exist"),
    USER_UPDATE_SAME_EMAIL("You cannot change email to same one"),
    USER_UPDATE_SAME_PHONE_NUMBER("You cannot change phone number to same one");

    private final String message;

    ErrorMessages(String message){
        this.message = message;
    }
}
