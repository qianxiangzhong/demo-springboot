package com.qian.demo.entity;

import com.qian.demo.interceptor.ApiInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * SpringBoot配置类
 * @author qianxiangzhong
 */
@SpringBootConfiguration
public class MyConfiguration implements WebMvcConfigurer
    // WebMvcConfigurationSupport会启用@EnableWebMvc，导致自定义的所有mvc配置失效（例如String-to-Date、编码等配置）
    // 所以这里使用 WebMvcConfigurer
//        extends WebMvcConfigurationSupport
{
    public static final Logger logger = LoggerFactory.getLogger(MyConfiguration.class);

    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.url}")
    private String url;

    /**
     * 方法使用@Bean注解，内部返回Bean对象可作为全局唯一Bean对象，其他地方可直接使用@Autowired来注入
     * @return
     */
    @Bean
    public LearningRecord getLearningRecord() {
        logger.info("username:{}, url:{}", username, url);
        LearningRecord learningRecord = new LearningRecord();
        learningRecord.setPersonId(1);
        learningRecord.setSchoolId(2);
        return learningRecord;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // addPathPatterns添加拦截路径，excludePathPatterns排除拦截的路径,多个路径中间用逗号隔开
        registry.addInterceptor(new ApiInterceptor()).addPathPatterns("/learning/*").excludePathPatterns("/learning/saveLearningRecord");
    }
}
