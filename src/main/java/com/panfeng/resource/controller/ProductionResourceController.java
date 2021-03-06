package com.panfeng.resource.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.paipianwang.pat.common.entity.DataGrid;
import com.paipianwang.pat.common.entity.PageParam;
import com.paipianwang.pat.common.entity.PhotoCutParam;
import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.common.util.DateUtils;
import com.paipianwang.pat.common.util.FileUtils;
import com.paipianwang.pat.common.util.JsonUtil;
import com.paipianwang.pat.common.util.PhotoUtil;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.common.web.exception.WebException;
import com.paipianwang.pat.common.web.file.FastDFSClient;
import com.paipianwang.pat.workflow.entity.PmsProductionActor;
import com.paipianwang.pat.workflow.entity.PmsProductionCameraman;
import com.paipianwang.pat.workflow.entity.PmsProductionCostume;
import com.paipianwang.pat.workflow.entity.PmsProductionDevice;
import com.paipianwang.pat.workflow.entity.PmsProductionDirector;
import com.paipianwang.pat.workflow.entity.PmsProductionPersonnel;
import com.paipianwang.pat.workflow.entity.PmsProductionStudio;
import com.paipianwang.pat.workflow.entity.ProductionConstants;
import com.paipianwang.pat.workflow.enums.ProductionResource;
import com.paipianwang.pat.workflow.facade.PmsProductionActorFacade;
import com.paipianwang.pat.workflow.facade.PmsProductionCameramanFacade;
import com.paipianwang.pat.workflow.facade.PmsProductionCostumeFacade;
import com.paipianwang.pat.workflow.facade.PmsProductionDeviceFacade;
import com.paipianwang.pat.workflow.facade.PmsProductionDirectorFacade;
import com.paipianwang.pat.workflow.facade.PmsProductionPersonnelFacade;
import com.paipianwang.pat.workflow.facade.PmsProductionStudioFacade;
import com.panfeng.domain.BaseMsg;
import com.panfeng.util.Log;

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
	@Autowired
	private PmsProductionPersonnelFacade pmsProductionPersonnelFacade;
	@Autowired
	private PmsProductionCameramanFacade pmsProductionCameramanFacade;
	@Autowired
	private PmsProductionCostumeFacade pmsProductionCostumeFacade;

	// --------------------演员--------------------------
	@RequestMapping(value = "/production/actor-list")
	public ModelAndView actorView(final HttpServletRequest request,final ModelMap model) {
		setPageParameter(request, model);
		return new ModelAndView("production/production-actor-list", model);
	}

	@RequestMapping(value = "/production/actor/list", method = RequestMethod.POST)
	public DataGrid<PmsProductionActor> actorList(final HttpServletRequest request,@RequestParam final Map<String, Object> paramMap,
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
		setDataLevel(request, paramMap);

		final DataGrid<PmsProductionActor> dataGrid = pmsProductionActorFacade.listWithPagination(param, paramMap);
		return dataGrid;
	}

	@RequestMapping(value = "/production/actor/delete", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public void actorDelete(final long[] ids, HttpServletRequest request) {		
		//TODO 是否考虑审核不通过的真实删除包括图片；审核通过的做逻辑删除，图片保留
		List<PmsProductionActor> deletes=pmsProductionActorFacade.deleteByIds(ids);
		//删除图片  
		if(ValidateUtil.isValid(deletes)) {
			for(PmsProductionActor actor:deletes) {
				String delImgStr=actor.getPhoto();
				if(actor.getStatus()!=1 && ValidateUtil.isValid(delImgStr)) {
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
			final PmsProductionActor actor) throws Exception {
		BaseMsg msg = new BaseMsg();

		String pathList = actor.getPhoto();
		pathList = editFile(null, pathList, actor.getDelImg());

		actor.setPhoto(pathList);
		actor.setCreator(getCreator(request));
		pmsProductionActorFacade.insert(actor);
		// TODO 或者是先添加，再更新照片

		return msg;
	}

	@RequestMapping(value = "/production/actor/update", method = RequestMethod.POST)
	public BaseMsg actorUpdate(final HttpServletRequest request, final HttpServletResponse response,
			final PmsProductionActor actor) throws Exception {
		BaseMsg msg = new BaseMsg();

		String pathList = actor.getPhoto();
		// 上传图片
		pathList = editFile(null, pathList, actor.getDelImg());

		actor.setPhoto(pathList);
		pmsProductionActorFacade.update(actor);

		return msg;
	}

	// --------------------设备--------------------------
	@RequestMapping(value = "/production/device-list")
	public ModelAndView deviceView(final HttpServletRequest request,final ModelMap model) {
		setPageParameter(request, model);
		//设备类型
		/*ProductionDeviceType[] types=ProductionDeviceType.values();		
		List<Map<String,Object>> typesList=new ArrayList<>();
		for(ProductionDeviceType type:types) {
			Map<String,Object> map=new HashMap<>();
			map.put("key",type.getKey());
			map.put("name",type.getName());
			map.put("quotationType",type.getQuotationType());
			typesList.add(map);
		}
		
		model.put("typeList", JSONArray.toJSON(typesList));*/
		
		return new ModelAndView("production/production-device-list", model);
	}
	

	@RequestMapping(value = "/production/device/list", method = RequestMethod.POST)
	public DataGrid<PmsProductionDevice> deviceList(final HttpServletRequest request,@RequestParam final Map<String, Object> paramMap,
			final PageParam param) {

		final long page = param.getPage();
		final long rows = param.getRows();
		param.setBegin((page - 1) * rows);
		param.setLimit(rows);

		paramMap.remove("page");
		paramMap.remove("rows");

		setDataLevel(request, paramMap);
		final DataGrid<PmsProductionDevice> dataGrid = pmsProductionDeviceFacade.listWithPagination(param, paramMap);
		return dataGrid;
	}

	@RequestMapping(value = "/production/device/delete", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public long deviceDelete(final long[] ids, HttpServletRequest request) {
		return pmsProductionDeviceFacade.deleteByIds(ids);
	}

	@RequestMapping(value = "/production/device/save", method = RequestMethod.POST)
	public BaseMsg deviceSave(final HttpServletRequest request, final HttpServletResponse response,
			final PmsProductionDevice device) throws Exception {
		BaseMsg msg = new BaseMsg();
		device.setCreator(getCreator(request));
		pmsProductionDeviceFacade.insert(device);
		return msg;
	}

	@RequestMapping(value = "/production/device/update", method = RequestMethod.POST)
	public BaseMsg deviceUpdate(final HttpServletRequest request, final HttpServletResponse response,
			 final PmsProductionDevice device) throws Exception {
		BaseMsg msg = new BaseMsg();

		pmsProductionDeviceFacade.update(device);

		return msg;
	}
	
	// --------------------导演--------------------------
		@RequestMapping(value = "/production/director-list")
		public ModelAndView directorView(final HttpServletRequest request,final ModelMap model) {
			setPageParameter(request, model);
			
			ProductionConstants[] specialtyList=ProductionConstants.specialtyList;
			model.put("specialtyList", JSONArray.toJSON(specialtyList));
			
			return new ModelAndView("production/production-director-list", model);
		}

		@RequestMapping(value = "/production/director/list", method = RequestMethod.POST)
		public DataGrid<PmsProductionDirector> directorList(final HttpServletRequest request,@RequestParam final Map<String, Object> paramMap,
				final PageParam param) {

			final long page = param.getPage();
			final long rows = param.getRows();
			param.setBegin((page - 1) * rows);
			param.setLimit(rows);

			paramMap.remove("page");
			paramMap.remove("rows");

			setDataLevel(request, paramMap);
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
					if(director.getStatus()!=1 && ValidateUtil.isValid(delImgStr)) {
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
				final PmsProductionDirector director) throws Exception {
			BaseMsg msg = new BaseMsg();
			String pathList = director.getPhoto();
			pathList = editFile(null, pathList, director.getDelImg());

			director.setPhoto(pathList);
			director.setCreator(getCreator(request));
			pmsProductionDirectorFacade.insert(director);
			return msg;
		}

		@RequestMapping(value = "/production/director/update", method = RequestMethod.POST)
		public BaseMsg directorUpdate(final HttpServletRequest request, final HttpServletResponse response,
				final PmsProductionDirector director) throws Exception {
			BaseMsg msg = new BaseMsg();

			String pathList = director.getPhoto();
			// 上传图片
			pathList = editFile(null, pathList, director.getDelImg());

			director.setPhoto(pathList);
			
			pmsProductionDirectorFacade.update(director);

			return msg;
		}
		
		
	// --------------------场地--------------------------
	@RequestMapping(value = "/production/studio-list")
	public ModelAndView studioView(final HttpServletRequest request, final ModelMap model) {
		setPageParameter(request, model);
		return new ModelAndView("production/production-studio-list", model);
	}

	@RequestMapping(value = "/production/studio/list", method = RequestMethod.POST)
	public DataGrid<PmsProductionStudio> studioList(final HttpServletRequest request,@RequestParam final Map<String, Object> paramMap,
			final PageParam param) {

		final long page = param.getPage();
		final long rows = param.getRows();
		param.setBegin((page - 1) * rows);
		param.setLimit(rows);

		paramMap.remove("page");
		paramMap.remove("rows");

		setDataLevel(request, paramMap);
		final DataGrid<PmsProductionStudio> dataGrid = pmsProductionStudioFacade.listWithPagination(param, paramMap);
		return dataGrid;
	}

	@RequestMapping(value = "/production/studio/delete", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public void studioDelete(final long[] ids, HttpServletRequest request) {
		List<PmsProductionStudio> deletes = pmsProductionStudioFacade.deleteByIds(ids);
		// 删除图片
		if (ValidateUtil.isValid(deletes)) {
			for (PmsProductionStudio studio : deletes) {
				String delImgStr = studio.getPhoto();
				if (studio.getStatus()!=1 && ValidateUtil.isValid(delImgStr)) {
					String[] delImgs = delImgStr.split(";");
					for (String delImg : delImgs) {
						FastDFSClient.deleteFile(delImg);
					}
				}
			}
		}
	}

	@RequestMapping(value = "/production/studio/save", method = RequestMethod.POST)
	public BaseMsg studioSave(final HttpServletRequest request, final HttpServletResponse response,
			final PmsProductionStudio studio) throws Exception {
		BaseMsg msg = new BaseMsg();

		String pathList = studio.getPhoto();
		pathList = editFile(null, pathList, studio.getDelImg());

		studio.setPhoto(pathList);
		studio.setCreator(getCreator(request));
		pmsProductionStudioFacade.insert(studio);
		// TODO 或者是先添加，再更新照片

		return msg;
	}

	@RequestMapping(value = "/production/studio/update", method = RequestMethod.POST)
	public BaseMsg studioUpdate(final HttpServletRequest request, final HttpServletResponse response,
			final PmsProductionStudio studio) throws Exception {
		BaseMsg msg = new BaseMsg();

		String pathList = studio.getPhoto();
		// 上传图片
		pathList = editFile(null, pathList, studio.getDelImg());

		studio.setPhoto(pathList);
		pmsProductionStudioFacade.update(studio);

		return msg;
	}

	// --------------------人员--------------------------
	@RequestMapping(value = "/production/{position}-list")
	public ModelAndView personnelView(final HttpServletRequest request, final ModelMap model,@PathVariable("position") final String position) {
		setPageParameter(request, model);
		
		ProductionResource resource=ProductionResource.getEnum(position);
		if(resource==null) {
			//全部加上职位校验
			throw new WebException("职位不存在", "001");
		}
		
		model.put("position",position);
		model.put("positionName", resource.getName());
		return new ModelAndView("production/production-personnel-list", model);
	}

	@RequestMapping(value = "/production/{position}/list", method = RequestMethod.POST)
	public DataGrid<PmsProductionPersonnel> personnelList(final HttpServletRequest request,@RequestParam final Map<String, Object> paramMap,
			final PageParam param,@PathVariable("position") final String position) {
		
		ProductionResource resource=ProductionResource.getEnum(position);
		if(resource==null) {
			//全部加上职位校验
			throw new WebException("职位不存在", "001");
		}

		final long page = param.getPage();
		final long rows = param.getRows();
		param.setBegin((page - 1) * rows);
		param.setLimit(rows);

		paramMap.remove("page");
		paramMap.remove("rows");
		
		paramMap.put("profession",position);
		
		setDataLevel(request, paramMap);

		final DataGrid<PmsProductionPersonnel> dataGrid = pmsProductionPersonnelFacade.listWithPagination(param, paramMap);
		return dataGrid;
	}

	@RequestMapping(value = "/production/{position}/delete", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public BaseMsg personnelDelete(final long[] ids, HttpServletRequest request,@PathVariable("position") final String position) {
		BaseMsg msg=new BaseMsg();
		ProductionResource resource=ProductionResource.getEnum(position);
		if(resource==null) {
			//全部加上职位校验
			msg.setErrorMsg("职位不存在");
			return msg;
		}
		
		List<PmsProductionPersonnel> deletes = pmsProductionPersonnelFacade.deleteByIds(ids);
		// 删除图片
		if (ValidateUtil.isValid(deletes)) {
			for (PmsProductionPersonnel personnel : deletes) {
				String delImgStr = personnel.getPhoto();
				if (personnel.getStatus()!=1 && ValidateUtil.isValid(delImgStr)) {
					String[] delImgs = delImgStr.split(";");
					for (String delImg : delImgs) {
						FastDFSClient.deleteFile(delImg);
					}
				}
			}
		}
		msg.setCode(BaseMsg.NORMAL);
		
		return msg;
	}

	@RequestMapping(value = "/production/{position}/save", method = RequestMethod.POST)
	public BaseMsg personnelSave(final HttpServletRequest request, final HttpServletResponse response,
			final PmsProductionPersonnel personnel,@PathVariable("position") final String position) throws Exception {
		BaseMsg msg = new BaseMsg();
		ProductionResource resource=ProductionResource.getEnum(position);
		if(resource==null) {
			//全部加上职位校验
			msg.setErrorMsg("职位不存在");
			return msg;
		}

		String pathList = personnel.getPhoto();
		pathList = editFile(null, pathList, personnel.getDelImg());

		personnel.setPhoto(pathList);
		personnel.setCreator(getCreator(request));
		personnel.setProfession(position);
		pmsProductionPersonnelFacade.insert(personnel);
		return msg;
	}

	@RequestMapping(value = "/production/{position}/update", method = RequestMethod.POST)
	public BaseMsg personnelUpdate(final HttpServletRequest request, final HttpServletResponse response,
			final PmsProductionPersonnel personnel,@PathVariable("position") final String position) throws Exception {
		BaseMsg msg = new BaseMsg();
		ProductionResource resource=ProductionResource.getEnum(position);
		if(resource==null) {
			//全部加上职位校验
			msg.setErrorMsg("职位不存在");
			return msg;
		}

		String pathList = personnel.getPhoto();
		// 上传图片
		pathList = editFile(null, pathList, personnel.getDelImg());

		personnel.setPhoto(pathList);
		personnel.setProfession(position);
		pmsProductionPersonnelFacade.update(personnel);

		return msg;
	}

	
	// --------------------摄影师--------------------------
		@RequestMapping(value = "/production/cameraman-list")
		public ModelAndView cameramanView(final HttpServletRequest request, final ModelMap model) {
			setPageParameter(request, model);
			return new ModelAndView("production/production-cameraman-list", model);
		}

		@RequestMapping(value = "/production/cameraman/list", method = RequestMethod.POST)
		public DataGrid<PmsProductionCameraman> cameramanList(final HttpServletRequest request,@RequestParam final Map<String, Object> paramMap,
				final PageParam param) {

			final long page = param.getPage();
			final long rows = param.getRows();
			param.setBegin((page - 1) * rows);
			param.setLimit(rows);

			paramMap.remove("page");
			paramMap.remove("rows");
			
			setDataLevel(request, paramMap);

			final DataGrid<PmsProductionCameraman> dataGrid = pmsProductionCameramanFacade.listWithPagination(param, paramMap);
			return dataGrid;
		}

		@RequestMapping(value = "/production/cameraman/delete", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
		public void cameramanDelete(final long[] ids, HttpServletRequest request) {
			List<PmsProductionCameraman> deletes = pmsProductionCameramanFacade.deleteByIds(ids);
			// 删除图片
			if (ValidateUtil.isValid(deletes)) {
				for (PmsProductionCameraman cameraman : deletes) {
					String delImgStr = cameraman.getPhoto();
					if (cameraman.getStatus()!=1 && ValidateUtil.isValid(delImgStr)) {
						String[] delImgs = delImgStr.split(";");
						for (String delImg : delImgs) {
							FastDFSClient.deleteFile(delImg);
						}
					}
				}
			}
		}

		@RequestMapping(value = "/production/cameraman/save", method = RequestMethod.POST)
		public BaseMsg cameramanSave(final HttpServletRequest request, final HttpServletResponse response,
				final PmsProductionCameraman cameraman) throws Exception {
			BaseMsg msg = new BaseMsg();

			String pathList = cameraman.getPhoto();
			pathList = editFile(null, pathList, cameraman.getDelImg());

			cameraman.setPhoto(pathList);
			cameraman.setCreator(getCreator(request));
			pmsProductionCameramanFacade.insert(cameraman);
			return msg;
		}

		@RequestMapping(value = "/production/cameraman/update", method = RequestMethod.POST)
		public BaseMsg cameramanUpdate(final HttpServletRequest request, final HttpServletResponse response,
				final PmsProductionCameraman cameraman) throws Exception {
			BaseMsg msg = new BaseMsg();

			String pathList = cameraman.getPhoto();
			// 上传图片
			pathList = editFile(null, pathList, cameraman.getDelImg());

			cameraman.setPhoto(pathList);
			pmsProductionCameramanFacade.update(cameraman);

			return msg;
		}
		
		// --------------------服装道具--------------------------
		@RequestMapping(value = "/production/costume-{nature}-list")
		public ModelAndView costumeView(final HttpServletRequest request, final ModelMap model,@PathVariable("nature") final String nature) {
			setPageParameter(request, model);
			
			ProductionResource resource=ProductionResource.getEnum(nature);
			if(resource==null) {
				//全部加上职位校验
				throw new WebException("请求不存在", "001");
			}
			
			model.put("nature",nature);
			model.put("natureName", resource.getName());
			
			ProductionConstants[] clothingTypeList=ProductionConstants.clothingTypeList;
			model.put("clothingTypeList", JsonUtil.toJson(clothingTypeList));
			
			ProductionConstants[] accreditList=ProductionConstants.accreditList;
			model.put("accreditList", JsonUtil.toJson(accreditList));
			
			ProductionConstants[] propsTypeList=ProductionConstants.propsTypeList;
			model.put("propsTypeList", JsonUtil.toJson(propsTypeList));
			
			return new ModelAndView("production/production-costume-list", model);
		}

		@RequestMapping(value = "/production/costume-{nature}/list", method = RequestMethod.POST)
		public DataGrid<PmsProductionCostume> costumeList(final HttpServletRequest request,@RequestParam final Map<String, Object> paramMap,
				final PageParam param,@PathVariable("nature") final String nature) {
			
			ProductionResource resource=ProductionResource.getEnum(nature);
			if(resource==null) {
				//全部加上职位校验
				throw new WebException("请求不存在", "001");
			}

			final long page = param.getPage();
			final long rows = param.getRows();
			param.setBegin((page - 1) * rows);
			param.setLimit(rows);

			paramMap.remove("page");
			paramMap.remove("rows");
			
			paramMap.put("nature",nature);
			
			setDataLevel(request, paramMap);

			final DataGrid<PmsProductionCostume> dataGrid = pmsProductionCostumeFacade.listWithPagination(param, paramMap);
			return dataGrid;
		}

		@RequestMapping(value = "/production/costume-{nature}/delete", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
		public BaseMsg costumeDelete(final long[] ids, HttpServletRequest request,@PathVariable("nature") final String nature) {
			BaseMsg msg=new BaseMsg();
			ProductionResource resource=ProductionResource.getEnum(nature);
			if(resource==null) {
				//全部加上职位校验
				msg.setErrorMsg("请求不存在");
				return msg;
			}
			
			List<PmsProductionCostume> deletes = pmsProductionCostumeFacade.deleteByIds(ids);
			// 删除图片
			if (ValidateUtil.isValid(deletes)) {
				for (PmsProductionCostume costume : deletes) {
					String delImgStr = costume.getPhoto();
					if (costume.getStatus()!=1 && ValidateUtil.isValid(delImgStr)) {
						String[] delImgs = delImgStr.split(";");
						for (String delImg : delImgs) {
							FastDFSClient.deleteFile(delImg);
						}
					}
				}
			}
			msg.setCode(BaseMsg.NORMAL);
			
			return msg;
		}

		@RequestMapping(value = "/production/costume-{nature}/save", method = RequestMethod.POST)
		public BaseMsg costumeSave(final HttpServletRequest request, final HttpServletResponse response,
				final PmsProductionCostume costume,@PathVariable("nature") final String nature) throws Exception {
			BaseMsg msg = new BaseMsg();
			ProductionResource resource=ProductionResource.getEnum(nature);
			if(resource==null) {
				//全部加上职位校验
				msg.setErrorMsg("请求不存在");
				return msg;
			}

			String pathList = costume.getPhoto();
			pathList = editFile(null, pathList, costume.getDelImg());

			costume.setPhoto(pathList);
			costume.setCreator(getCreator(request));
			costume.setNature(nature);
			pmsProductionCostumeFacade.insert(costume);
			return msg;
		}

		@RequestMapping(value = "/production/costume-{nature}/update", method = RequestMethod.POST)
		public BaseMsg costumeUpdate(final HttpServletRequest request, final HttpServletResponse response,
				final PmsProductionCostume costume,@PathVariable("nature") final String nature) throws Exception {
			BaseMsg msg = new BaseMsg();
			ProductionResource resource=ProductionResource.getEnum(nature);
			if(resource==null) {
				//全部加上职位校验
				msg.setErrorMsg("请求不存在");
				return msg;
			}

			String pathList = costume.getPhoto();
			// 上传图片
			pathList = editFile(null, pathList, costume.getDelImg());

			costume.setPhoto(pathList);
			costume.setNature(nature);
			pmsProductionCostumeFacade.update(costume);

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
		if(uploadFiles!=null && uploadFiles.length>0) {
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
	 * 设置页面公共参数：默认推荐人、审核状态、数据等级
	 * @param request
	 * @param model
	 */
	private void setPageParameter(final HttpServletRequest request,final ModelMap model) {
		//设置默认推荐人
		SessionInfo session=this.getCurrentInfo(request);
		model.put("referrer", session.getReqiureId());
		model.put("referrer_name", session.getRealName());
		
		//设置审核状态
		ProductionConstants[] statusList=ProductionConstants.statusList;
		model.put("statusList", JsonUtil.toJson(statusList));
		
		//设置数据等级
		if(session.isSuperAdmin() || session.getUserRank()==1) {//管理员、联合创始人
			model.put("dataLevel","1");
		}else {
			model.put("dataLevel","0");
		}
		
	}
/*	private void setStatusList(final ModelMap model) {
		ProductionConstants[] statusList=ProductionConstants.statusList;
		model.put("statusList", JsonUtil.toJson(statusList));		
	}
	
	private void setPageDataLevel(final HttpServletRequest request,final ModelMap model) {
		SessionInfo session=this.getCurrentInfo(request);
		if(session.isSuperAdmin()) {//TODO || session.is联合创始人
			model.put("dataLevel","1");
		}else {
			model.put("dataLevel","0");
		}
	}*/
	
	private void setDataLevel(final HttpServletRequest request,Map<String, Object> paramMap) {
		SessionInfo session=this.getCurrentInfo(request);
		if(session.isSuperAdmin() || session.getUserRank()==1) {//管理员、联合创始人
			paramMap.put("dataLevel", 1);
		}else {
			paramMap.put("current_user",session.getReqiureId());
			paramMap.put("dataLevel", 0);
		}
	}
	
	@RequestMapping("/production/cutPhoto")
	public BaseMsg uploadAndCutImg(final HttpServletRequest request,@RequestParam MultipartFile uploadFile, final PhotoCutParam param )
			throws Exception {
		BaseMsg result=new BaseMsg();
		if (param != null && !"".equals(param.getImgUrl())) {
			
			if(uploadFile.isEmpty()) {
				return result;
			}

			final String imgPath = param.getImgUrl();
			InputStream inputStream =null;
			String extName = null;
			
			if(ValidateUtil.isValid(imgPath)) {
				inputStream = FastDFSClient.downloadFile(imgPath);
				extName = FileUtils.getExtName(imgPath, ".");
			}else {
				inputStream=uploadFile.getInputStream();
				extName=uploadFile.getOriginalFilename();
			}

			SessionInfo sessionInfo = getCurrentInfo(request);
			Log.error(" cut photo begin", sessionInfo);

			// cut photo
			inputStream = PhotoUtil.cutPhoto(inputStream, param, FileUtils.getExtName(extName, "."));
			Log.error(" cut photo - success", sessionInfo);
			
			String path = FastDFSClient.uploadFile(inputStream, extName);
			
			if(ValidateUtil.isValid(path)) {
				result.setCode(BaseMsg.NORMAL);
				result.setResult(path);
			}			
		}
		return result;
	}
	
	
}
