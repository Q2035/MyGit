package top.hellooooo.jobsubmission;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("top.hellooooo.jobsubmission.mapper")
@SpringBootApplication
public class JobSubmissionApplication {
    public static void main(String[] args) {
        SpringApplication.run(JobSubmissionApplication.class, args);
    }
}
