package com.panfeng.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.beecloud.BCEumeration.PAY_CHANNEL;
import cn.beecloud.BCPay;
import cn.beecloud.BCUtil;
import cn.beecloud.bean.BCException;
import cn.beecloud.bean.BCOrder;

import com.panfeng.service.PayService;

public class PayServiceImpl implements PayService {

	@Override
	public void pay(BCOrder bcOrder) {
		bcOrder.getChannel();

	}

	/**
	 * 编写银联发起支付
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	// UN_WEB -->银联
	// ALI_WEB -->支付
	private void ewq(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ServletOutputStream out = response.getOutputStream();
		// 模拟商户的交易编号、标题、金额、附加数据
		String billNo = BCUtil.generateRandomUUIDPure();
		String title = "demo测试";
		// 附加数据-->会在回调中原样返回
		Map<String, Object> optional = new HashMap<String, Object>();
		optional.put("rui", "feng");
		// 支付类型
		String type = request.getParameter("paytype");
		PAY_CHANNEL channel;
		try {
			channel = PAY_CHANNEL.valueOf(type);
		} catch (Exception e) {
			channel = null;
		}

		BCOrder bcOrder = new BCOrder(channel, 1, billNo, title);
		bcOrder.setBillTimeout(360);
		bcOrder.setOptional(optional);

		// 以下是每个渠道的return url
		// String aliReturnUrl =
		// "http://localhost:8080/PC-Web-Pay-Demo/return_url_example/aliReturnUrl.jsp";
		String unReturnUrl = "http://localhost:8080/PC-Web-Pay-Demo/return_url_example/unReturnUrl.jsp";
		bcOrder.setReturnUrl(unReturnUrl);
		try {
			bcOrder = BCPay.startBCPay(bcOrder);
			System.out.print(bcOrder.getObjectId());
			out.println(bcOrder.getObjectId());
			Thread.sleep(3000);
			out.println(bcOrder.getHtml());
		} catch (BCException e) {
			out.println(e.getMessage());
		}
	}
}
