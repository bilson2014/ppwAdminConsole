package com.panfeng.resource.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.paipianwang.pat.common.entity.DataGrid;
import com.paipianwang.pat.common.entity.PageParam;
import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.common.util.DateUtils;
import com.paipianwang.pat.common.util.JsonUtil;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.common.web.file.FastDFSClient;
import com.paipianwang.pat.workflow.entity.PmsProductionActor;
import com.paipianwang.pat.workflow.entity.PmsProductionDevice;
import com.paipianwang.pat.workflow.entity.PmsProductionDirector;
import com.paipianwang.pat.workflow.entity.PmsProductionStudio;
import com.paipianwang.pat.workflow.entity.ProductionConstants;
import com.paipianwang.pat.workflow.enums.ProductionDeviceType;
import com.paipianwang.pat.workflow.facade.PmsProductionActorFacade;
import com.paipianwang.pat.workflow.facade.PmsProductionDeviceFacade;
import com.paipianwang.pat.workflow.facade.PmsProductionDirectorFacade;
import com.paipianwang.pat.workflow.facade.PmsProductionStudioFacade;
import com.panfeng.domain.BaseMsg;

/**
 * 制片资源管理
 * @author rui
 *
 */
@RestController
@RequestMapping("/portal")
public class ProductionResourceController extends BaseController {

	@Autowired
	private PmsProductionActorFacade pmsProductionActorFacade;
	@Autowired
	private PmsProductionDeviceFacade pmsProductionDeviceFacade;
	@Autowired
	private PmsProductionDirectorFacade pmsProductionDirectorFacade;
	@Autowired
	private PmsProductionStudioFacade pmsProductionStudioFacade;

	// --------------------演员--------------------------
	@RequestMapping(value = "/production/actor-list")
	public ModelAndView actorView(final HttpServletRequest request,final ModelMap model) {
		setDefaultReferrer(request, model);
		setStatusList(model);
		return new ModelAndView("production/production-actor-list", model);
	}

	@RequestMapping(value = "/production/actor/list", method = RequestMethod.POST)
	public DataGrid<PmsProductionActor> actorList(@RequestParam final Map<String, Object> paramMap,
			final PageParam param) {

		final long page = param.getPage();
		final long rows = param.getRows();
		param.setBegin((page - 1) * rows);
		param.setLimit(rows);

		paramMap.remove("page");
		paramMap.remove("rows");
		
		if(paramMap.containsKey("beginAge") && ValidateUtil.isValid((String)paramMap.get("beginAge"))) {
			paramMap.put("beginBirthDay", DateUtils.getAgeByYear(Integer.parseInt((String)paramMap.get("beginAge")))+"");
			paramMap.remove("beginAge");
		}
		if(paramMap.containsKey("endAge") && ValidateUtil.isValid((String)paramMap.get("endAge"))) {
			paramMap.put("endBirthDay", DateUtils.getAgeByYear(Integer.parseInt((String)paramMap.get("endAge")))+"");
			paramMap.remove("endAge");
		}

		final DataGrid<PmsProductionActor> dataGrid = pmsProductionActorFacade.listWithPagination(param, paramMap);
		return dataGrid;
	}

