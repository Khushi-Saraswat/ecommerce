package com.example.demo.service.impl.CacheService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.config.KeyGenerator;
// Assuming these are your local DTOs
// import com.example.demo.dto.ProductResponseDto; 
import com.example.demo.response.Product.ProductResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductCacheService {
    
    private final RedisTemplate<String, Object> redisTemplate;
    private final KeyGenerator keyGenerator;

    private static final Logger logger = LoggerFactory.getLogger(ProductCacheService.class);

    // CACHE configurations
    private static final long CACHE_TTL_MINUTES = 10;

  
    @Autowired
    private ObjectMapper jacksonObjectMapper;

    @SuppressWarnings("unchecked")
    public Page<ProductResponseDTO> getResponse(Integer pageNo, Integer pageSize, String category) {
    String cacheKey = keyGenerator.generateScrollCacheKey(pageNo, pageSize, category);
    try {
        Object cached = redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            logger.info("Cache found for key: {}", cacheKey);

        
            com.fasterxml.jackson.databind.JsonNode node = jacksonObjectMapper.valueToTree(cached);

            // Content (List) निकालें
            List<ProductResponseDTO> content = jacksonObjectMapper.convertValue(
                node.get("content"), 
                jacksonObjectMapper.getTypeFactory().constructCollectionType(List.class, ProductResponseDTO.class)
            );

            // Pagination डिटेल्स निकालें
            long totalElements = node.has("totalElements") ? node.get("totalElements").asLong() : content.size();
            
            // PageImpl वापस भेजें
            return new org.springframework.data.domain.PageImpl<>(content, org.springframework.data.domain.PageRequest.of(pageNo, pageSize), totalElements);
        }
        logger.info("Cache not found for Key: {}", cacheKey);
    } catch (Exception e) {
        logger.error("Error while fetching/converting cache for key: {}", cacheKey, e);
    }
    return null;
}


  public void cacheProductResponse(Page<ProductResponseDTO> response,
                                 Integer pageNo,
                                 Integer pageSize,
                                 String category) {

    String cacheKey = keyGenerator.generateScrollCacheKey(pageNo, pageSize, category);

    try {

        Map<String, Object> cacheData = new HashMap<>();
        cacheData.put("content", response.getContent());
        cacheData.put("totalElements", response.getTotalElements());
        cacheData.put("pageNo", response.getNumber());
        cacheData.put("pageSize", response.getSize());

        redisTemplate.opsForValue().set(
                cacheKey,
                cacheData,
                CACHE_TTL_MINUTES,
                TimeUnit.MINUTES
        );

        logger.info("Cache saved for key: {}", cacheKey);

    } catch (Exception e) {
        logger.error("Error while saving cache", e);
    }
}
    public void evictAllCache() {
        try {
            // Ensure your keyGenerator has a no-args version or a pattern generator
            String pattern = keyGenerator.generateScrollCachePattern();
            Set<String> keys = redisTemplate.keys(pattern);
            
            if (keys != null && !keys.isEmpty()) {
                Long deletedCount = redisTemplate.delete(keys);
                logger.info("Deleted {} keys from cache", deletedCount);
            } else {
                logger.info("No keys found in cache to evict");
            }
        } catch (Exception e) {
            logger.error("Error while deleting cache keys", e);
        }
    }
}