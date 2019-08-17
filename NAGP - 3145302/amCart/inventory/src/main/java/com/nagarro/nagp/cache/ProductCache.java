package com.nagarro.nagp.cache;

import com.nagarro.nagp.model.Product;

public interface ProductCache {

	public void cacheProduct(Product product);
	public Product checkCache(String name);
}
