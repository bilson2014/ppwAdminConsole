package com.panfeng.resource.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.panfeng.service.FDFSService;
import com.panfeng.service.KindeditorService;
import com.panfeng.util.Constants;

/**
 * 编辑器相关
 * @author Jack
 *
 */
@RestController
public class KindeditorController extends BaseController {

	@Autowired
	private KindeditorService kindeditorService;
	@Autowired
	private FDFSService dfsService;

	//上传图片和视频
	@RequestMapping(value = "/kindeditor/uploadImage", produces = "text/html; charset=UTF-8")
	public String uploadImage(final MultipartRequest multipartRequest,
			final String dir, final String sessionId,HttpServletRequest request) {
		// 从源码得知 上传input标签名为“imgFile”
		MultipartFile multipartFile = multipartRequest.getFile("imgFile");
		// step 1.验证文件
		String result = kindeditorService.verifyImage(multipartFile, dir);
		if (!"".equals(result))
			return kindeditorService.createMsg(result, Constants.MSG_FAIL);
		// step 2.保存文件
		result = dfsService.upload(multipartFile);
		if (Constants.FAIL.equals(result))
			return kindeditorService.createMsg("保存文件失败。", Constants.MSG_FAIL);
		// step 3.生成图片URL
		result = kindeditorService.getImageUrl(result);
		return kindeditorService.createMsg(result, Constants.MSG_SUCCESS);
	}
	
	
	
	
	@RequestMapping(value = "/kindeditor/delete/{sessionId}")
	public boolean delete(@PathVariable("sessionId") final String sessionid) {
		if (sessionid != null &&!"".equals(sessionid))
			kindeditorService.deleteImageDir(sessionid);
		return true;
	}
}
