package com.yieon.practice.auth.security;

import com.yieon.practice.auth.web.rest.errors.PracticeAuthException;
import com.yieon.practice.auth.web.rest.errors.InvalidPasswordException;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.util.Objects;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-09-11
 * <PRE>
 * ------------------------
 * summary : RestTemplate Error Handler
 * ------------------------
 * Revision history
 * 2023-09-11. yieon : Initial creation
 * 2023-04-16. yieon : Authenticate error message handle
 * </PRE>
 */
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

    @SneakyThrows
    @Override
    public boolean hasError(ClientHttpResponse clientHttpResponse) {
        if (clientHttpResponse.getStatusCode() != HttpStatus.OK) {
            if (clientHttpResponse.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new InvalidPasswordException();
            } else {
                throw new PracticeAuthException(Objects.requireNonNull(clientHttpResponse.getHeaders().get("WWW-Authenticate")).toString());
            }
        } else {
            return false;
        }
    }

    @Override
    public void handleError(ClientHttpResponse clientHttpResponse) {

    }
}
