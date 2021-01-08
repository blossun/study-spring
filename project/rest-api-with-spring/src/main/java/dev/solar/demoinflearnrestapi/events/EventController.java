package dev.solar.demoinflearnrestapi.events;

import org.modelmapper.ModelMapper;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

    private final EventRepository eventRepository;

    private final ModelMapper modelmapper;

    public EventController(EventRepository eventRepository, ModelMapper modelmapper) {
        this.eventRepository = eventRepository;
        this.modelmapper = modelmapper;
    }

    @PostMapping()
    public ResponseEntity createEvent(@RequestBody EventDto eventDto) {
        Event event = modelmapper.map(eventDto, Event.class);
        Event newEvent = this.eventRepository.save(event);
        URI createdUri = linkTo(EventController.class).slash(newEvent.getId()).toUri(); // DB에 저장된 ID 값
        return ResponseEntity.created(createdUri).body(newEvent); //저장된 Event 정보 반환
    }
}
