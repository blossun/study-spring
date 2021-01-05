package dev.solar.springbootsecurity2.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

@Service
public class AccountService implements UserDetailsService { // UserDetailsServie 인터페이스를 구현

    @Autowired
    private AccountRepository accountRepository;

    public Account createAccount(String username, String password) {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        return accountRepository.save(account);
    }

//    로그인 처리 시 loadUserByUsername()가 호출된다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        로그인 페이지에서 입력받은 username이 매개변수로 들어온다. 넘겨받은 username으로 DB의 실제 유저정보를 확인
//        DB 유저정보 내의 패스워드와 입력받은 패스워드를 확인해서 같으면 로그인 처리, 다르면 에러를 발생시킨다.
        Optional<Account> byUsername = accountRepository.findByUsername(username);
        Account account = byUsername.orElseThrow(() -> new UsernameNotFoundException(username));
//        반환값은 `UserDetails` 인터페이스 구현체를 리턴해야 한다. (우리가 가진 유저 정보를 가지고 변환해서 리턴)
        return new User(account.getUsername(), account.getPassword(), authorities());
    }

    private Collection<? extends GrantedAuthority> authorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")); //~한 권한을 가진 유저라는 것을 셋팅
    }
}
