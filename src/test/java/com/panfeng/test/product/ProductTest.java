package com.panfeng.test.product;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.panfeng.mq.service.FileConvertMQService;
import com.panfeng.resource.model.Product;
import com.panfeng.service.ProductService;
import com.panfeng.test.BaseTest;

public class ProductTest extends BaseTest {

	@Autowired
	private ProductService productService = null;
	
	@Autowired
	private final FileConvertMQService fileConvertMQService = null;
	
	@Test
	public void convert() {
		List<Product> list = productService.selectFilmToConvert();
		for (Product product : list) {
			fileConvertMQService.sendMessage(product.getProductId(), product.getVideoUrl());
		}
	}
}
