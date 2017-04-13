package com.panfeng.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paipianwang.pat.common.config.PublicConfig;
import com.panfeng.resource.model.DealLog;
import com.panfeng.resource.model.PayWebHook;
import com.panfeng.service.DealLogService;
import com.panfeng.service.PayWebHookService;
import com.panfeng.util.DateUtils;
import com.sun.star.uno.Exception;

@Service
public class PayWebHookServiceImpl implements PayWebHookService {

	public static String SUCCESS = "success";

	public static String FAIL = "fail";

	@Autowired
	DealLogService dealLogService;

	static String PAT_HOOK_URL = PublicConfig.PAY_SERVER + "pay/hook";

	/**
	 * 2.消息去重复<br>
	 * 3.认证价格<br>
	 * 4.处理定订单相关逻辑&&返回结果<br>
	 */
	public String hookInfoProcessing(PayWebHook payWebHook) {

		if (payWebHook == null) {
			sendError("消息不能为空");
			return FAIL;
		}
		// 该订单号状态：已经完成，则该消息可能是重发消息。跳过不处理
		String transactionId = payWebHook.getTransaction_id();// 发起支付时的订单号
		DealLog dealLog = dealLogService.findDealByBillNo(transactionId);
		if (dealLog != null) {
			if (dealLog.getDealStatus().equals(DealLog.DEAL_STATUS_SUCCEED)) {
				sendError("消息是重发消息，滤掉返回处理成功" + payWebHook.toString());
				// 消息是重发消息，滤掉返回处理成功
				return SUCCESS;
			} else {
				// 认证价格
				long transactionFee = payWebHook.getTransaction_fee();// 以分为单位
				Double payPrice = dealLog.getPayPrice();
				long payPricePoints = yuanConversionPoints(payPrice);

				if (transactionFee == payPricePoints) {
					// 价格相同 认证通过，开始处理订单相关逻辑
					dealLog.setDealStatus(DealLog.DEAL_STATUS_SUCCEED);
					dealLog.setUnOrderId(payWebHook.getMessage_detail().get("queryId"));
					dealLog.setPayTime(DateUtils.getDateByFormatStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
					dealLog.setPayChannel("银联");
					long l = dealLogService.update(dealLog);
					if (l > 0) {
						return SUCCESS;
					} else {
						sendError("更新失败，数据库无法更新" + payWebHook.toString());
						return FAIL;
					}
				} else {
					sendError("价格认证失败|" + payWebHook.toString() + "|" + dealLog.toString());
					return FAIL;
				}
			}
		} else {
			sendError("该订单在数据库中不存在" + payWebHook.toString());
			System.out.println("该订单在数据库中不存在" + payWebHook.toString());
			// 消息是重发消息，滤掉返回处理成功
			return FAIL;
		}
	}

	private void sendError(String msg) {
		try {
			throw new Exception(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int yuanConversionPoints(double yuan) {
		BigDecimal bdYuan = BigDecimal.valueOf(yuan);
		BigDecimal bdPoints = bdYuan.multiply(BigDecimal.valueOf(100));
		return bdPoints.intValue();
	}

	@SuppressWarnings("unused")
	private int pointsConversionYuan(double points) {
		BigDecimal bdYuan = BigDecimal.valueOf(points);
		BigDecimal bdPoints = bdYuan.divide(BigDecimal.valueOf(100));
		return bdPoints.intValue();
	}

}
