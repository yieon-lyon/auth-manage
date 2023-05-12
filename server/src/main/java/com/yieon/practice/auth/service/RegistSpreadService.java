package com.yieon.practice.auth.service;

import com.yieon.practice.auth.lib.exception.RegisterSpreadException;
import com.yieon.practice.auth.lib.model.constant.account.UserClient;
import com.yieon.practice.auth.lib.model.domain.account.ClientInfo;
import com.yieon.practice.auth.lib.repository.account.ClientInfoRepository;
import com.yieon.practice.auth.lib.repository.account.UserRepository;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.List;

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2023-04-06
 * <PRE>
 * ------------------------
 * summary : 등록 전파 서비스
 * ------------------------
 * Revision history
 * 2023-04-06. yieon : Initial creation
 * </PRE>
 */
@Deprecated
@Service
public class RegistSpreadService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClientInfoRepository clientInfoRepository;

    public void attemptRegisterSpread(User client, com.yieon.practice.auth.lib.model.domain.account.User user) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
        body.add("username", user.getEmail());
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();

        List<ClientInfo> userClients = clientInfoRepository.findAllByUser(user);
        userClients.forEach(clientInfo -> {
            if (client.getUsername().equals(UserClient.CLIENT_AUTH.value+"-api")) return;
        });
        user.setLstLoginDt(Instant.now());
        userRepository.save(user);
    }
}
