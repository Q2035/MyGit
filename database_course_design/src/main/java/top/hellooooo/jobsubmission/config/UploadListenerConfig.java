package top.hellooooo.jobsubmission.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ScopeMetadata;
import org.springframework.web.multipart.MultipartResolver;

/**
 * @Author Q
 * @Date 13/09/2020 15:56
 * @Description
 */
@Configuration
public class UploadListenerConfig {
    @Bean(name = "multipartResolver")
    public MultipartResolver multipartResolver(){
        CustomMultipartResolver customMultipartResolver = new CustomMultipartResolver();
        return customMultipartResolver;
    }
}
