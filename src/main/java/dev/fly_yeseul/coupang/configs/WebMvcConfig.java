package dev.fly_yeseul.coupang.configs;

import dev.fly_yeseul.coupang.interceptors.SessionInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


// 프로젝트하면 이거 먼저 설정하는것이 좋다.
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public SessionInterceptor sessionInterceptor(){
        return new SessionInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 메서드 체인
        // addInterceptor => InterceptorRegistration
        // 같은 객체에 대해서 계속적으로 호출할 수 있다.
        // 참고만 해도 된다.
        // 편하게 코드 짤 수 있다.
        registry.addInterceptor(this.sessionInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/resources/**");

    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // /user/login      (0)
        // /user/login/     (X)
        configurer.setUseTrailingSlashMatch(false);
    }
}
