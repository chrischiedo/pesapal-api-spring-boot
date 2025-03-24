package dev.chiedo.pesapal.helper;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;


/**
 * Helper class for managing access token operations with the Pesapal API.
 * This component handles authentication requests to the API.
 *
 * <p>By default, it connects to the Pesapal Sandbox/Demo environment. For production use,
 * the base URL should be changed to "https://pay.pesapal.com/v3".</p>
 */
@Component
public class AccessTokenHelper {

    private final RestClient restClient;

    public AccessTokenHelper() {
        String baseUrl = "https://cybqa.pesapal.com/pesapalv3"; // use "https://pay.pesapal.com/v3" for production
        restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public Map<String, String> getAccessToken(String consumerKey, String consumerSecret) {
        String url = "/api/Auth/RequestToken";

        String requestBody = String.format("{\"consumer_key\":\"%s\",\"consumer_secret\":\"%s\"}",
                consumerKey, consumerSecret);

        return restClient.post()
                .uri(url)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
