package dev.solar.demoinflearnrestapi.common;

import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

@TestConfiguration //테스트에서만 사용하는 설정임을 알려주는 애노테이션
public class RestDocsConfiguration {

    @Bean
    public RestDocsMockMvcConfigurationCustomizer restDocsMockMvcConfigurationCustomizer() {
        return configurer -> {
            configurer.operationPreprocessors()
                    .withRequestDefaults(prettyPrint())     //request 본문을 예쁘게 출력
                    .withResponseDefaults(prettyPrint());   //response 본문을 예쁘게 출력
        };
    }
}
