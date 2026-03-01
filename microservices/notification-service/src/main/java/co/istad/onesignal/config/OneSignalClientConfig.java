package co.istad.onesignal.config;

import com.onesignal.client.ApiClient;
import com.onesignal.client.api.DefaultApi;
import com.onesignal.client.auth.HttpBearerAuth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OneSignalClientConfig {

    @Value("${onesignal.rest-api-key}")
    private String REST_API_KEY;

    @Bean
    public DefaultApi defaultApi(){
        ApiClient defaultClient = com.onesignal.client.Configuration.getDefaultApiClient();
        HttpBearerAuth restApiAuth = (HttpBearerAuth) defaultClient.getAuthentication("rest_api_key");
        restApiAuth.setBearerToken(REST_API_KEY);
        return new DefaultApi(defaultClient);
    }
}
