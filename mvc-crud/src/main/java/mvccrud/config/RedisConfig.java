package mvccrud.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * Redis Cache Configuration
 *
 * Sets up:
 * - Cache TTL (Time-To-Live) of 5 minutes
 * - JSON serialization so cached values are human-readable
 * - Named caches with individual TTLs
 */
@Configuration
@EnableCaching // ← activates @Cacheable / @CacheEvict / @CachePut throughout the app
public class RedisConfig {

        @Bean
        public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {

                // ── Default config: 5-minute TTL, JSON values, String keys ──
                RedisCacheConfiguration defaultConfig = RedisCacheConfiguration
                                .defaultCacheConfig()
                                .entryTtl(Duration.ofMinutes(5))
                                .serializeKeysWith(
                                                RedisSerializationContext.SerializationPair
                                                                .fromSerializer(new StringRedisSerializer()))
                                .serializeValuesWith(
                                                RedisSerializationContext.SerializationPair
                                                                .fromSerializer(new GenericJackson2JsonRedisSerializer()))
                                .disableCachingNullValues();

                return RedisCacheManager.builder(connectionFactory)
                                .cacheDefaults(defaultConfig)
                                .withCacheConfiguration("students",
                                                defaultConfig.entryTtl(Duration.ofMinutes(5))) // student list: 5 min
                                .withCacheConfiguration("student",
                                                defaultConfig.entryTtl(Duration.ofMinutes(10))) // single student: 10
                                                                                                // min
                                .withCacheConfiguration("courses",
                                                defaultConfig.entryTtl(Duration.ofMinutes(10))) // course list: 10 min
                                .withCacheConfiguration("course",
                                                defaultConfig.entryTtl(Duration.ofMinutes(15))) // single course: 15 min
                                .build();
        }
}
