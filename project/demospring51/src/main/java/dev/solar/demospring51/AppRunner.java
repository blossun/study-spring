package dev.solar.demospring51;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

@Component
public class AppRunner implements ApplicationRunner {

    @Value("#{1 +1}")
    int value;

    @Value("#{'hello ' + 'world'}") //문자열은 싱글쿼터(') 사용
    String greeting;

    @Value("#{1 eq 1}")
    boolean trueOrFalse;

    @Value("haha")
    String haha;

    @Value("${my.value}") //프로퍼티 사용
    int myValue;

    @Value("#{${my.value} eq 100}") //표현식 내에서 프로퍼티를 감싸서 가지고 있는 것은 가능
    boolean isMyValue100;

    @Value("#{'문자열 그대로'}")
    String text;

    @Value("#{sample.data}")
    int sampleData;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("================= ");
        System.out.println(value);
        System.out.println(greeting);
        System.out.println(trueOrFalse);
        System.out.println(haha);
        System.out.println(myValue);
        System.out.println(isMyValue100);
        System.out.println(text);
        System.out.println(sampleData);

        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression("2 + 100"); //표현식 정의
        Integer value = expression.getValue(Integer.class);//표현식으로 가져올 데이터 타입
        System.out.println(value);
    }
}
