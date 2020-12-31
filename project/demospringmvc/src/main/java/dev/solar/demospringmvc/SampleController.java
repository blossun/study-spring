package dev.solar.demospringmvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SampleController {

    @GetMapping("/hello")
    public String hello() {
        throw new SampleException();
    }

    @ExceptionHandler(SampleException.class) //이 SampleController안에서 SampleException이 발생하면 이 핸들러를 사용하겠다.
    public @ResponseBody AppError sampleError(SampleException e) {
        AppError appError = new AppError(); //Exception 정보를 기반으로 AppError 정보를 채워주면 된다.
        appError.setMessage("error.app.key");
        appError.setReason("IDK IDK IDK");
        return appError;
    }

}
