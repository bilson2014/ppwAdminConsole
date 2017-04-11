package com.panfeng.resource.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.paipianwang.pat.common.entity.BaseEntity;
import com.paipianwang.pat.common.entity.BaseMsg;
import com.paipianwang.pat.common.entity.DataGrid;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.facade.product.entity.PmsChanPinConfiguration;
import com.paipianwang.pat.facade.product.service.PmsChanPinConfigurationFacade;

@RestController
@RequestMapping("/portal")
public class ChanPinConfigurationController extends BaseController {

	@Autowired
	private PmsChanPinConfigurationFacade PmsChanPinConfigurationFacade;

	@RequestMapping("/chanpinconfiguration-list")
	public ModelAndView activityView() {
		return new ModelAndView("/chanpinconfiguration-list");
	}

	@RequestMapping("/config/list")
	public DataGrid<PmsChanPinConfiguration> sceneList() {
		DataGrid<PmsChanPinConfiguration> allScene = PmsChanPinConfigurationFacade.getAllChanPinConfiguration();
		return allScene;
	}

	@RequestMapping("/config/save")
	public BaseMsg save(@RequestBody PmsChanPinConfiguration chanPin) {
		Map<String, Object> save = PmsChanPinConfigurationFacade.save(chanPin);
		BaseMsg baseMsg = new BaseMsg();
		baseMsg.setCode(BaseMsg.NORMAL);
		Long row = Long.valueOf(save.get(BaseEntity.SAVE_MAP_ROWS) + "");
		String res = row != null && row > 0 ? "保存成功" : "保存失败";
		baseMsg.setResult(res);
		return baseMsg;
	}

	@RequestMapping("/config/info/{cId}")
	public BaseMsg configInfo(@PathVariable long cId) {
		PmsChanPinConfiguration chanPinConfigurationInfo = PmsChanPinConfigurationFacade
				.getChanPinConfigurationInfo(cId);
		BaseMsg baseMsg = new BaseMsg();
		baseMsg.setCode(BaseMsg.NORMAL);
		baseMsg.setResult(chanPinConfigurationInfo);
		return baseMsg;
	}

	@RequestMapping("/config/update")
	public BaseMsg update(@RequestBody PmsChanPinConfiguration chanpin) {
		long update = PmsChanPinConfigurationFacade.update(chanpin);
		BaseMsg baseMsg = new BaseMsg();
		baseMsg.setCode(BaseMsg.NORMAL);
		baseMsg.setResult(update > 0 ? "保存成功" : "保存失败");
		return baseMsg;
	}

	@RequestMapping("/config/delete")
	public BaseMsg delete(String ids) {
		BaseMsg baseMsg = new BaseMsg();
		if (ValidateUtil.isValid(ids)) {
			String[] id = ids.split(",");
			for (int i = 0; i < id.length; i++) {
				PmsChanPinConfigurationFacade.delete(Long.valueOf(id[i]));
			}
			baseMsg.setCode(BaseMsg.NORMAL);

		} else {
			baseMsg.setCode(BaseMsg.ERROR);
			baseMsg.setErrorMsg("id不存在！");
		}
		return baseMsg;
	}

	@RequestMapping("/config/chanpin")
	public List<PmsChanPinConfiguration> getConfig(Long chanpinId) {
		List<PmsChanPinConfiguration> chanPinConfigurationByChanPinId = PmsChanPinConfigurationFacade
				.getChanPinConfigurationByChanPinId(chanpinId);
		return chanPinConfigurationByChanPinId;
	}

}
