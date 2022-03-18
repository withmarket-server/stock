package com.example.demo.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
//@ConfigurationProperties(prefix = "spring.redis.cluster")
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public RedissonClient createRedisClient() {
        return buildRedissionClient();
    }

    private RedissonClient buildRedissionClient() {
        Config config = new Config().setCodec(new JsonJacksonCodec());
        config.useSingleServer().setAddress("redis://"+host+":"+port);
        return Redisson.create(config);
    }

    /* 레디스 클러스터 설정
    private List<String> nodes;

    public List<String> getNodes() {
        return nodes;
    }

    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
    }

    @Bean
    public RedissonClient createRedisClient() {
        return buildRedissionClient();
    }

    private RedissonClient buildRedissionClient() {
        Config config = new Config().setCodec(new JsonJacksonCodec(buildObjectMapper()));
        ClusterServersConfig clusterConfig = config.useClusterServers().setScanInterval(2000);
        getNodes().forEach(clusterConfig::addNodeAddress);
        return Redisson.create(config);
    }

    // DataTime 직렬화 설정
    public ObjectMapper buildObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        List<String> nodeCollection = nodes.stream().map(node -> node.substring(8))
                .collect(Collectors.toList());
        return new LettuceConnectionFactory(new RedisClusterConfiguration(nodeCollection));
    }
     */

//     RedisTemplate 설정
//    @Bean
//    public RedisTemplate<String, InvestProduct> redisTemplate() {
//        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
//
//        RedisTemplate<String, InvestProduct> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(redisConnectionFactory());
//
//        // json 형식으로 데이터를 받을 때 값이 깨지지 않게 직렬화를 진행한다
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(serializer);
//        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//        redisTemplate.setHashValueSerializer(serializer);
//
//        redisTemplate.setEnableTransactionSupport(true);
//
//        return redisTemplate;
//    }


    /**
     * Redis Cache를 이용하기 위해서 cache manager를 등록한다 <br>
     * redisCacheConfiguration : Redis Cache에 사용자 설정을 부여 <br>
     * disableCachingNullValues : null의 캐싱 방지 <br>
     * entryTil : 캐시의 Ttl(Time to live)를 설정. <br>
     * serializeKeysWith : 캐시 Key를 Serialize-Deserialize 하는데 사용하는 Pair를 지정 <br>
     * serializeValuesWith : 캐시 Value를 Serialize-Deserialize 하는데 사용하는 Pair를 지정 <br>
     * Value의 경우에는 다양한 클래스가 들어오기 때문에 GenericJackson2JsonRedisSerializer를 사용 <br>
     */
    /*
    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration
                .defaultCacheConfig()
                .disableCachingNullValues()
                .entryTtl(Duration.ofDays(1L))
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())
                )
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer())
                );

        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .build();
    }
     */
}
