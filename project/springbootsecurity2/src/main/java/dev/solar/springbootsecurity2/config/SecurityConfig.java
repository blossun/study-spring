package dev.solar.springbootsecurity2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/hello").permitAll() // '/'와 '/hello'는 모든 사용자에게 접근 허용
                .anyRequest().authenticated() // 그 외의 나머지 모든 요청은 인증이 필요
                .and()
                .formLogin() // 폼 로그인 사용
                .and()
                .httpBasic(); // Basic 인증도 사용
    }
}
