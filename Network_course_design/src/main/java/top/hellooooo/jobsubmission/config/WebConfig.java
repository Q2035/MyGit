package top.hellooooo.jobsubmission.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.hellooooo.jobsubmission.interceptor.LoginInterceptor;

import java.util.Arrays;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/job/user/**")
                .addPathPatterns("/job/manager/**")
                .addPathPatterns("/job/manager/admin/**")
                .excludePathPatterns(Arrays.asList("/job/user/authentication", "/job/user/index", "/job/user/", "/job/user/login"));
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}
