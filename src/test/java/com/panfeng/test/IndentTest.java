package com.panfeng.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.panfeng.persist.IndentMapper;
import com.panfeng.resource.model.Indent;
import com.panfeng.resource.view.IndentView;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})
public class IndentTest {

	@Autowired
	private final IndentMapper mapper = null;
	
	@Test
	public void testList(){
		final IndentView view = new IndentView();
		final List<Indent> list = mapper.listWithPagination(view);
		for (final Indent indent : list) {
			System.err.println(indent);
		}
	}
}
