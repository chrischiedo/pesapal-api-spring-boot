package dev.chiedo.pesapal.interceptor;

import dev.chiedo.pesapal.helper.AccessTokenHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;


/**
 * Interceptor class that automatically handles API authentication.
 * This component intercepts incoming requests and enriches them with a valid access token
 * before they reach the handler methods, ensuring all API calls are properly authenticated.
 */
@Component
public class AccessTokenInterceptor implements HandlerInterceptor {

    private final AccessTokenHelper accessTokenHelper;

    @Value("${pesapal.consumer-key}")
    private String consumerKey;

    @Value("${pesapal.consumer-secret}")
    private String consumerSecret;

    // constructor injection
    public AccessTokenInterceptor(AccessTokenHelper accessTokenHelper) {
        this.accessTokenHelper = accessTokenHelper;
    }

    /**
     * Pre-processes each request by obtaining an access token and attaching it to the request.
     * If token generation fails, the request is rejected with an error response.
     *
     * @param request The incoming HTTP request
     * @return true if the request should proceed to the handler, false otherwise
     * @throws Exception if an error occurs during token generation or response writing
     */

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        try {
            Map<String, String> tokenData = accessTokenHelper.getAccessToken(consumerKey, consumerSecret);
            request.setAttribute("accessToken", tokenData.get("token"));
            return true;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Error generating access token\"}");
            return false;
        }
    }
}
