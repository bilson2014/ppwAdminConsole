package com.panfeng.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.paipianwang.pat.facade.right.entity.PmsRight;
import com.paipianwang.pat.facade.right.service.PmsRightFacade;

public class RightTest extends BaseTest {

	@Autowired
	private final PmsRightFacade pmsRightFacade = null;
	
	@Test
	public void maxPos(){
		final long pos = pmsRightFacade.getMaxPos();
		System.err.println(pos);
		/*final long code = pmsRightFacade.findMaxCodeByPos(pos);
		System.err.println("pos: " + pos + " ,code: " + code);*/
	}
	
	@Test
	public void save(){
		PmsRight right = new PmsRight();
		right.setRightName("根节点");
		right.setRightDescription("root");
		right.setResourceType(0);
		pmsRightFacade.save(right);
	}
	
}
