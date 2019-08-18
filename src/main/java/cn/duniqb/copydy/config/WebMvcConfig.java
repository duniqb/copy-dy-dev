package cn.duniqb.copydy.config;

import cn.duniqb.copydy.controller.interceptor.Interceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources/")
                .addResourceLocations("file:E:/upload/");
    }

    /**
     * 注册拦截器到容器
     *
     * @return
     */
    @Bean
    public Interceptor interceptor() {
        return new Interceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor()).addPathPatterns("/user/**").addPathPatterns("/bgm/**").addPathPatterns("/video/**");
        super.addInterceptors(registry);
    }
}
