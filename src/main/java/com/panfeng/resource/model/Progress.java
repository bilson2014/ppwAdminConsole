package com.panfeng.resource.model;

import com.paipianwang.pat.common.entity.BaseEntity;

public class Progress extends BaseEntity {

	private static final long serialVersionUID = -2839198745323604044L;

	private long progressId = 0l;
	
	private long pBytesRead = 0l; // 到目前为止读取文件的比特数
	
	private long pContentLength = 0l; // 文件总大小
	
	private long pItems = 0l; // 目前正在读取第几个文件

	public long getProgressId() {
		return progressId;
	}

	public void setProgressId(long progressId) {
		this.progressId = progressId;
	}

	public long getpBytesRead() {
		return pBytesRead;
	}

	public void setpBytesRead(long pBytesRead) {
		this.pBytesRead = pBytesRead;
	}

	public long getpContentLength() {
		return pContentLength;
	}

	public void setpContentLength(long pContentLength) {
		this.pContentLength = pContentLength;
	}

	public long getpItems() {
		return pItems;
	}

	public void setpItems(long pItems) {
		this.pItems = pItems;
	}
	
}
