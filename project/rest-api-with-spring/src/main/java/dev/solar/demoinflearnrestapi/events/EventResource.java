package dev.solar.demoinflearnrestapi.events;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

public class EventResource extends EntityModel<Event> {

    public EventResource(Event content, Link ... links) {
        super(content, links);
    }
}
