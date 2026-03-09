package mvccrud.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS Configuration
 *
 * Allows the React dev server (localhost:5173 via Vite)
 * to make API calls to this Spring backend (localhost:8082).
 *
 * In production, replace the origin with your deployed frontend URL.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // only expose REST endpoints
                .allowedOrigins(
                        "http://localhost:5173", // Vite dev server
                        "http://localhost:5174",
                        "http://localhost:5175",
                        "http://localhost:3000" // CRA dev server (fallback)
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*") // includes Authorization header
                .exposedHeaders("Authorization")
                .allowCredentials(true); // required for Authorization header
    }
}
