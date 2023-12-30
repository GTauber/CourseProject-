package com.ead.authuser.models.exceptions;

import com.ead.authuser.models.errors.ResponseCode;

public class UsernameAlreadyExistsException extends BaseException {

    public UsernameAlreadyExistsException() {
        super(ResponseCode.USERNAME_ALREADY_EXISTS);
    }
}
