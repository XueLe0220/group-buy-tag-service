package cn.xuele.tag.infrastructure.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static org.springframework.util.StringUtils.hasText;

/**
 * Redisson client configuration owned by tag-service infrastructure.
 */
@Configuration
@EnableConfigurationProperties(RedisClientConfigProperties.class)
public class RedisClientConfig {

    @Bean("redissonClient")
    public RedissonClient redissonClient(RedisClientConfigProperties properties) {
        Config config = new Config();

        if ("cluster".equalsIgnoreCase(properties.getMode())) {
              ClusterServersConfig clusterServersConfig = config.useClusterServers()
                    .addNodeAddress(toRedisAddresses(properties.getCluster().getNodes()))
                    .setConnectTimeout(properties.getConnectTimeout())
                    .setRetryAttempts(properties.getRetryAttempts())
                    .setRetryInterval(properties.getRetryInterval());

            if (hasText(properties.getPassword())) {
                clusterServersConfig.setPassword(properties.getPassword());
            }
        } else {
            SingleServerConfig singleServerConfig = config.useSingleServer()
                    .setAddress("redis://" + properties.getStandalone().getHost() + ":" + properties.getStandalone().getPort())
                    .setConnectionPoolSize(properties.getPoolSize())
                    .setConnectionMinimumIdleSize(properties.getMinIdleSize())
                    .setIdleConnectionTimeout(properties.getIdleTimeout())
                    .setConnectTimeout(properties.getConnectTimeout())
                    .setRetryAttempts(properties.getRetryAttempts())
                    .setRetryInterval(properties.getRetryInterval())
                    .setPingConnectionInterval(properties.getPingInterval())
                    .setKeepAlive(properties.isKeepAlive());

            if (hasText(properties.getPassword())) {
                singleServerConfig.setPassword(properties.getPassword());
            }
        }

        return Redisson.create(config);
    }

    private String[] toRedisAddresses(List<String> nodes) {
        if (nodes == null || nodes.isEmpty()) {
            throw new IllegalArgumentException("redis cluster nodes must not be empty");
        }

        return nodes.stream()
                .filter(node -> node != null && !node.isBlank())
                .map(node -> node.startsWith("redis://") ? node : "redis://" + node)
                .toArray(String[]::new);
    }

}
