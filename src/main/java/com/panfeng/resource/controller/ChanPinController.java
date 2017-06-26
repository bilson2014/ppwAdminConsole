package com.panfeng.resource.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.paipianwang.pat.common.entity.BaseEntity;
import com.paipianwang.pat.common.entity.DataGrid;
import com.paipianwang.pat.common.util.FileUtils;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.common.web.file.FastDFSClient;
import com.paipianwang.pat.facade.product.entity.Banner;
import com.paipianwang.pat.facade.product.entity.Feature;
import com.paipianwang.pat.facade.product.entity.PmsChanPin;
import com.paipianwang.pat.facade.product.entity.PmsScene;
import com.paipianwang.pat.facade.product.service.PmsChanPinFacade;
import com.paipianwang.pat.facade.product.service.PmsSceneFacade;
import com.panfeng.domain.BaseMsg;

@RestController
@RequestMapping("/portal")
public class ChanPinController extends BaseController {

	@Autowired
	private PmsChanPinFacade pmsChanPinFacade;

	@Autowired
	private PmsSceneFacade pmsSceneFacade;

	ReentrantLock reentrantLock = new ReentrantLock();

	@RequestMapping("/chanpin-list")
	public ModelAndView activityView() {
		return new ModelAndView("/chanpin-list");
	}

	@RequestMapping("/chanpin/list")
	public DataGrid<PmsChanPin> sceneList() {
		DataGrid<PmsChanPin> allScene = pmsChanPinFacade.getAllChanPin();
		return allScene;
	}
	@RequestMapping("/chanpin/list2")
	public List<PmsChanPin> sceneList2() {
		DataGrid<PmsChanPin> allScene = pmsChanPinFacade.getAllChanPin();
		return allScene.getRows();
	}

	@RequestMapping("/chanpin/save")
	public BaseMsg save(PmsChanPin chanPin, HttpServletRequest request, MultipartFile picimg) {
		if (!picimg.isEmpty()) {
			String uploadFile = FastDFSClient.uploadFile(picimg);
			chanPin.setChanpinPicLDUrl(uploadFile);
		}
		Map<String, Object> save = pmsChanPinFacade.save(chanPin);
		Map<String, String[]> sceneTag = request.getParameterMap();
		String[] tags = sceneTag.get("sceneTag");
		if (tags != null && tags.length > 0) {
			Long id = (Long) save.get(BaseEntity.SAVE_MAP_ID);
			List<PmsScene> list = new ArrayList<>();
			for (int i = 0; i < tags.length; i++) {
				PmsScene ps = new PmsScene();
				ps.setSceneId(Long.valueOf(tags[i]));
				list.add(ps);
			}
			pmsChanPinFacade.updateChanpinScene(id, list);
		}
		BaseMsg baseMsg = new BaseMsg();
		baseMsg.setCode(BaseMsg.NORMAL);
		baseMsg.setResult(save.get(BaseEntity.SAVE_MAP_ROWS));
		return baseMsg;
	}

	@RequestMapping("/chanpin/update")
	public BaseMsg update(PmsChanPin chanpin, HttpServletRequest request, MultipartFile picimg) {
		if (!picimg.isEmpty()) {
			PmsChanPin chanPinInfo = pmsChanPinFacade.getChanPinInfo(chanpin.getChanpinId());
			if (chanPinInfo != null) {
				String chanpinPicLDUrl = chanPinInfo.getChanpinPicLDUrl();
				if (ValidateUtil.isValid(chanpinPicLDUrl)) {
					FastDFSClient.deleteFile(chanpinPicLDUrl);
				}
			}
			String uploadFile = FastDFSClient.uploadFile(picimg);
			chanpin.setChanpinPicLDUrl(uploadFile);
		}
		long update = pmsChanPinFacade.update(chanpin);
		Map<String, String[]> sceneTag = request.getParameterMap();
		String[] tags = sceneTag.get("sceneTag");
		if (tags != null && tags.length > 0) {
			List<PmsScene> list = new ArrayList<>();
			for (int i = 0; i < tags.length; i++) {
				PmsScene ps = new PmsScene();
				ps.setSceneId(Long.valueOf(tags[i]));
				list.add(ps);
			}
			pmsChanPinFacade.updateChanpinScene(chanpin.getChanpinId(), list);
		}
		BaseMsg baseMsg = new BaseMsg();
		baseMsg.setCode(BaseMsg.NORMAL);
		baseMsg.setResult(update);
		return baseMsg;
	}

