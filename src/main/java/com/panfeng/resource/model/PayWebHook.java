package com.panfeng.resource.model;

import java.util.Map;

import com.panfeng.domain.BaseObject;

/**
 * 支付回掉返回实体
 * 
 * @author laowang
 *
 */
public class PayWebHook extends BaseObject {

	private static final long serialVersionUID = -8830509334980777367L;

	private String sign = null;
	private Long timestamp = null;
	private String channel_type = null;
	private String sub_channel_type = null;
	private String transaction_type = null;
	private String transaction_id = null;
	private Integer transaction_fee = null;
	private Boolean trade_success = null;
	private Map<String, String> optional = null;
	private Map<String, String> message_detail = null;

	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("sign:");
		stringBuffer.append(sign != null ? sign.toString() : "");
		stringBuffer.append(" timestamp:");
		stringBuffer.append(timestamp != null ? timestamp.toString() : "");
		stringBuffer.append(" channel_type:");
		stringBuffer.append(channel_type != null ? channel_type.toString() : "");
		stringBuffer.append(" sub_channel_type:");
		stringBuffer.append(sub_channel_type != null ? sub_channel_type.toString() : "");
		stringBuffer.append(" transaction_type:");
		stringBuffer.append(transaction_type != null ? transaction_type.toString() : "");
		stringBuffer.append(" transaction_id:");
		stringBuffer.append(transaction_id != null ? transaction_id.toString() : "");
		stringBuffer.append(" transaction_fee:");
		stringBuffer.append(transaction_fee != null ? transaction_fee.toString() : "");
		stringBuffer.append(" trade_success:");
		stringBuffer.append(trade_success != null ? trade_success.toString() : "");
		stringBuffer.append(" optional:");
		stringBuffer.append(optional != null ? optional.toString() : "");
		stringBuffer.append(" message_detail:");
		stringBuffer.append(message_detail != null ? message_detail.toString() : "");
		if (message_detail != null) {
			for (String key : message_detail.keySet()) {
				stringBuffer.append("key= " + key + " and value= " + message_detail.get(key));
			}
		}
		return stringBuffer.toString();
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getChannel_type() {
		return channel_type;
	}

	public void setChannel_type(String channel_type) {
		this.channel_type = channel_type;
	}

	public String getSub_channel_type() {
		return sub_channel_type;
	}

	public void setSub_channel_type(String sub_channel_type) {
		this.sub_channel_type = sub_channel_type;
	}

	public String getTransaction_type() {
		return transaction_type;
	}

	public void setTransaction_type(String transaction_type) {
		this.transaction_type = transaction_type;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	public Integer getTransaction_fee() {
		return transaction_fee;
	}

	public void setTransaction_fee(Integer transaction_fee) {
		this.transaction_fee = transaction_fee;
	}

	public Boolean getTrade_success() {
		return trade_success;
	}

	public void setTrade_success(Boolean trade_success) {
		this.trade_success = trade_success;
	}

	public Map<String, String> getOptional() {
		return optional;
	}

	public void setOptional(Map<String, String> optional) {
		this.optional = optional;
	}

	public Map<String, String> getMessage_detail() {
		return message_detail;
	}

	public void setMessage_detail(Map<String, String> message_detail) {
		this.message_detail = message_detail;
	}

}
