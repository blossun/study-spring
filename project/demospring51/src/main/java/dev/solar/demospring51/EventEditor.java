package dev.solar.demospring51;

import java.beans.PropertyEditorSupport;

public class EventEditor extends PropertyEditorSupport {

    @Override
    public String getAsText() {
        //PropertyEditor가 받은 객체를 getValue()로 가져올 수 있다. (반환타입은 Object) Event로 형변환 필요
        Event event = (Event)getValue();
        return event.getId().toString();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(new Event(Integer.parseInt(text))); //text를 Integer로 변환 후 Event 객체의 id값으로 해서 Event 객체 생성
    }
}