	@RequestMapping("/chanpin/delete")
	public BaseMsg delete(String ids) {
		BaseMsg baseMsg = new BaseMsg();
		if (ValidateUtil.isValid(ids)) {
			String[] id = ids.split(",");
			for (int i = 0; i < id.length; i++) {
				pmsChanPinFacade.delete(Long.valueOf(id[i]));
			}
			baseMsg.setCode(BaseMsg.NORMAL);

		} else {
			baseMsg.setCode(BaseMsg.ERROR);
			baseMsg.setErrorMsg("id不存在！");
		}
		return baseMsg;
	}

	@RequestMapping("/chanpin/scene/list/{chanpinId}")
	public BaseMsg getScene(@PathVariable("chanpinId") Long chanpinId) {
		BaseMsg baseMsg = new BaseMsg();
		DataGrid<PmsScene> allScene = pmsSceneFacade.getAllScene();
		List<PmsScene> pList = pmsSceneFacade.getSceneByChanPinId(chanpinId);
		if (ValidateUtil.isValid(pList) && allScene != null && ValidateUtil.isValid(allScene.getRows())) {
			for (PmsScene pmsScene : pList) {
				for (PmsScene p : allScene.getRows()) {
					Long pListSceneId = pmsScene.getSceneId();
					Long sceneId = p.getSceneId();
					if (pListSceneId.equals(sceneId)) {
						p.setChecked(true);
						break;
					}
				}
			}
		}
		baseMsg.setErrorCode(BaseMsg.NORMAL);
		baseMsg.setResult(allScene);
		return baseMsg;
	}

	//////////////////////////////////////////////// 封面//////////////////////////////////////////
	/**
	 * 产品封面上传方法。</br>
	 * 客户端使用多图片上传客户端zyUpload,其原理是每次上传一张图片。 此方法用于保存图片
	 * 
	 * @param fileList
	 * @param chapinId
	 * @return
	 */
	@RequestMapping("/chanpin/upload")
	public BaseMsg uploadPicImg(MultipartFile fileList, Long chanpinId) {
		BaseMsg baseMsg = new BaseMsg();
		if (!fileList.isEmpty()) {
			// 图片控件是异步批量上传的 要同步储存数据库
			reentrantLock.lock();
			String fId = FastDFSClient.uploadFile(fileList);
			PmsChanPin chanPinInfo = pmsChanPinFacade.getChanPinInfo(chanpinId);
			if (chanPinInfo != null) {
				LinkedList<Banner> picImgs = chanPinInfo.bannerImgs();
				Banner banner = new Banner();
				if (FileUtils.isMPicture(fileList.getOriginalFilename())) {
					banner.setType(Banner.PHONE);
				} else {
					banner.setType(Banner.WEB);
				}
				banner.setUrl(fId);
				picImgs.addLast(banner);
				chanPinInfo.buildBannerImgs(picImgs);
				pmsChanPinFacade.updateBannerImgs(chanPinInfo);
				baseMsg.setCode(BaseMsg.NORMAL);
			} else {
				baseMsg.setCode(BaseMsg.ERROR);
				baseMsg.setErrorMsg("产品不存在！");
			}
			reentrantLock.unlock();
		}
		return baseMsg;
	}

