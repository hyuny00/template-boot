package com.futechsoft.framework.common.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.futechsoft.framework.common.mapper.CommonMapper;

@Service
public class CacheUpdateService {
  
    
    @Resource(name = "framework.common.mapper.CommonMapper")
	private CommonMapper commonMapper;
    
    
    /**
     * 특정 캐시의 최신 업데이트 시간 기록
     * @throws Exception 
     */
    public void updateCacheTimestamp(String cacheName) throws Exception {
    	commonMapper.deleteCache(cacheName);
    	commonMapper.insertCache(cacheName);
    }
}
