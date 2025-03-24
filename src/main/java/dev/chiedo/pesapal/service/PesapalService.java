package dev.chiedo.pesapal.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;


@Service
public class PesapalService {

    private final RestClient restClient;

    public PesapalService() {
        String baseUrl = "https://cybqa.pesapal.com/pesapalv3"; // use "https://pay.pesapal.com/v3" for production
        restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public Map<String, Object> submitOrder(Map<String, Object> request, String accessToken) {
        String url = "/api/Transactions/SubmitOrderRequest";

        return restClient.post()
                .uri(url)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .body(request)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public Map<String, Object> registerIPN(Map<String, Object> ipnRequest, String accessToken) {
        String url = "/api/URLSetup/RegisterIPN";

        return restClient.post()
                .uri(url)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .body(ipnRequest)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public Map<String, Object> getTransactionStatus(String orderTrackingId, String accessToken) {
        String url = "/api/Transactions/GetTransactionStatus";

        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(url)
                        .queryParam("orderTrackingId", orderTrackingId)
                        .build())
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
