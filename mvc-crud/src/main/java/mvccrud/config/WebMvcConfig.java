package mvccrud.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvc Configuration
 *
 * Registers the JwtAuthFilter interceptor to protect all /api/** endpoints.
 * RestTemplate bean is in RestTemplateConfig (separate, to avoid circular
 * deps).
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final JwtAuthFilter jwtAuthFilter;

    public WebMvcConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    // ── Register the JWT filter for all /api/** endpoints ──
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtAuthFilter)
                .addPathPatterns("/api/**"); // protect all REST endpoints
    }
}
