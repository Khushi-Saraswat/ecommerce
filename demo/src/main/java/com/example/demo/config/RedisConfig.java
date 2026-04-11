package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;



@Configuration
@EnableCaching
public class RedisConfig {
    
           
       @Value("${spring.data.redis.host}")
       private String redisHost;
       @Value("${spring.data.redis.port:6379}")
       private int redisPort;
        @Value("${spring.data.redis.password:}")
       private String redisPassord;


       //configure RedisConnectionFactory bean to establish connection with Redis server using JedisConnectionFactory
       @Bean
       public RedisConnectionFactory redisConnectionException(){

        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName(redisHost);
        redisConfig.setPort(redisPort);
        if (!redisPassord.isEmpty() && redisPassord != null) {
            redisConfig.setPassword(redisPassord);
        }
        return new JedisConnectionFactory(redisConfig);
      
       }


       //redisTemplate bean can be defined here if needed for Redis operations
       @Bean
       public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // Define RedisTemplate bean if needed for Redis operations
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        //connection factory is set to the redisTemplate to enable Redis operations
        template.setConnectionFactory(redisConnectionFactory);
        //configure serializers for keys and values if necessary
        template.setKeySerializer(RedisSerializer.string());
        template.setValueSerializer(RedisSerializer.json());
        template.setHashKeySerializer(RedisSerializer.string());

        //configure value serializer for hash values if necessary
        template.setHashValueSerializer(RedisSerializer.json());

        template.afterPropertiesSet();

        return template;
        
       }





}
