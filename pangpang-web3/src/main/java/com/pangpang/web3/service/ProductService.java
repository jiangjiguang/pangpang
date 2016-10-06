package com.pangpang.web3.service;

import com.pangpang.web3.domain.Product;

public interface ProductService {
	Product add(Product product);
	Product get(long id);
}
