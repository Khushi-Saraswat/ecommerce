package com.example.demo.service.impl;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.cache.Cache;

@Service
public class CacheInspectionService {
    
       
    @Autowired
    private CacheManager cacheManager;



    public void printCacheContents(String cacheName){
        Cache cache= cacheManager.getCache(cacheName);
        if(cache != null){
            System.out.println("Cache content");
            System.out.println(Objects.requireNonNull(cache.getNativeCache()).toString());
        }else{
            System.out.println("No such cache: "+cacheName);
        }

   }






}
