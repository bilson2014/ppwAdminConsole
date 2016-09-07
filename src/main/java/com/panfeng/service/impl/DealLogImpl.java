package com.panfeng.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.panfeng.domain.BaseMsg;
import com.panfeng.domain.GlobalConstant;
import com.panfeng.persist.DealLogMapper;
import com.panfeng.resource.model.DealLog;
import com.panfeng.resource.model.IndentProject;
import com.panfeng.service.DealLogService;
import com.panfeng.service.IndentProjectService;
import com.panfeng.util.AESUtil;
import com.panfeng.util.DateUtils;
import com.panfeng.util.HttpsUtils;
import com.panfeng.util.JsonUtil;
import com.panfeng.util.PathFormatUtils;
import com.panfeng.util.ValidateUtil;
import com.sun.star.lang.NullPointerException;

@Service
public class DealLogImpl implements DealLogService {
	@Autowired
	DealLogMapper dealLogMapper;

	static String KEY = "PanFengYOUWen659";
	static int PAY_URL_TIMEOUT = 30 * 60 * 1000; // ms
	static int PAY_ORDER_TIMEOUT = 48 * 60 * 60 * 1000; // ms
	static String PAY_INCOME_URL = GlobalConstant.PAY_SERVER + "pay/income";
	static String UN_WEB_RETURNURL = GlobalConstant.PAY_RETURN_SERVER + "payment/success";
	@Autowired
	IndentProjectService indentProjectService;

	@Override
	public String getBillNo() {

		return null;
	}

	@Override
	public BaseMsg payIncome(String token) throws Exception {
		// 进入支付流程
		// 1.验证URL
		// 2.查询完整数据 && 订单是否在有效期内
		// 3.签名数据
		// 4.发起支付请求

		// step 1 验证URL
		String[] data = decodeToken(token);
		boolean timeout = verifyTime(data[1]);
		if (timeout) {
			return new BaseMsg(BaseMsg.ERROR, "链接已经失效", null);
		}
		// step 1 end

		// step 2 查询完整数据
		DealLog dealLog = dealLogMapper.findDealById(Long.parseLong(data[0]));
		if (!verifyOrder(dealLog)) {
			dealLog.setDealStatus(DealLog.DEAL_STATUS_OFF);
			dealLogMapper.update(dealLog);
			return new BaseMsg(BaseMsg.ERROR, "订单失效，请联系视频管家。", null);
		}
		dealLog.setPayChannel("UN_WEB");
		dealLog.setTitle(dealLog.getProjectName());
		dealLog.setReturnUrl(UN_WEB_RETURNURL);
		// step 2 end

		// step 3 签名数据
		// Sign sign = new
		// SignBuilder().setKey(GlobalConstant.PAY_SIGN_KEY).build();
		// dealLog.setSign(sign);
		// step 3 end

		// step 4 发起支付请求
		String str = HttpsUtils.httpsPost(PAY_INCOME_URL, dealLog, null, false);
		if (ValidateUtil.isValid(str)) {
			return JsonUtil.toBean(str, BaseMsg.class);
		} else {
			return new BaseMsg(BaseMsg.ERROR, "服务器繁忙请稍后重新尝试", null);
		}
		// step 4 end
	}

