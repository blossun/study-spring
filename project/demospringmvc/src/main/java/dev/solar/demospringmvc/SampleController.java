package dev.solar.demospringmvc;

import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
public class SampleController {

    @GetMapping("/hello")
    public EntityModel<Hello> hello() {
        Hello hello = new Hello();
        hello.setPrefix("Hey,");
        hello.setName("solar");

        EntityModel<Hello> helloEntityModel = EntityModel.of(hello,
                linkTo(SampleController.class).slash(hello).withSelfRel());

        return helloEntityModel;
    }

}
