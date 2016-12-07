package com.panfeng.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.panfeng.resource.model.Service;
import com.panfeng.service.ServiceService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class ServiceTest {
	
	@Autowired
	private ServiceService serService = null;
	
	@Test
	public void testSave(){
		Service service = new Service();
		service.setServiceOd(0);
		service.setServiceDescription("This is the first service ...");
		service.setServiceDiscount(0.8);
		service.setServiceName("First Service");
		service.setServicePrice(100);
		service.setServiceRealPrice(80);
		service.setProductId(13l);
		serService.save(service);
	}
	
	@Test
	public void testSearch(){
		final Service service = serService.findServiceById(10);
		System.err.println(service);
	}
}
