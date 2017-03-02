package com.panfeng.test;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.panfeng.persist.ProductMapper;
import com.panfeng.resource.model.Product;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class ProductTest {

	
	@Autowired
	private final ProductMapper mapper = null;
	
	@Test
	public void testFindProduct(){
		
		final List<Product> list = mapper.findProductByArray(new long[]{1,2});
		System.err.println(list.size());
		for(Product product : list){
			System.err.println(product);
		}
	}
	
	/*@Test
	public void updateImageUrl() {
		
		final List<Product> list = mapper.productWithOpt();
		if (list != null && list.size() > 0){
			for (final Product product : list) {
				String videoUrl = product.getVideoUrl();
				String picLDUrl = product.getPicLDUrl();
				String picHDUrl = product.getPicHDUrl();
				
				if(videoUrl != null && !"".equals(videoUrl)){
					videoUrl = videoUrl.split("/opt")[1];
				}
				
				if(picLDUrl != null && !"".equals(picLDUrl)){
					picLDUrl = picLDUrl.split("/opt")[1];
				}
				
				if(picHDUrl != null && !"".equals(picHDUrl)){
					picHDUrl = picHDUrl.split("/opt")[1];
				}
				product.setVideoUrl(videoUrl);
				product.setPicLDUrl(picLDUrl);
				product.setPicHDUrl(picHDUrl);
				mapper.updateFileUrl(product);
			}
		}
	}*/
	
	@Test
	public void testLoadProductMap(){
		
		Map<Long,Product> map = mapper.getProductByRecommend();
		System.err.println(map);
	}
}
