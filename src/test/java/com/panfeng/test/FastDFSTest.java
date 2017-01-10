package com.panfeng.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.panfeng.service.FDFSService;

public class FastDFSTest extends BaseTest{

	@Autowired
	private final FDFSService service = null;
	
	@Test
	public void test() {
		final String fileId = "group1/M00/00/02/CgptuFhfwDyAJTaMAGJJDuq79eI484.mp4";
		final int result = service.delete(fileId);
		System.err.println(result == 0 ? "删除失败" : "删除成功");
	}
}
