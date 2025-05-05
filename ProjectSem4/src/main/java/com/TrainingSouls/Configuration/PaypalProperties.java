package com.TrainingSouls.Configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "paypal")
public class PaypalProperties {
    private String clientId;
    private String secretId;
}
