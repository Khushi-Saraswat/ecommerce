package com.example.demo.config;

import java.util.StringJoiner;

import org.springframework.stereotype.Component;

@Component
public class KeyGenerator {

    private static final String Pagination_CACHE_PREFIX="product:cache";



    public String generateScrollCacheKey(

        Integer pageNo,
        Integer PageSize,
        String category

    ){
         
        StringJoiner joiner=new StringJoiner(":");
        joiner.add(Pagination_CACHE_PREFIX);
        joiner.add("page="+pageNo);
        joiner.add("page="+PageSize);
        joiner.add("category="+category);

        //convert stringjoiner to string
        return joiner.toString();



    }




    //delete all cache 
    public String generateScrollCachePattern(){


        return Pagination_CACHE_PREFIX + ":*";
    }

   private void addIfNotAbsent(StringJoiner joiner,String key,Object value){
      if(value != null)
       {
           joiner.add(key + "=" + value);
       }
     
    }
    
}
