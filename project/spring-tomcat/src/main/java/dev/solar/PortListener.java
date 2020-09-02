package dev.solar;

import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class PortListener implements ApplicationListener<ServletWebServerInitializedEvent> { //웹서버가 생성이되면 이 이벤트 리스터가 호출된다.
    @Override
    public void onApplicationEvent(ServletWebServerInitializedEvent servletWebServerInitializedEvent) {
        //포트 정보를 알아내는 방법
        ServletWebServerApplicationContext applicationContext = servletWebServerInitializedEvent.getApplicationContext();
        //서블릿 웹 서버 applicationContext이기 때문에 webServer 정보를 알 수 있고, webServer를 통해서 port 정보를 알 수 있다.
        System.out.println(applicationContext.getWebServer().getPort());
    }
}
