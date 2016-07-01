package com.panfeng.resource.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.panfeng.domain.BaseMsg;
import com.panfeng.resource.model.DealLog;
import com.panfeng.resource.model.PayWebHook;
import com.panfeng.service.DealLogService;
import com.panfeng.service.PayWebHookService;
import com.panfeng.util.ValidateUtil;

@RestController
@RequestMapping("/pay")
public class PayController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger("service");

	static String RETURN_KEY = "billNo";
	static String RESULT_KEY = "result";

	@Autowired
	PayWebHookService payWebHookService;
	@Autowired
	DealLogService dealLogService;

	@RequestMapping(value = "/hook", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public void hookInfoProcessing(@RequestBody PayWebHook payWebHook, HttpServletResponse response) {
		// debug
		if (payWebHook != null) {
			logger.info(payWebHook.toString());
			System.out.println(payWebHook.toString());
		}
		// debug end
		String res = payWebHookService.hookInfoProcessing(payWebHook);

		// debug
		if (res != null) {
			logger.info("res:=" + res);
			System.out.println("res:=" + res);
		}
		// debug end

		response.setCharacterEncoding("UTF-8");
		try {
			PrintWriter printWriter = response.getWriter();
			printWriter.write(res);
			printWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/get/billno")
	public DealLog getBillNo(@RequestBody Map<String, Long> map) {

		return dealLogService.getDefaultDeal(map.get("projectId"));
	}

	@RequestMapping(value = "/income", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public BaseMsg payIncome(String token) {
		System.out.println("");
		try {
			return dealLogService.payIncome(token);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new BaseMsg();
	}

	@RequestMapping(value = "/get/deallogs", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public List<DealLog> getDealLogByProject(@RequestBody Map<String, String> projectId) {
		List<DealLog> deals = dealLogService.getDealLogByProject(projectId);
		if (deals == null) {
			return new ArrayList<DealLog>();
		} else {
			return deals;
		}
	}

	@RequestMapping(value = "/sendpay", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public BaseMsg sendPay(@RequestBody DealLog dealLog) {
		BaseMsg baseMsg;
		try {
			baseMsg = dealLogService.sendPay(dealLog);
			if (baseMsg == null || dealLog.getBillNo() == null || dealLog.getProjectName() == null
					|| dealLog.getUserName() == null || dealLog.getPayPrice() == null) {
				return new BaseMsg(BaseMsg.ERROR, "发起失败", null);
			} else {
				return baseMsg;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseMsg(BaseMsg.ERROR, "发起失败", null);
		}
	}

	@RequestMapping(value = "/shareurl", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public BaseMsg shartUrl(@RequestBody Map<String, String> token) {
		if (ValidateUtil.isValid(token)) {
			try {
				BaseMsg baseMsg = dealLogService.shareUrl(token.get("token"));
				if (baseMsg == null) {
					return new BaseMsg(BaseMsg.ERROR, "创建失败x", null);
				} else {
					return baseMsg;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return new BaseMsg(BaseMsg.ERROR, "创建异常", e.getMessage());
			}
		} else {
			return new BaseMsg(BaseMsg.ERROR, "Token不能为null", "");
		}
	}

	@RequestMapping("/get/orderview")
	public DealLog getOrderInfo(String token) {
		try {
			DealLog res = dealLogService.getOrderInfo(token);
			if (res != null)
				return res;
			else
				return new DealLog();
		} catch (Exception e) {
			e.printStackTrace();
			return new DealLog();
		}
	}

	@RequestMapping("/offline/save")
	public BaseMsg offlineSave(@RequestBody DealLog dealLog) {
		BaseMsg baseMsg = dealLogService.offlineSave(dealLog);
		return baseMsg;
	}

	@RequestMapping("/offorder")
	public BaseMsg offOrder(@RequestBody Map<String, String> token) {
		if (token == null) {
			return new BaseMsg(BaseMsg.ERROR, "token，不能为空", "");
		}
		try {
			BaseMsg baseMsg = dealLogService.offOrder(token.get("token"));
			return baseMsg;
		} catch (Exception e) {
			e.printStackTrace();
			return new BaseMsg(BaseMsg.ERROR, "token，解析失败", "");
		}
	}

	@RequestMapping("/hasOrderHistory")
	public BaseMsg hasOrderHistory(@RequestBody Map<String, Long> projectId) {
		if (projectId.size() < 1) {
			return new BaseMsg(BaseMsg.ERROR, "项目ID不能为空", "");
		}
		BaseMsg baseMsg = dealLogService.orderNumber(projectId.get("projectId"));
		return baseMsg;
	}

	@RequestMapping("/hasNotPayOrder")
	public BaseMsg hasNotPayOrder(@RequestBody Map<String, String> userInfo) {
		if (userInfo.size() < 2) {
			return new BaseMsg(BaseMsg.ERROR, "用户信息不完整", "");
		}
		BaseMsg baseMsg = dealLogService.notPayNumber(userInfo.get("userType"), Long.parseLong(userInfo.get("userId")));
		return baseMsg;
	}
}
