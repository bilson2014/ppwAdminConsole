package com.panfeng.service;

import java.util.List;
import java.util.Map;

import com.panfeng.domain.BaseMsg;
import com.panfeng.resource.model.DealLog;

public interface DealLogService {
	/**
	 * 获取订单号
	 * 
	 * @return
	 */
	String getBillNo();

	/**
	 * 开始进入支付流程
	 * 
	 * @param dealLog
	 *            支付对象
	 * @return 支付结果
	 */
	BaseMsg payIncome(String token) throws Exception;

	/**
	 * 获取该项目的所有支付记录
	 * 
	 * @param projectId
	 *            项目ID
	 * @return
	 */
	List<DealLog> getDealLogByProject(Map<String,String> pram);

	/**
	 * 生成支付 Url Token
	 * 
	 * @param dealLog
	 * @return
	 */
	String getToken(DealLog dealLog) throws Exception;

	/**
	 * 退款
	 * 
	 * @param dealLog
	 *            订单对象
	 * @return
	 */
	String sendRefund(DealLog dealLog);

	/**
	 * 发起支付
	 * 
	 * @param dealLog
	 * @return
	 */
	BaseMsg sendPay(DealLog dealLog) throws Exception;

	/**
	 * 验证支付记录是否在有效期内（48h）
	 * 
	 * @return
	 */
	DealLog getOrderInfo(String token) throws Exception;

	/**
	 * 获取订单Id
	 * 
	 * @param billNo
	 * @return
	 */
	DealLog findDealByBillNo(String billNo);

	/**
	 * 获取分享url
	 * 
	 * @param dealLog
	 * @return
	 */
	BaseMsg shareUrl(String token) throws Exception;

	long update(DealLog dealLog);

	// 线下支付
	BaseMsg offlineSave(DealLog dealLog);
	
	DealLog getDefaultDeal(long projectId);
	
	/**
	 * 关闭支付订单
	 * @return
	 * @throws Exception 
	 */
	BaseMsg offOrder(String token) throws Exception;
	
	BaseMsg orderNumber(long projectId);
	
	/**
	 * 是否还有为支付订单
	 * @param userType
	 * @param userId
	 * @return
	 */
	BaseMsg notPayNumber(String userType,long userId);
	
	long notPayNumber(long projectId);
}
