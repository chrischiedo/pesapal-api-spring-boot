package dev.chiedo.pesapal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;


@Getter
@Setter
public class OrderRequest {

    @NotBlank
    @JsonProperty("id")
    private String id;

    @NotBlank
    @JsonProperty("currency")
    private String currency;

    @NotNull
    @JsonProperty("amount")
    private BigDecimal amount;

    @NotBlank
    @JsonProperty("description")
    private String description;

    @NotBlank
    @JsonProperty("callback_url")
    private String callbackUrl;

    @NotNull
    @JsonProperty("notification_id")
    private UUID notificationId;

    @NotNull
    @JsonProperty("billing_address")
    private BillingAddress billingAddress;
}