	@RequestMapping(value = "/production/actor/delete", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public void actorDelete(final long[] ids, HttpServletRequest request) {		
		List<PmsProductionActor> deletes=pmsProductionActorFacade.deleteByIds(ids);
		//删除图片
		if(ValidateUtil.isValid(deletes)) {
			for(PmsProductionActor actor:deletes) {
				String delImgStr=actor.getPhoto();
				if(ValidateUtil.isValid(delImgStr)) {
					String[] delImgs = delImgStr.split(";");
					for(String delImg:delImgs) {
						FastDFSClient.deleteFile(delImg);
					}
				}			
			}
		}
	}

	@RequestMapping(value = "/production/actor/save", method = RequestMethod.POST)
	public BaseMsg actorSave(final HttpServletRequest request, final HttpServletResponse response,
			@RequestParam final MultipartFile[] uploadFiles, final PmsProductionActor actor) throws Exception {
		BaseMsg msg = new BaseMsg();

		String pathList = "";
		pathList = editFile(uploadFiles, pathList, actor.getDelImg());

		actor.setPhoto(pathList);
		actor.setCreator(getCreator(request));
		pmsProductionActorFacade.insert(actor);
		// TODO 或者是先添加，再更新照片

		return msg;
	}

	@RequestMapping(value = "/production/actor/update", method = RequestMethod.POST)
	public BaseMsg actorUpdate(final HttpServletRequest request, final HttpServletResponse response,
			@RequestParam final MultipartFile[] uploadFiles, final PmsProductionActor actor) throws Exception {
		BaseMsg msg = new BaseMsg();

		String pathList = actor.getPhoto();
		// 上传图片
		pathList = editFile(uploadFiles, pathList, actor.getDelImg());

		actor.setPhoto(pathList);
		pmsProductionActorFacade.update(actor);

		return msg;
	}

	// --------------------设备--------------------------
	@RequestMapping(value = "/production/device-list")
	public ModelAndView deviceView(final HttpServletRequest request,final ModelMap model) {
		setDefaultReferrer(request, model);
		setStatusList(model);
		//设备类型
		ProductionDeviceType[] types=ProductionDeviceType.values();		
		List<Map<String,Object>> typesList=new ArrayList<>();
		for(ProductionDeviceType type:types) {
			Map<String,Object> map=new HashMap<>();
			map.put("key",type.getKey());
			map.put("name",type.getName());
			map.put("quotationType",type.getQuotationType());
			typesList.add(map);
		}
		
		model.put("typeList", JSONArray.toJSON(typesList));
		
		
		return new ModelAndView("production/production-device-list", model);
	}
	

	@RequestMapping(value = "/production/device/list", method = RequestMethod.POST)
	public DataGrid<PmsProductionDevice> deviceList(@RequestParam final Map<String, Object> paramMap,
			final PageParam param) {

		final long page = param.getPage();
		final long rows = param.getRows();
		param.setBegin((page - 1) * rows);
		param.setLimit(rows);

		paramMap.remove("page");
		paramMap.remove("rows");

		final DataGrid<PmsProductionDevice> dataGrid = pmsProductionDeviceFacade.listWithPagination(param, paramMap);
		return dataGrid;
	}

	@RequestMapping(value = "/production/device/delete", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public long deviceDelete(final long[] ids, HttpServletRequest request) {
		return pmsProductionDeviceFacade.deleteByIds(ids);
	}

	@RequestMapping(value = "/production/device/save", method = RequestMethod.POST)
	public BaseMsg deviceSave(final HttpServletRequest request, final HttpServletResponse response,
			@RequestParam final MultipartFile[] uploadFiles, final PmsProductionDevice device) throws Exception {
		BaseMsg msg = new BaseMsg();
		device.setCreator(getCreator(request));
		pmsProductionDeviceFacade.insert(device);
		return msg;
	}

	@RequestMapping(value = "/production/device/update", method = RequestMethod.POST)
	public BaseMsg deviceUpdate(final HttpServletRequest request, final HttpServletResponse response,
			@RequestParam final MultipartFile[] uploadFiles, final PmsProductionDevice device) throws Exception {
		BaseMsg msg = new BaseMsg();

		pmsProductionDeviceFacade.update(device);

		return msg;
	}
	
	// --------------------导演--------------------------
		@RequestMapping(value = "/production/director-list")
		public ModelAndView directorView(final HttpServletRequest request,final ModelMap model) {
			setDefaultReferrer(request, model);
			setStatusList(model);
			return new ModelAndView("production/production-director-list", model);
		}

		@RequestMapping(value = "/production/director/list", method = RequestMethod.POST)
		public DataGrid<PmsProductionDirector> directorList(@RequestParam final Map<String, Object> paramMap,
				final PageParam param) {

			final long page = param.getPage();
			final long rows = param.getRows();
			param.setBegin((page - 1) * rows);
			param.setLimit(rows);

			paramMap.remove("page");
			paramMap.remove("rows");

			final DataGrid<PmsProductionDirector> dataGrid = pmsProductionDirectorFacade.listWithPagination(param, paramMap);
			return dataGrid;
		}

		@RequestMapping(value = "/production/director/delete", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
		public void directorDelete(final long[] ids, HttpServletRequest request) {
			List<PmsProductionDirector> deletes=pmsProductionDirectorFacade.deleteByIds(ids);
			//删除图片
			if(ValidateUtil.isValid(deletes)) {
				for(PmsProductionDirector director:deletes) {
					String delImgStr=director.getPhoto();
					if(ValidateUtil.isValid(delImgStr)) {
						String[] delImgs = delImgStr.split(";");
						for(String delImg:delImgs) {
							FastDFSClient.deleteFile(delImg);
						}
					}			
				}
			}
		}

		@RequestMapping(value = "/production/director/save", method = RequestMethod.POST)
		public BaseMsg directorSave(final HttpServletRequest request, final HttpServletResponse response,
				@RequestParam final MultipartFile[] uploadFiles, final PmsProductionDirector director) throws Exception {
			BaseMsg msg = new BaseMsg();
			String pathList = "";
			pathList = editFile(uploadFiles, pathList, director.getDelImg());

			director.setPhoto(pathList);
			director.setCreator(getCreator(request));
			pmsProductionDirectorFacade.insert(director);
			return msg;
		}

		@RequestMapping(value = "/production/director/update", method = RequestMethod.POST)
		public BaseMsg directorUpdate(final HttpServletRequest request, final HttpServletResponse response,
				@RequestParam final MultipartFile[] uploadFiles, final PmsProductionDirector director) throws Exception {
			BaseMsg msg = new BaseMsg();

			String pathList = director.getPhoto();
			// 上传图片
			pathList = editFile(uploadFiles, pathList, director.getDelImg());

			director.setPhoto(pathList);
			
			pmsProductionDirectorFacade.update(director);

			return msg;
		}
		
		// --------------------场地--------------------------
		@RequestMapping(value = "/production/studio-list")
		public ModelAndView studioView(final HttpServletRequest request,final ModelMap model) {
			setDefaultReferrer(request, model);
			setStatusList(model);
			return new ModelAndView("production/production-studio-list", model);
		}

		@RequestMapping(value = "/production/studio/list", method = RequestMethod.POST)
		public DataGrid<PmsProductionStudio> studioList(@RequestParam final Map<String, Object> paramMap,
				final PageParam param) {

			final long page = param.getPage();
			final long rows = param.getRows();
			param.setBegin((page - 1) * rows);
			param.setLimit(rows);

			paramMap.remove("page");
			paramMap.remove("rows");

			final DataGrid<PmsProductionStudio> dataGrid = pmsProductionStudioFacade.listWithPagination(param, paramMap);
			return dataGrid;
		}

		@RequestMapping(value = "/production/studio/delete", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
		public void studioDelete(final long[] ids, HttpServletRequest request) {
			List<PmsProductionStudio> deletes=pmsProductionStudioFacade.deleteByIds(ids);
			//删除图片
			if(ValidateUtil.isValid(deletes)) {
				for(PmsProductionStudio studio:deletes) {
					String delImgStr=studio.getPhoto();
					if(ValidateUtil.isValid(delImgStr)) {
						String[] delImgs = delImgStr.split(";");
						for(String delImg:delImgs) {
							FastDFSClient.deleteFile(delImg);
						}
					}			
				}
			}
		}

		@RequestMapping(value = "/production/studio/save", method = RequestMethod.POST)
		public BaseMsg studioSave(final HttpServletRequest request, final HttpServletResponse response,
				@RequestParam final MultipartFile[] uploadFiles, final PmsProductionStudio studio) throws Exception {
			BaseMsg msg = new BaseMsg();

			String pathList = "";
			pathList = editFile(uploadFiles, pathList, studio.getDelImg());

			studio.setPhoto(pathList);
			studio.setCreator(getCreator(request));
			pmsProductionStudioFacade.insert(studio);
			// TODO 或者是先添加，再更新照片

			return msg;
		}

		@RequestMapping(value = "/production/studio/update", method = RequestMethod.POST)
		public BaseMsg studioUpdate(final HttpServletRequest request, final HttpServletResponse response,
				@RequestParam final MultipartFile[] uploadFiles, final PmsProductionStudio studio) throws Exception {
			BaseMsg msg = new BaseMsg();

			String pathList = studio.getPhoto();
			// 上传图片
			pathList = editFile(uploadFiles, pathList, studio.getDelImg());

			studio.setPhoto(pathList);
			pmsProductionStudioFacade.update(studio);

			return msg;
		}


	/**
	 * 图片上传
	 * 
	 * @param uploadFiles
	 *            待上传图片集合
	 * @param pathList
	 *            已有图片地址
	 * @param delImgStr
	 *            待删除图片
	 * @return
	 */
	private String editFile(final MultipartFile[] uploadFiles, String pathList, String delImgStr) {

		List<String> delImgList = new ArrayList<>();
		if (ValidateUtil.isValid(delImgStr)) {
			String[] delImgs = delImgStr.split(";");
			CollectionUtils.addAll(delImgList, delImgs);
		}

		// 上传图片
		for (int i = 0; i < uploadFiles.length; i++) {
			final MultipartFile multipartFile = uploadFiles[i];
			if (!multipartFile.isEmpty()) {

				if (delImgList.contains(multipartFile.getOriginalFilename())) {
					delImgList.remove(delImgList.indexOf(multipartFile.getOriginalFilename()));
				} else {
					String path = FastDFSClient.uploadFile(multipartFile);
					pathList += path + ";";
				}
			}
		}
		// 删除图片
		for (String delImg : delImgList) {
			if (ValidateUtil.isValid(delImg) && delImg.startsWith("group1/")) {
				FastDFSClient.deleteFile(delImg);
				pathList = StringUtils.remove(pathList, delImg + ";");
			}
		}

		return pathList;
	}
	
	/**
	 * 获取当前登陆人 为数据创建人（e-内部员工）
	 * @param request
	 * @return
	 */
	private String getCreator(final HttpServletRequest request) {
		SessionInfo session=this.getCurrentInfo(request);
		return "e_"+session.getReqiureId();
	}
	
	/**
	 * 设置默认推荐人为当前用户
	 * @param request
	 * @param model
	 */
	private void setDefaultReferrer(final HttpServletRequest request,final ModelMap model) {
		SessionInfo session=this.getCurrentInfo(request);
		model.put("referrer", session.getReqiureId());
	}
	private void setStatusList(final ModelMap model) {
		ProductionConstants[] statusList=ProductionConstants.statusList;
		model.put("statusList", JsonUtil.toJson(statusList));		
	}
	
}
