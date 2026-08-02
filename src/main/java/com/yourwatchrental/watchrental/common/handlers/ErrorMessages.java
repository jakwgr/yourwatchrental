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
    USER_UPDATE_SAME_PHONE_NUMBER("You cannot change phone number to same one"),
    USER_CANNOT_LOGIN("Either the email or password is not accurate. Try again"),
    USER_UPDATE_SAME_PASSWORD("You cannot change password to same one"),
    USER_UPDATE_NOT_SAME_NEW_PASSWORDS("New passwords are not accurate. Try again"),
    USER_UPDATE_PASSWORD_DOESNT_MATCH("Check old password and try again"),
    USER_UPDATE_WRONG_PASSWORD("Check your password"),
    USER_DELETE_CONFIRM("The confirmation needs to be exact"),
    USER_UPDATE_SAME_STATUS("You cannot change status to same one"),
    USER_DISABLED_LOGIN_REGISTER("You cannot login. This account is disabled or suspended. Contact support via email to get more information"),

    WATCH_SAME_SERIAL("This serial number is already used"),
    WATCH_NOT_FOUND("This watch does not exist"),
    WATCH_SAME_SERIAL_AS_BEFORE("You cannot change serial number to same one"),

    WATCH_HISTORY_NOT_FOUND("This watch history does not exist"),

    WATCH_PHOTO_SAME_TYPE("Photo of this type is already used"),
    WATCH_PHOTO_NOT_FOUND("This photo does not exist"),

    ADMIN_SELF_DELETE("You cannot deactivate your own admin account"),

    RENTAL_WATCH_NOT_AVAILABLE("This watch is not available"),
    RENTAL_BAD_DATE_RANGE("Rental end date cannot be before start date and start needs to be at least today"),
    RENTAL_NOT_FOUND("This rental does not exist"),
    RENTAL_TOO_LATE_CHANGE_CANCEL("It is too late to cancel this rental. Please contact support for assistance."),
    RENTAL_PAYMENT_STATUS_CHANGE_NOT_ALLOWED("Payment status change is not allowed from the current status"),

    RESOURCE_FORBIDDEN("You don't have permission to access this resource");


    private final String message;
    ErrorMessages(String message){
        this.message = message;
    }
}
