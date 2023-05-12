//package server;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import java.util.Map;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpHeaders;
//import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
///**
// * Oauth2 client IT given account resource server
// *
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//public class OAuth2ClientIT {
//
//    @Autowired
//    protected MockMvc mockMvc;
//
////    @Autowired
////    protected ObjectMapper objectMapper;
//
//    @Test
//    public void runOauthClient() throws Exception {
//        /**
//         * 1) /accounts/me with anonymous user
//         * Try to access "/accounts/me" without access token.
//         * Then receive unauthorized response
//         */
////        mockMvc.perform(get("/accounts/me"))
////               .andDo(print())
////               .andExpect(status().isUnauthorized());
//
//        /**
//         * 2) getting access token
//         *
//         * Try to get access token with BasicAuth.
//         *
//         * * Header
//         *  - Key : "Authentication", Value: Base64Enc("application:pass")
//         *
//         * * Body with form-data
//         *  - username=user@gmail.com
//         *  - password=user
//         *  - grant_type=password
//         */
//        final String username = "admin@test.com";
//        final String password = "admin";
//        final String clientId = "auth";
//        final String clientSecret = "auth1004";
//
//        ResultActions perform = mockMvc.perform(post("/oauth/token")
//                                                        .with(httpBasic(clientId, clientSecret))
//                                                        .param("username", username)
//                                                        .param("password", password)
//                                                        .param("grant_type", "password"));
//
//        String response = perform.andReturn().getResponse().getContentAsString();
//        System.out.println("/oauth/token response : " + response);
//
//        Map<String, Object> results = new Jackson2JsonParser().parseMap(response);
//        String accessToken = results.get("access_token").toString();
//        String refreshToken = results.get("refresh_token").toString();
//        String bearerToken = "Bearer " + accessToken;
//
//        /**
//         * 3) request with auth token
//         * Try to get resource with access token (Bearer)
//         * * Header
//         *  - Key : "Authentication", Value: Bearer <access-token>
//         */
//        mockMvc.perform(get("/accounts/me")
//                                .header(HttpHeaders.AUTHORIZATION, bearerToken))
//               .andDo(print())
//               .andExpect(jsonPath("id").exists())
//               .andExpect(jsonPath("email").value(username))
//               .andExpect(jsonPath("age").exists());
//
//        /**
//         * 4) refresh token
//         * Try to get a new access token with refresh token
//         *
//         * * Header
//         *  - Key : "Authentication", Value: Base64Enc("application:pass")
//         *
//         * * Body with form-data
//         * - grant_type=refresh_token
//         * - refresh_token=<refresh-token>
//         */
//        perform = mockMvc.perform(post("/oauth/token")
//                                          .with(httpBasic(clientId, clientSecret))
//                                          .param("grant_type", "refresh_token")
//                                          .param("refresh_token", refreshToken));
//
//        response = perform.andReturn().getResponse().getContentAsString();
//        results = new Jackson2JsonParser().parseMap(response);
//        String updatedAccessToken = results.get("access_token").toString();
//        String updatedBearerToken = "Bearer " + updatedAccessToken;
//
//        assertThat(accessToken).isNotEqualTo(updatedAccessToken);
//
//        // failed to request if access with prev token.
//        mockMvc.perform(get("/accounts/me")
//                                .header(HttpHeaders.AUTHORIZATION, bearerToken))
//               .andExpect(status().isUnauthorized());
//
//        // success to request if access with new token
//        mockMvc.perform(get("/accounts/me")
//                                .header(HttpHeaders.AUTHORIZATION, updatedBearerToken))
//               .andDo(print())
//               .andExpect(jsonPath("id").exists())
//               .andExpect(jsonPath("email").value(username))
//               .andExpect(jsonPath("age").exists());
//
//    }
//}
