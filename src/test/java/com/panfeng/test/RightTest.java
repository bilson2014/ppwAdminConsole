package com.panfeng.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.panfeng.persist.RightMapper;
import com.panfeng.resource.model.Right;
import com.panfeng.service.RightService;

public class RightTest extends BaseTest {

	@Autowired
	private final RightMapper mapper = null;
	
	@Autowired
	private final RightService service = null;
	
	@Test
	public void maxPos(){
		final long pos = mapper.findMaxPos();
		final long code = mapper.findMaxCodeByPos(pos);
		System.err.println("pos: " + pos + " ,code: " + code);
	}
	
	@Test
	public void save(){
		Right right = new Right();
		right.setRightName("根节点");
		right.setRightDescription("root");
		right.setResourceType(0);
		service.save(right);
	}
	
}
