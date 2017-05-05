package com.panfeng.resource.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.paipianwang.pat.common.entity.DataGrid;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.common.web.file.FastDFSClient;
import com.paipianwang.pat.facade.product.entity.PmsScene;
import com.paipianwang.pat.facade.product.service.PmsSceneFacade;
import com.panfeng.domain.BaseMsg;

@RestController
@RequestMapping("/portal")
public class SceneController extends BaseController {

	@Autowired
	private PmsSceneFacade pmsSceneFacade;

	@RequestMapping("/scene-list")
	public ModelAndView activityView() {
		return new ModelAndView("/scene-list");
	}

	@RequestMapping("/scene/list")
	public DataGrid<PmsScene> sceneList() {
		DataGrid<PmsScene> allScene = pmsSceneFacade.getAllScene();
		return allScene;
	}

	@RequestMapping("/scene/save")
	public BaseMsg save(PmsScene scene, MultipartFile scenenPicLDUrlFile) {
		if (!scenenPicLDUrlFile.isEmpty()) {
			String upload = FastDFSClient.uploadFile(scenenPicLDUrlFile);
			scene.setScenenPicLDUrl(upload);
		}
		long save = pmsSceneFacade.save(scene);
		BaseMsg baseMsg = new BaseMsg();
		baseMsg.setCode(BaseMsg.NORMAL);
		baseMsg.setResult(save);
		return baseMsg;
	}

	@RequestMapping("/scene/update")
	public BaseMsg update(PmsScene scene, MultipartFile scenenPicLDUrlFile) {
		if (!scenenPicLDUrlFile.isEmpty()) {
			String upload = FastDFSClient.uploadFile(scenenPicLDUrlFile);
			scene.setScenenPicLDUrl(upload);
		}
		long update = pmsSceneFacade.update(scene);
		BaseMsg baseMsg = new BaseMsg();
		baseMsg.setCode(BaseMsg.NORMAL);
		baseMsg.setResult(update);
		return baseMsg;
	}

	@RequestMapping("/scene/delete")
	public BaseMsg delete(String ids) {
		BaseMsg baseMsg = new BaseMsg();
		if (ValidateUtil.isValid(ids)) {
			String[] id = ids.split(",");
			for (int i = 0; i < id.length; i++) {
				pmsSceneFacade.delete(Long.valueOf(id[i]));
			}
			baseMsg.setCode(BaseMsg.NORMAL);

		} else {
			baseMsg.setCode(BaseMsg.ERROR);
			baseMsg.setErrorMsg("id不存在！");
		}
		return baseMsg;
	}
}