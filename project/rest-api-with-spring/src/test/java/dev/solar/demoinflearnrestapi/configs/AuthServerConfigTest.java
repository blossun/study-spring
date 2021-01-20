package dev.solar.demoinflearnrestapi.configs;

import dev.solar.demoinflearnrestapi.accounts.Account;
import dev.solar.demoinflearnrestapi.accounts.AccountRole;
import dev.solar.demoinflearnrestapi.accounts.AccountService;
import dev.solar.demoinflearnrestapi.common.BaseControllerTest;
import dev.solar.demoinflearnrestapi.common.TestDescription;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.header.Header;

import java.util.Set;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthServerConfigTest extends BaseControllerTest {

    @Autowired
    AccountService accountService;

    @Test
    @TestDescription("인증 토큰을 발급 받는 테스트")
    public void getAuthToken() throws Exception {
        // Given
        String username = "solar@email.com";
        String password = "solar";
        Account solar = Account.builder()
                .email(username)
                .password(password)
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();
        this.accountService.saveAccount(solar);

        String clientId = "myApp";
        String clientSecret = "pass";

        // When & Then
        this.mockMvc.perform(post("/oauth/token")
                    .with(httpBasic(clientId, clientSecret)) // Basic OAuth Header
                    .param("username", username)
                    .param("password", password)
                    .param("grant_type", "password"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists());
    }

}
