package com.fengling.config;

import com.fengling.interceptor.AuthInterceptor;
import com.fengling.interceptor.OptionalAuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;
    private final OptionalAuthInterceptor optionalAuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns(
                        "/api/shelf/**",
                        "/api/user/logout",
                        "/api/user/mine",
                        "/api/user/updateinfo",
                        "/api/user/uploadphoto",
                        "/api/author/**",
                        "/api/admin/**"
                )
                .excludePathPatterns(
                        "/api/user/login",
                        "/api/user/register",
                        "/api/author/register"
                );
        registry.addInterceptor(optionalAuthInterceptor)
                .addPathPatterns("/api/novel/*/chapter/*");
    }
}
