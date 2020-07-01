package top.hellooooo.netjobsubmission;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan("top.hellooooo.netjobsubmission.mapper")
@SpringBootApplication
@EnableScheduling
public class NetJobSubmissionApplication {

    public static void main(String[] args) {
        SpringApplication.run(NetJobSubmissionApplication.class, args);
    }
}
