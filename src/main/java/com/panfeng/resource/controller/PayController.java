package com.panfeng.resource.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.panfeng.resource.model.PayWebHook;
import com.panfeng.service.PayWebHookService;

@RestController
@RequestMapping("/pay")
public class PayController extends BaseController {

	@Autowired
	PayWebHookService payWebHookService;

	@RequestMapping("/hook/callback")
	public void hookCallback(@RequestBody PayWebHook payWebHook,
			HttpServletResponse response) {
		
		payWebHookService.hookInfoProcessing(payWebHook);
//		try {
//			ServletOutputStream out = response.getOutputStream();
//			if (true) { // 验证成功
//				out.println("success"); // 请不要修改或删除
//			} else { // 验证失败
//				out.println("fail");
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
	@RequestMapping("/")
	public void pay(){
		
	}  
}
