package dev.solar.springtestdemo.sample;

import org.springframework.stereotype.Service;

@Service
public class SampleService {

    public String getName() {
        return "Holariri"; //devtools 모듈 추가 - 코드 변경 후, 빌드하면 자동으로 재시작되는 것 확인 가능
    }
}
