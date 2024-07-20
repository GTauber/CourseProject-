package com.ead.authuser.models.exceptions;

import com.ead.authuser.models.errors.ResponseCode;

public class MismatchedOldPasswordException extends BaseException {
    public MismatchedOldPasswordException() {
        super(ResponseCode.MISMATCHED_OLD_PASSWORD);
    }
}
