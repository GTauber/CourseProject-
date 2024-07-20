package com.ead.authuser.models.errors;

import java.util.ResourceBundle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseCode {

    GENERIC_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "generic"),
    EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "error.email_already_exists"),
    USERNAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "error.username_already_exists"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "error.user_not_found"),
    MISMATCHED_OLD_PASSWORD(HttpStatus.CONFLICT, "error.mismatched_old_password"),;

    private static final ResourceBundle resourceBundle;
    private static final String DEFAULT_RESOURCE = "messages.errors.messages";
    private static final String DEFAULT_MESSAGE = "Unknown error";

    static {
        resourceBundle = ResourceBundle.getBundle(DEFAULT_RESOURCE);
    }

    private final HttpStatus status;
    private final String key;

    public String getCode() {
        return this.name();
    }

    public String getMessage() {
        return resourceBundle.containsKey(this.key) ? resourceBundle.getString(this.key) : DEFAULT_MESSAGE;
    }

}