	@Override
	public List<DealLog> getDealLogByProject(Map<String, String> pram) {
		if (pram.get("userid") != null && pram.get("userType") != null) {
			Long userId = Long.parseLong(pram.get("userid"));
			String userType = pram.get("userType");
			Long projectId = Long.parseLong(pram.get("projectId"));

			List<DealLog> dealLogs = null;
			switch (userType) {
			case GlobalConstant.ROLE_EMPLOYEE:
				dealLogs = dealLogMapper.findDealByProjectId(projectId);
				break;
			case GlobalConstant.ROLE_CUSTOMER:
				dealLogs = dealLogMapper.findDealByUserId(projectId, userId);
				break;
			case GlobalConstant.ROLE_PROVIDER:
				break;
			}
			if (dealLogs != null) {

				// 不能明文传递Id，整个系统只有Id才是通行证，其他信息均无效。
				// 剔除Id,绑定token
				if (ValidateUtil.isValid(dealLogs)) {
					for (DealLog dealLog : dealLogs) {
						if (DealLog.DEAL_STATUS_ONGOING == dealLog.getDealStatus()) {
							if (verifyOrder(dealLog)) {
								// 订单仍然在有效期内
								try {
									dealLog.setToken(getToken(dealLog));
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								// 订单已经过期
								dealLog.setDealStatus(DealLog.DEAL_STATUS_OFF);
								dealLogMapper.update(dealLog);
							}
						}
						setOrderTimeOut(dealLog); // 设置超时时间
						// 否则为其他状态，不添加token
						// 剔除Id属性
						dealLog.setDealId(-1L);
					}
				}
			}
			return dealLogs;
		}
		return new ArrayList<>();
	}

	@Override
	public String getToken(DealLog dealLog) throws Exception {
		String formatData = String.valueOf(dealLog.getDealId()) + "|" + String.valueOf(System.currentTimeMillis());
		String token = AESUtil.Encrypt2Hex(formatData, KEY);
		return token;
	}

	/**
	 * 发起退款
	 */
	@Override
	public String sendRefund(DealLog dealLog) {

		return null;
	}

	/**
	 * 发起支付
	 * 
	 * @throws Exception
	 */
	@Override
	public BaseMsg sendPay(final DealLog dealLog) throws Exception {
		// 设置基本属性
		dealLog.setDealLogSource(DealLog.DEALLOG_SOURCE_ONLINE);
		dealLog.setDealStatus(DealLog.DEAL_STATUS_ONGOING);
		dealLog.setLogType(DealLog.LOG_TYPE_INCOME);
		// 补充title
		dealLog.setTitle(dealLog.getProjectName());
		// 填充客户
		long proId = dealLog.getProjectId();
		IndentProject indentProject = new IndentProject();
		indentProject.setId(proId);
		indentProject = indentProjectService.getProjectInfo(indentProject);

		dealLog.setUserType(GlobalConstant.ROLE_CUSTOMER);
		dealLog.setUserId(indentProject.getCustomerId());

		long res = dealLogMapper.save(dealLog);
		if (res > 0) {
			String token = getToken(dealLog);
			System.out.println(token);
			return new BaseMsg(BaseMsg.NORMAL, "发起成功", token);
		} else
			return new BaseMsg(BaseMsg.ERROR, "发起失败", null);
	}

	public DealLog findDealLogBydlID(long dlId) {
		return dealLogMapper.findDealById(dlId);
	}

	/**
	 * 解码token
	 * 
	 * @param token
	 * @return
	 * @throws Exception
	 */
	static String[] decodeToken(String token) throws Exception {
		String Ostr = AESUtil.HexDecrypt(token, KEY);
		String[] data = Ostr.split("\\|");// 0 -->Id,1 -->timestamp
		return data;
	}

	/**
	 * 验证是否超时
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	static boolean verifyTime(String timestamp) throws Exception {
		long urltimestamp = Long.parseLong(timestamp);
		long currtimestamp = System.currentTimeMillis();
		if (currtimestamp - urltimestamp > PAY_URL_TIMEOUT)
			return true;
		else
			return false;
	}

	/**
	 * 验证支付记录是否在有效期内（48h）
	 * 
	 * @param dealLog
	 * @return
	 */
	static boolean verifyOrder(DealLog dealLog) {
		Date date = DateUtils.getDateByFormat(dealLog.getCreateTime(), "yyyy-MM-dd HH:mm:ss");
		long createTime = date.getTime();
		long currTime = System.currentTimeMillis();
		if ((currTime - createTime) < PAY_ORDER_TIMEOUT)
			return true;
		else
			return false;
	}

	/**
	 * 设置订单超时时间
	 * 
	 * @param dealLog
	 */
	static void setOrderTimeOut(DealLog dealLog) {
		Date date = DateUtils.getDateByFormat(dealLog.getCreateTime(), "yyyy-MM-dd HH:mm:ss");
		long createTime = date.getTime();
		long sumTime = createTime + PAY_ORDER_TIMEOUT;
		String formattime = DateUtils.getDateByFormatStr(new Date(sumTime), "yyyy-MM-dd HH:mm:ss");
		dealLog.setOrderTimeOut(formattime);
	}

	/**
	 * 加载去支付页面信息，
	 */
	@Override
	public DealLog getOrderInfo(String token) throws Exception {
		String[] data = decodeToken(token);
		boolean timeout = verifyTime(data[1]);
		DealLog DBdealLog = findDealLogBydlID(Long.parseLong(data[0]));
		DBdealLog.setToken(token);
		if (timeout) {
			DBdealLog.setUrlEffective(false);
		} else {
			DBdealLog.setUrlEffective(true);
		}
		// 订单状态为正常时，检测其是否超时
		if (DBdealLog.getDealStatus() == DealLog.DEAL_STATUS_ONGOING) {
			boolean res = verifyOrder(DBdealLog);
			setOrderTimeOut(DBdealLog);
			if (!res) { // 订单失效
				DBdealLog.setDealStatus(DealLog.DEAL_STATUS_OFF);
				dealLogMapper.update(DBdealLog); // 更改状态 订单完结
			}
		}
		DBdealLog.setDealId(-1L); // 解绑ID
		return DBdealLog;
	}

	@Override
	public DealLog findDealByBillNo(String billNo) {
		return dealLogMapper.findDealByBillNo(billNo);
	}

	@Override
	public long update(DealLog dealLog) {
		return dealLogMapper.update(dealLog);
	}

	@Override
	public BaseMsg shareUrl(String token) throws Exception {
		if (token == null) {
			return new BaseMsg(BaseMsg.ERROR, "token不能为空", null);
		}
		// 返回token，其他在前台拼接
		String[] data = decodeToken(token);
		if (!verifyTime(data[1])) {
			DealLog dealLog = dealLogMapper.findDealById(Long.parseLong(data[0]));
			if (dealLog != null) {
				if (verifyOrder(dealLog)) {
					// 订单在有效期内生成Token
					String newToken = getToken(dealLog);
					if (ValidateUtil.isValid(newToken))
						return new BaseMsg(BaseMsg.NORMAL, "正常", newToken);
					else
						return new BaseMsg(BaseMsg.ERROR, "token，生成失败", token);
				} else {
					return new BaseMsg(BaseMsg.ERROR, "token，订单已经失效", token);
				}
			} else {
				return new BaseMsg(BaseMsg.ERROR, "token无效", token);
			}
		} else {
			return new BaseMsg(BaseMsg.ERROR, "token无效", null);
		}
	}

	@Override
	public BaseMsg offlineSave(DealLog dealLog) {
		if (dealLog == null) {
			return new BaseMsg(BaseMsg.NORMAL, "储存失败,请完善订单信息", null);
		}

		dealLog.setDealLogSource(DealLog.DEALLOG_SOURCE_OFFLINE);
		dealLog.setDealStatus(DealLog.DEAL_STATUS_SUCCEED);
		dealLog.setLogType(DealLog.LOG_TYPE_INCOME);
		// 填充客户
		long proId = dealLog.getProjectId();
		IndentProject indentProject = new IndentProject();
		indentProject.setId(proId);
		indentProject = indentProjectService.getProjectInfo(indentProject);

		dealLog.setUserType(GlobalConstant.ROLE_CUSTOMER);
		dealLog.setUserId(indentProject.getCustomerId());
		dealLog.setBillNo(generateBillNo(indentProject.getSerial()));
		dealLog.setPayChannel("线下转账");

		long res = dealLogMapper.save(dealLog);
		if (res > 0)
			return new BaseMsg(BaseMsg.NORMAL, "储存成功", null);
		else
			return new BaseMsg(BaseMsg.NORMAL, "储存失败", null);
	}

	@Override
	public DealLog getDefaultDeal(long projectId) {
		IndentProject ip = new IndentProject();
		ip.setId(projectId);
		ip = indentProjectService.getRedundantProject(ip);
		if (ip != null) {
			DealLog dealLog = new DealLog();
			dealLog.setCreateTime(DateUtils.getDateByFormatStr(new Date(), "yyyy-MM-dd HH:mm:ss"));
			Double cusPay = ip.getCustomerPayment();
			Double finish = ip.getPriceFinish();
			Double last = ip.getPriceLast();
			Double first = ip.getPriceFirst();
			if (cusPay != null && cusPay != 0) {
				dealLog.setPayPrice(cusPay);
			} else if (finish != null && finish != 0) {
				dealLog.setPayPrice(finish);
			} else if (last != null && last != 0) {
				dealLog.setPayPrice(last);
			} else if (first != null && first != 0) {
				dealLog.setPayPrice(first);
			}
			dealLog.setProjectName(ip.getProjectName());
			dealLog.setUserName(ip.getUserName());

			dealLog.setBillNo(generateBillNo(ip.getSerial()));
			return dealLog;
		}
		return new DealLog();
	}

	public BaseMsg offOrder(String token) throws Exception {
		if (token == null)
			throw new NullPointerException("token,不能为空");
		String[] data = decodeToken(token);
		if (!verifyTime(data[1])) {
			DealLog dealLog = dealLogMapper.findDealById(Long.parseLong(data[0]));
			if (dealLog != null) {
				dealLog.setDealStatus(DealLog.DEAL_STATUS_OFF);
				dealLogMapper.update(dealLog);
				return new BaseMsg(BaseMsg.NORMAL, "关闭成功", null);
			} else {
				return new BaseMsg(BaseMsg.ERROR, "token无效", token);
			}
		} else {
			return new BaseMsg(BaseMsg.ERROR, "token无效", null);
		}
	}

	public static String generateBillNo(String serial) {
		String res = serial + PathFormatUtils.parse("{rand:4}{hh}{ii}{ss}");
		return res;
	}

	@Override
	public BaseMsg orderNumber(long projectId) {
		long count = dealLogMapper.orderNumber(projectId);
		return new BaseMsg(BaseMsg.NORMAL, "正常", count);
	}

	@Override
	public BaseMsg notPayNumber(String userType, long userId) {
		long count = dealLogMapper.notPayNumber(userType, userId);
		return new BaseMsg(BaseMsg.NORMAL, "正常", count);
	}

	@Override
	public long notPayNumber(long projectId) {
		return dealLogMapper.notPayNumberByProjectId(projectId);
	}
}
