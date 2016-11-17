package com.panfeng.flow.taskchain;

/**
 * 任务阶段状态
 * 
 * @author wang
 *
 */
public enum EventType {
	SMS(0, "短信"),MAIL(1, "邮件");

	private int id;
	private String name;

	private EventType(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(int id, String name) {
		this.name = name;
		this.id = id;
	}

}
