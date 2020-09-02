package dev.solar;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

public class Application {

    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);

        Context context = tomcat.addContext("/", "/");

        tomcat.start();
//        tomcat.getServer().await(); // await()하면 요청을 기다리는 상태로 끝나지 않고 대기하게 된다.
    }
}
