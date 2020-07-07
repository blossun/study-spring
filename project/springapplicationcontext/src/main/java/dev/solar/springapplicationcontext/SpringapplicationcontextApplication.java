package dev.solar.springapplicationcontext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;

public class SpringapplicationcontextApplication {
    private static final Logger log = LoggerFactory.getLogger(SpringapplicationcontextApplication.class);

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        log.debug("생성된 빈의 이름 : {}", Arrays.toString(beanDefinitionNames));
        BookService bookService = (BookService) context.getBean("bookService");//Type cast
        log.debug("의존성 주입이 되었는지 확인 : {}",bookService.bookRepository != null);//null이 아닌지 확인 -> true : 빈주입 성공
    }

}
