package top.hellooooo.jobsubmission;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan("top.hellooooo.jobsubmission.mapper")
@SpringBootApplication(exclude = MultipartAutoConfiguration.class)
@EnableScheduling
public class JobSubmissionApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobSubmissionApplication.class, args);
    }
}
