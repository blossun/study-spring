package dev.solar.demoinflearnrestapi.configs;

import dev.solar.demoinflearnrestapi.accounts.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * OAuth2 인증서버 설정
 */
@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager; // 유저인증 정보를 가지고 있음

    @Autowired
    AccountService accountService;

    @Autowired
    TokenStore tokenStore;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.passwordEncoder(passwordEncoder);  //client_secret를 확인할 때 사용 client_secret도 전부 Password를 Encoding해서 관리
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory() // 학습을 위해 inMemory 용으로 생성함. DB로 관리하는 것이 이상적
                .withClient("myApp") // myApp에 대한 클라이언트를 하나 생성
                .authorizedGrantTypes("password", "refresh_token") // 지원하는 grant_Type
                .scopes("read", "write") // 앱에서 정의하는 값에 따라
                .secret(this.passwordEncoder.encode("pass"))
                .accessTokenValiditySeconds(10 * 60) // 엑세스 토큰의 유효시간(초)
                .refreshTokenValiditySeconds(6 * 10 * 60); // refresh_token의 유효시간(초)
    }

    // AuthenticationManager, TokenStore, UserDetailsService를 설정할 수 있음
    // 유저정보를 확인을 해야 토큰을 발급 받을 수 있음
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager) //우리 유저정보를 알고 있는 authenticationManager로 설정
                .userDetailsService(accountService) // UserDetailsService 설정
                .tokenStore(tokenStore); // TokenStore 설정
    }
}
