package com.yieon.practice.auth.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class AuthApiException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public AuthApiException(String message){
        super(ErrorConstants.DEFAULT_TYPE, message, Status.INTERNAL_SERVER_ERROR);
    }

    public AuthApiException(String message, String detail) {
        super(ErrorConstants.DEFAULT_TYPE, message, Status.INTERNAL_SERVER_ERROR, detail);
    }
}
