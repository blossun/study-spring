package dev.solar.demospring51;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Arrays;

@Component
public class AppRunner implements ApplicationRunner {

    @Autowired
    Validator validator;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("validator : " + validator.getClass()); //어떠한 validator가 주입되는지 확인

        Event event = new Event();
        event.setLimit(-1); // 0이상 이어야 error 안 남
        event.setEmail("hahaha"); // email 형식에 맞지 않는 문자열

        Errors errors = new BeanPropertyBindingResult(event, "event");

        // 검증
        validator.validate(event, errors);

        // error가 있는지 확인
        System.out.println(errors.hasErrors());
        // 모든 에러를 가져와서 에러를 순차적으로 순회하면서 에러코드를 출력
        errors.getAllErrors().forEach(e -> {
            System.out.println("===== error code =====");
            Arrays.stream(e.getCodes()).forEach(System.out::println);
            System.out.println(e.getDefaultMessage());
        });
    }
}
