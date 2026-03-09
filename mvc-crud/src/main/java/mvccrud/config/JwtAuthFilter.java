package mvccrud.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT Authentication Filter (HandlerInterceptor)
 *
 * Intercepts every /api/** request and validates the JWT token
 * by calling the auth-service's /auth/validate endpoint.
 *
 * Flow:
 * 1. Extract "Authorization: Bearer <token>" header
 * 2. Call GET auth-service:8083/auth/validate with the token
 * 3. If valid → allow the request to proceed
 * 4. If invalid/missing → return 401 Unauthorized
 *
 * This is the MICROSERVICE pattern: mvc-crud doesn't know HOW
 * to validate a JWT — it delegates that to auth-service.
 */
@Component
public class JwtAuthFilter implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

    private final RestTemplate restTemplate;
    private final String authServiceUrl;

    public JwtAuthFilter(
            RestTemplate restTemplate,
            @Value("${auth-service.url}") String authServiceUrl) {
        this.restTemplate = restTemplate;
        this.authServiceUrl = authServiceUrl;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        // ── Skip preflight (OPTIONS) requests ──
        // Browsers send OPTIONS before the real request when CORS is involved
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // ── 1. Extract the Authorization header ──
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Missing or invalid Authorization header for {} {}",
                    request.getMethod(), request.getRequestURI());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Authentication required. Please log in.\"}");
            return false; // block the request
        }

        // ── 2. Call auth-service to validate the token ──
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authHeader); // forward the same header

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<String> authResponse = restTemplate.exchange(
                    authServiceUrl + "/auth/validate",
                    HttpMethod.GET,
                    entity,
                    String.class);

            if (authResponse.getStatusCode() == HttpStatus.OK) {
                log.debug("Token validated successfully for {} {}",
                        request.getMethod(), request.getRequestURI());
                return true; // allow the request
            }

        } catch (Exception e) {
            log.warn("Token validation failed for {} {}: {}",
                    request.getMethod(), request.getRequestURI(), e.getMessage());
        }

        // ── 3. Validation failed → 401 ──
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"Invalid or expired token. Please log in again.\"}");
        return false;
    }
}
