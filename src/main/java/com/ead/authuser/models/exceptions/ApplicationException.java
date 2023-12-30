package com.ead.authuser.models.exceptions;

import com.ead.authuser.models.errors.ResponseCode;

public class ApplicationException extends BaseException {

    public ApplicationException() {
        super(ResponseCode.GENERIC_ERROR);
    }
}