	@RequestMapping("/chanpin/bannerImgs")
	public DataGrid<Banner> getPicImgs(Long chanpinId) {
		PmsChanPin chanPinInfo = pmsChanPinFacade.getChanPinInfo(chanpinId);
		LinkedList<Banner> picImgs = chanPinInfo.bannerImgs();
		DataGrid<Banner> dd = new DataGrid<>();
		if (ValidateUtil.isValid(picImgs)) {
			dd.setRows(picImgs);
			dd.setTotal(picImgs.size());
			return dd;
		} else {
			return new DataGrid<>(0, new ArrayList<>());
		}
	}

	@RequestMapping("/chanpin/delete/img")
	public BaseMsg deletePicImg(Long chanpinId, String path) {
		BaseMsg baseMsg = new BaseMsg();
		PmsChanPin chanPinInfo = pmsChanPinFacade.getChanPinInfo(chanpinId);
		if(chanPinInfo != null) {
		
		LinkedList<Banner> picImgs = chanPinInfo.bannerImgs();
		
		picImgs.stream().filter(s -> s.getUrl()==null).collect(Collectors.toList());
		Banner b = null;
		for (Banner banner : picImgs) {
			if(banner != null) {
				String url = banner.getUrl();
				if(ValidateUtil.isValid(url)) {
					if (url.equals(path)) {
						b = banner;
						break;
					}
				}
			}
		}
		picImgs.remove(b);
		
		// 判断path 是否为空
		if(ValidateUtil.isValid(path)) {
			// 如果不为空，则删除文件
			FastDFSClient.deleteFile(path);
		}
		chanPinInfo.buildBannerImgs(picImgs);
		pmsChanPinFacade.updateBannerImgs(chanPinInfo);
		baseMsg.setCode(BaseMsg.NORMAL);
		
		} else {
			baseMsg.setCode(BaseMsg.ERROR);
			baseMsg.setErrorMsg("产品不存在！");
		}
		return baseMsg;
	}

	@RequestMapping("/chanpin/saveod")
	public BaseMsg saveOd(Long chanpinId, String paths) {
		BaseMsg baseMsg = new BaseMsg();
		PmsChanPin chanPinInfo = pmsChanPinFacade.getChanPinInfo(chanpinId);
		if (chanPinInfo != null) {
			chanPinInfo.setChanpinBannerUrl(paths);
			pmsChanPinFacade.updateBannerImgs(chanPinInfo);
			baseMsg.setCode(BaseMsg.NORMAL);
		} else {
			baseMsg.setCode(BaseMsg.ERROR);
			baseMsg.setErrorMsg("产品不存在！");
		}
		return baseMsg;
	}

	@RequestMapping("/chanpin/save/feature")
	public BaseMsg saveFeature(Long chanpinId, MultipartFile fileList, Feature feature) {
		PmsChanPin chanPinInfo = pmsChanPinFacade.getChanPinInfo(chanpinId);
		BaseMsg baseMsg = new BaseMsg();
		if (feature != null) {
			if (!fileList.isEmpty()) {
				String fileId = FastDFSClient.uploadFile(fileList);
				feature.setPicHDUrl(fileId);
			}
			feature.setfId(UUID.randomUUID().toString().replaceAll("-", "")); // 生成唯一ID
			LinkedList<Feature> featureList = chanPinInfo.feature();
			if (FileUtils.isMPicture(fileList.getOriginalFilename())) {
				feature.setType(Feature.PHONE);
			} else {
				feature.setType(Feature.WEB);
			}
			featureList.add(feature);
			chanPinInfo.buildFeature(featureList);
			pmsChanPinFacade.update(chanPinInfo);
			baseMsg.setCode(BaseMsg.NORMAL);
			baseMsg.setResult("新建成功！");
		} else {
			baseMsg.setCode(BaseMsg.ERROR);
			baseMsg.setErrorMsg("特性不能为空！");
		}
		return baseMsg;
	}

