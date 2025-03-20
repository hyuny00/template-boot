package com.futechsoft.config;

import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
@EnableCaching
public class CacheConfig {
	
	 @Bean
	    public CacheManager cacheManager() {
	        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
	        
	        // 모든 캐시에 동일한 설정 적용
	        cacheManager.setCaffeine(Caffeine.newBuilder()
	                .maximumSize(3000)   
	                .expireAfterAccess(30, TimeUnit.DAYS) 
	                .recordStats());     
	        
	        return cacheManager;
	    }

}
