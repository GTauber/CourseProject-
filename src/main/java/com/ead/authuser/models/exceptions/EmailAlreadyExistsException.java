package com.ead.authuser.models.exceptions;

import com.ead.authuser.models.errors.ResponseCode;

public class EmailAlreadyExistsException extends BaseException {
    public EmailAlreadyExistsException() {
        super(ResponseCode.EMAIL_ALREADY_EXISTS);
    }
}
