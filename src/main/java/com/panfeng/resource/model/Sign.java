package com.panfeng.resource.model;

import java.util.Random;

import com.panfeng.domain.BaseObject;

public class Sign extends BaseObject {

	private static final long serialVersionUID = -2140105539445226864L;

	private long timestamp;
	private String key = null;
	private Integer random = null;

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Integer getRandom() {
		return random;
	}

	public void setRandom(Integer random) {
		this.random = random;
	}

	public static class SignBuilder {
		private Sign sign;

		public SignBuilder() {
			super();
			this.sign = new Sign();
		}

		public SignBuilder setTimestamp(long timestamp) {
			sign.timestamp = timestamp;
			return this;
		}

		public SignBuilder setKey(String key) {
			sign.key = key;
			return this;
		}

		public SignBuilder setRandom(Integer random) {
			sign.random = random;
			return this;
		}

		public Sign build() {
			if (sign.timestamp < 0)
				sign.timestamp = System.currentTimeMillis();
			if (sign.random == null) {
				Random random = new Random();
				sign.random = random.nextInt(99999999);
			}
			if (sign.key == null && sign.key.length() <= 0) {
				throw new NullPointerException("签名KEY不能为NULL");
			}
			return sign;
		}
	}

}