	@RequestMapping("/chanpin/update/feature")
	public BaseMsg updateFeature(Long chanpinId, MultipartFile fileList, Feature feature) {
		PmsChanPin chanPinInfo = pmsChanPinFacade.getChanPinInfo(chanpinId);
		BaseMsg baseMsg = new BaseMsg();
		if (feature != null) {
			if (FileUtils.isMPicture(fileList.getOriginalFilename())) {
				feature.setType(Feature.PHONE);
			} else {
				feature.setType(Feature.WEB);
			}
			LinkedList<Feature> featureList = chanPinInfo.feature();
			ListIterator<Feature> iterator = featureList.listIterator();
			while (iterator.hasNext()) {
				Feature f = iterator.next();
				if (f.getfId().equals(feature.getfId())) {
					iterator.remove();
					if (!fileList.isEmpty()) {
						String fileId = FastDFSClient.uploadFile(fileList);
						feature.setPicHDUrl(fileId);
					} else {
						feature.setPicHDUrl(f.getPicHDUrl());
					}
					iterator.add(feature);
				}
			}
			chanPinInfo.buildFeature(featureList);
			pmsChanPinFacade.update(chanPinInfo);
			baseMsg.setCode(BaseMsg.NORMAL);
			baseMsg.setResult("更新成功！");
		} else {
			baseMsg.setCode(BaseMsg.ERROR);
			baseMsg.setErrorMsg("特性不能为空！");
		}
		return baseMsg;
	}

	@RequestMapping("/chanpin/feature")
	public DataGrid<Feature> featureList(Long chanpinId) {
		PmsChanPin chanPinInfo = pmsChanPinFacade.getChanPinInfo(chanpinId);
		LinkedList<Feature> feature = chanPinInfo.feature();
		DataGrid<Feature> dataGrid = new DataGrid<>();
		if (ValidateUtil.isValid(feature)) {
			dataGrid.setRows(feature);
			dataGrid.setTotal(feature.size());
		} else {
			dataGrid.setRows(new ArrayList<>());
			dataGrid.setTotal(0);
		}
		return dataGrid;
	}

	@RequestMapping("/chanpin/delete/feature")
	public BaseMsg deleteFeature(Long chanpinId, String fId) {
		BaseMsg baseMsg = new BaseMsg();
		PmsChanPin chanPinInfo = pmsChanPinFacade.getChanPinInfo(chanpinId);
		if (chanPinInfo != null) {
			LinkedList<Feature> featureList = chanPinInfo.feature();
			Iterator<Feature> iterator = featureList.iterator();
			while (iterator.hasNext()) {
				Feature feature = iterator.next();
				if (feature.getfId().equals(fId)) {
					if (ValidateUtil.isValid(feature.getPicHDUrl())) {
						FastDFSClient.deleteFile(feature.getPicHDUrl());
					}
					iterator.remove();
					break;
				}
			}
			chanPinInfo.buildFeature(featureList);
			pmsChanPinFacade.update(chanPinInfo);
			baseMsg.setCode(BaseMsg.NORMAL);
			baseMsg.setResult("删除成功！");
		} else {
			baseMsg.setCode(BaseMsg.ERROR);
			baseMsg.setErrorMsg("产品不存在！");
		}
		return baseMsg;
	}

	@RequestMapping("/chanpin/saveFeatureOd")
	public BaseMsg saveFeatureOd(Long chanpinId, String features) {
		BaseMsg baseMsg = new BaseMsg();
		PmsChanPin chanPinInfo = pmsChanPinFacade.getChanPinInfo(chanpinId);
		if (chanPinInfo != null) {
			chanPinInfo.setChanpinFeature(features);
			pmsChanPinFacade.update(chanPinInfo);
			baseMsg.setCode(BaseMsg.NORMAL);
			baseMsg.setResult("更新成功！");
		} else {
			baseMsg.setCode(BaseMsg.ERROR);
			baseMsg.setErrorMsg("产品不存在！");
		}
		return baseMsg;
	}
	
}
