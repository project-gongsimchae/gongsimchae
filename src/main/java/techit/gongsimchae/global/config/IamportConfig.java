package techit.gongsimchae.global.config;

import com.siot.IamportRestClient.IamportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IamportConfig {

    @Value("${iamport.api.key}")
    private String apikey;

    @Value("${iamport.api.secret}")
    private String apiSecret;

    @Bean
    public IamportClient iamportClient(){
        return new IamportClient(apikey,apiSecret);
    }
}
