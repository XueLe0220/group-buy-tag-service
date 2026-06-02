package cn.xuele.tag.infrastructure.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Redis connection properties for tag-service.
 */
@Data
@ConfigurationProperties(prefix = "redis.sdk.config", ignoreInvalidFields = true)
public class RedisClientConfigProperties {

    private String mode = "standalone";
    private Standalone standalone = new Standalone();
    private Cluster cluster = new Cluster();

    private String password;
    private int poolSize = 64;
    private int minIdleSize = 10;
    private int idleTimeout = 10000;
    private int connectTimeout = 10000;
    private int retryAttempts = 3;
    private int retryInterval = 1000;
    private int pingInterval = 0;
    private boolean keepAlive = true;

    @Data
    public static class Standalone {
        private String host;
        private int port;
    }

    @Data
    public static class Cluster {
        private List<String> nodes = new ArrayList<>();
    }
}