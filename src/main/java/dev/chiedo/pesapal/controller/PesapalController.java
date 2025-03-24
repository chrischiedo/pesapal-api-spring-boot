package dev.chiedo.pesapal.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.chiedo.pesapal.model.IPNRequest;
import dev.chiedo.pesapal.model.OrderRequest;
import dev.chiedo.pesapal.service.PesapalService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
@RequestMapping("/api")
public class PesapalController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PesapalController.class);

    private final PesapalService pesapalService;

    ObjectMapper mapper = new ObjectMapper();

    // constructor injection
    public PesapalController(PesapalService pesapalService) {
        this.pesapalService = pesapalService;
    }

    @PostMapping("/submit-order")
    public ResponseEntity<?> submitOrder(@Valid  @RequestBody OrderRequest orderRequestDto,
                                         HttpServletRequest request) throws JsonProcessingException {

        LOGGER.info("Submitting order with payload: {}", mapper.writeValueAsString(orderRequestDto));

        try {
            Map<String, Object> orderRequest = new HashMap<>();
            orderRequest.put("id", orderRequestDto.getId());
            orderRequest.put("currency", orderRequestDto.getCurrency());
            orderRequest.put("amount", orderRequestDto.getAmount());
            orderRequest.put("description", orderRequestDto.getDescription());
            orderRequest.put("callback_url", orderRequestDto.getCallbackUrl());
            orderRequest.put("notification_id", orderRequestDto.getNotificationId());
            orderRequest.put("billing_address", orderRequestDto.getBillingAddress());

            // Get the token from the request attribute (set by the interceptor)
            String accessToken = (String) request.getAttribute("accessToken");

            Map<String, Object> orderResponse = pesapalService.submitOrder(orderRequest, accessToken);

            return new ResponseEntity<>(orderResponse, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());

            LOGGER.info("There was a server error: {}", e.getMessage());

            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/register-ipn")
    public ResponseEntity<?> registerIPN(@Valid @RequestBody IPNRequest ipnRequest,
                                         HttpServletRequest request) throws JsonProcessingException {

        LOGGER.info("Registering IPN with payload: {}", mapper.writeValueAsString(ipnRequest));

        try {
            Map<String, Object> ipnRequestMap = new HashMap<>();
            ipnRequestMap.put("url", ipnRequest.getUrl());
            ipnRequestMap.put("ipn_notification_type", ipnRequest.getIPNNotificationType());

            String accessToken = (String) request.getAttribute("accessToken");

            Map<String, Object> ipnResponse = pesapalService.registerIPN(ipnRequestMap, accessToken);

            LOGGER.info("Registration of IPN was successful");

            return new ResponseEntity<>(ipnResponse, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());

            LOGGER.info("Server error occurred: {}", e.getMessage());

            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/transaction-status")
    public ResponseEntity<?> getTransactionStatus(@RequestParam String orderTrackingId,
                                                  HttpServletRequest request) {
        try {
            String accessToken = (String) request.getAttribute("accessToken");

            Map<String, Object> statusResponse = pesapalService.getTransactionStatus(orderTrackingId, accessToken);

            LOGGER.info("Request processed successfully");

            return new ResponseEntity<>(statusResponse, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());

             LOGGER.info("The server has thrown an error: {}", e.getMessage());

            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
