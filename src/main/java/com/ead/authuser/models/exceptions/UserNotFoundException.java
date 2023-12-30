package com.ead.authuser.models.exceptions;

import com.ead.authuser.models.errors.ResponseCode;

public class UserNotFoundException extends BaseException {

    public UserNotFoundException() {
        super(ResponseCode.USER_NOT_FOUND);
    }
}
