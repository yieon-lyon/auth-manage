package com.yieon.practice.auth.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class PracticeAuthException extends AbstractThrowableProblem {

    public PracticeAuthException(String message){
        super(ErrorConstants.DEFAULT_TYPE, message, Status.INTERNAL_SERVER_ERROR);
    }

    public PracticeAuthException(String message, String detail) {
        super(ErrorConstants.DEFAULT_TYPE, message, Status.INTERNAL_SERVER_ERROR, detail);
    }
}
