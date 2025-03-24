package dev.chiedo.pesapal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class IPNRequest {

    @NotBlank
    @JsonProperty("url")
    private String url;

    @NotBlank
    @JsonProperty("ipn_notification_type")
    private String IPNNotificationType;
}
