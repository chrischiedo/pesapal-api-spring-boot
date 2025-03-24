package dev.chiedo.pesapal.config;

import dev.chiedo.pesapal.interceptor.AccessTokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * Configuration class that sets up a web component.
 * This configuration registers the access token interceptor
 * for the application.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AccessTokenInterceptor accessTokenInterceptor;

    /**
     * Registers the AccessTokenInterceptor to intercept requests to all paths
     * under the /api/ endpoint, ensuring they have proper authentication.
     *
     * @param registry The InterceptorRegistry used to register interceptors
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accessTokenInterceptor)
                .addPathPatterns("/api/**");
    }
}
