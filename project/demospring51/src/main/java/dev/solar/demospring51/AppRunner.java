package dev.solar.demospring51;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.Arrays;

@Component
public class AppRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Event event = new Event();
        EventValidator eventValidator = new EventValidator();

        //Errors 필요
        Errors errors = new BeanPropertyBindingResult(event, "event");

        // 검증
        eventValidator.validate(event, errors);

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
