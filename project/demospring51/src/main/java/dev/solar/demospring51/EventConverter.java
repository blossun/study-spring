package dev.solar.demospring51;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

public class EventConverter {

    //String -> Event 타입변환
//    @Component
    public static class StringToEventConverter implements Converter<String, Event> {
        @Override
        public Event convert(String source) {
            return new Event(Integer.parseInt(source));
        }
    }

    //Evnet -> String 타입변환
//    @Component
    public static class EventToStringConverter implements Converter<Event, String> {
        @Override
        public String convert(Event source) {
            return source.getId().toString();
        }
    }
}
