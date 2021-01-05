package dev.solar.springbootsecurity2;

import dev.solar.springbootsecurity2.account.Account;
import dev.solar.springbootsecurity2.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AccountRunner implements ApplicationRunner {

    @Autowired
    AccountService accountService;

    @Override
    public void run(ApplicationArguments args) {
        Account solar = accountService.createAccount("solar", "pass");
        System.out.println(solar.getUsername() + " password: " + solar.getPassword());
    }
}
