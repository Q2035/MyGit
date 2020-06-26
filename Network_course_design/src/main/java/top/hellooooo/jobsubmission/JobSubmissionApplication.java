package main.java.top.hellooooo.jobsubmission;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import top.hellooooo.jobsubmission.util.ExecutorUtil;

@MapperScan("top.hellooooo.jobsubmission.mapper")
@SpringBootApplication
@EnableScheduling
public class JobSubmissionApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobSubmissionApplication.class, args);
    }
}
