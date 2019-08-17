package com.nagarro.nagp.cache;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.nagarro.nagp.model.Product;
import com.nagarro.nagp.redis.RedisUtil;

@Configuration
public class ProductCacheImpl implements ProductCache{

	public static final String TABLE_PRODUCT = "TABLE_PRODUCT";
    public static final String PRODUCT = "PRODUCT_";
    private RedisUtil<Product> redisUtil;
    
    @Autowired
    public ProductCacheImpl(RedisUtil<Product> redisUtilStudent) {
        this.redisUtil = redisUtilStudent;
    }
    
    @Override
    public void cacheProduct(Product product){
        redisUtil.putMap(TABLE_PRODUCT,  PRODUCT + product.getName(), product);
        redisUtil.setExpire(TABLE_PRODUCT, 1, TimeUnit.HOURS);
    }

	@Override
	public Product checkCache(String name) {
		return redisUtil.getMapAsSingleEntry(TABLE_PRODUCT, PRODUCT + name);
	}
    
    


}
