package dev.solar.demospring51;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component @Scope(value = "prototype")
public class Proto {

    @Autowired
    Single single;
}
