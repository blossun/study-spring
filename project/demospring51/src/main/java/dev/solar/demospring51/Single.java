package dev.solar.demospring51;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class Single {

    @Autowired
    private ObjectProvider<Proto> proto;

    @Autowired
    private ApplicationContext applicationContext;

    public Proto getProto() {
        return proto.getIfAvailable();
    }
}
