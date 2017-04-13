package com.panfeng.test;

import org.junit.Test;

import com.paipianwang.pat.common.web.file.FastDFSClient;

public class FastDFSTest extends BaseTest{

	@Test
	public void test() {
		final String fileId = "group1/M00/00/02/CgptuFhfwDyAJTaMAGJJDuq79eI484.mp4";
		final int result = FastDFSClient.deleteFile(fileId);
		System.err.println(result == 0 ? "删除失败" : "删除成功");
	}
}
