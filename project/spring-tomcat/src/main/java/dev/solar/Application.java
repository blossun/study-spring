package dev.solar;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class Application {

    public static void main(String[] args) throws LifecycleException {
        // 1. 톰캣 객체 생성
        Tomcat tomcat = new Tomcat();
        // 2. 포트 설정
        tomcat.setPort(8080);

        // 3. 톰캣에 컨텍스트 추가
        Context context = tomcat.addContext("/", "/");

        // 4. 서블릿 생성
        HttpServlet servlet = new HttpServlet() {
            // doGet : get 요청에 대한 서블릿
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
                PrintWriter writer = resp.getWriter();
                writer.println("<html><head><title>");
                writer.println("Hey, Tomcat");
                writer.println("</title></head>");
                writer.println("<body><h1>Hello Tomcat</h1></body>");
                writer.println("</html>");
            }
        };

        // 5. 톰캣에 서블릿 등록
        String servletName = "helloServlet";
        tomcat.addServlet("/", servletName, servlet); // servlet을 servletName명으로 context에 추가

        // 6. 컨텍스트에 서블릿 맵핑
        context.addServletMappingDecoded("/hello", servletName);

        tomcat.start();
        tomcat.getServer().await(); // await()하면 요청을 기다리는 상태로 끝나지 않고 대기하게 된다.
    }
}
