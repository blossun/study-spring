package dev.solar.demospringmvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SampleController {

    @GetMapping("/hello")
    public String hello(Model model) { //Model : 전달할 데이터 (Map)
        model.addAttribute("name", "solar");
        return "hello"; //본문의 hello가 아님(@RestController가 아니라 @Controller이기 때문에)
    }
}
