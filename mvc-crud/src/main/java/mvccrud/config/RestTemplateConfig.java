package mvccrud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Provides the RestTemplate bean.
 *
 * Separated from WebMvcConfig to avoid a circular dependency:
 * JwtAuthFilter needs RestTemplate → WebMvcConfig needs JwtAuthFilter → LOOP
 *
 * By putting RestTemplate here, Spring can create it independently
 * before either JwtAuthFilter or WebMvcConfig.
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
