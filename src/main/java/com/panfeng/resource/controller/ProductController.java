package com.panfeng.resource.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.paipianwang.pat.common.entity.DataGrid;
import com.paipianwang.pat.common.entity.PageParam;
import com.paipianwang.pat.facade.product.entity.PmsProduct;
import com.paipianwang.pat.facade.product.entity.PmsService;
import com.paipianwang.pat.facade.product.service.PmsProductFacade;
import com.paipianwang.pat.facade.team.entity.PmsTeam;
import com.paipianwang.pat.facade.team.service.PmsTeamFacade;
import com.panfeng.domain.BaseMsg;
import com.panfeng.domain.GlobalConstant;
import com.panfeng.domain.SessionInfo;
import com.panfeng.mq.service.FileConvertMQService;
import com.panfeng.resource.model.Product;
import com.panfeng.resource.model.Service;
import com.panfeng.resource.model.Solr;
import com.panfeng.resource.view.ProductView;
import com.panfeng.resource.view.SolrView;
import com.panfeng.service.FDFSService;
import com.panfeng.service.ProductService;
import com.panfeng.service.ServiceService;
import com.panfeng.service.SolrService;
import com.panfeng.util.JsoupUtil;
import com.panfeng.util.Log;
import com.panfeng.util.ValidateUtil;

/**
 * 产品管理类
 * 
 * @author GY
 */
@RestController
@RequestMapping("/portal")
public class ProductController extends BaseController {

	@Autowired
	private final ProductService proService = null;

	@Autowired
	private final ServiceService serService = null;

	@Autowired
	private SolrService solrService = null;

	@Autowired
	private final FDFSService fdfsService = null;

	@Autowired
	private final FileConvertMQService fileConvertMQService = null;
	
	@Autowired
	private final PmsProductFacade pmsProductFacade = null;
	@Autowired
	private final PmsTeamFacade pmsTeamFacade = null;
	

	private static String PRODUCT_VIDEO_PATH = null; // video文件路径

	private static String SOLR_URL = null;
	private static String SOLR_EMPLOYEE_URL = null;
	private static String SOLR_PORTAL_URL = null;

	public ProductController() {
		if (PRODUCT_VIDEO_PATH == null || "".equals(PRODUCT_VIDEO_PATH)) {
			final InputStream is = this.getClass().getClassLoader().getResourceAsStream("jdbc.properties");
			try {
				Properties propertis = new Properties();
				propertis.load(is);
				PRODUCT_VIDEO_PATH = propertis.getProperty("upload.server.product.video");
				SOLR_URL = propertis.getProperty("solr.url");
				SOLR_EMPLOYEE_URL = propertis.getProperty("solr.employee.url");
				SOLR_PORTAL_URL = propertis.getProperty("solr.portal.url");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@RequestMapping(value = "/product-list")
	public ModelAndView view(final ModelMap model) {
		return new ModelAndView("product-list", model);
	}

	@RequestMapping(value = "/product/init", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public List<PmsTeam> init() {
		//final List<Team> list = teamService.getAll();
		final List<PmsTeam> list = pmsTeamFacade.getAll();
		return list;
	}

	/**
	 * 分页检索
	 * 
	 * @param view
	 *            product-条件视图
	 */
	@RequestMapping(value = "/product/list", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public DataGrid<PmsProduct> list(final ProductView view, final PageParam param) {

		final long page = param.getPage();
		final long rows = param.getRows();
		param.setBegin((page - 1) * rows);
		param.setLimit(rows);
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("teamName", view.getTeamName());
		paramMap.put("productName", view.getProductName());
		paramMap.put("beginTime", view.getBeginTime());
		paramMap.put("endTime", view.getEndTime());
		paramMap.put("flag", view.getFlag());
		paramMap.put("visible", view.getVisible());
		paramMap.put("hret", view.getHret());
		paramMap.put("recommend", view.getRecommend());
		final DataGrid<PmsProduct> dataGrid = pmsProductFacade.listWithPagination(param,paramMap);
		return dataGrid;
	}

	/**
	 * 删除
	 * 
	 * @param ids
	 *            product id array
	 */
	@RequestMapping(value = "/product/delete", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public long delete(final long[] ids, HttpServletRequest request) {

		if (ids.length > 0) {
			//-> modify to dubbo 2017-2-3 13:59:38 begin
			final List<PmsProduct> list = pmsProductFacade.delete(ids);
			//-> modify to dubbo 2017-2-3 13:59:38 end
			// delete file
			if (!list.isEmpty() && list.size() > 0) {

				for (final PmsProduct product : list) {
					// 删除 缩略图
					final String picLDUrl = product.getPicLDUrl();
					fdfsService.delete(picLDUrl);
					// 删除 高清图，如果删除的是以前的作品的话，高清图是存在的
					final String picHDUrl = product.getPicHDUrl();
					fdfsService.delete(picHDUrl);

					// 删除 视频
					final String videoUrl = product.getVideoUrl();
					fdfsService.delete(videoUrl);
					// 待修改成分解富文本编辑器，删除图片
					final String description = product.getVideoDescription();
					if (ValidateUtil.isValid(description)) {
						List<String> imgList = JsoupUtil.getImgSrc(description);
						for (String s : imgList) {
							fdfsService.delete(s);
						}
					}
					// 删除搜索索引
					solrService.deleteDoc(product.getProductId(), SOLR_URL);
				}
			} else {
				throw new RuntimeException("Product ids is null");
			}
			SessionInfo sessionInfo = getCurrentInfo(request);
			Log.error("delete products ... ids:" + ids.toString(), sessionInfo);
		}
		return 0l;
	}

	@RequestMapping(value = "/product/save", method = RequestMethod.POST)
	public void save(final HttpServletRequest request, final HttpServletResponse response,
			@RequestParam final MultipartFile[] uploadFiles, final PmsProduct product) {
		response.setContentType("text/html;charset=UTF-8");
		// 保存 product
		long ProductId = pmsProductFacade.save(product);
		product.setProductId(ProductId);
		// 路径接收
		final List<String> pathList = new ArrayList<String>();
		for (int i = 0; i < uploadFiles.length; i++) {
			final MultipartFile multipartFile = uploadFiles[i];
			if (!multipartFile.isEmpty()) {
				String path = fdfsService.upload(multipartFile);
				pathList.add(path);
			} else {
				pathList.add("");
			}
		}
		product.setVideoUrl(pathList.get(0));
		product.setPicLDUrl(pathList.get(1));
		// 保存路径
		pmsProductFacade.saveFileUrl(product);
		// 增加审核通过时，创建service数据
		if (product.getFlag() == 1) {
			final double servicePrice = product.getServicePrice(); // 保存服务信息
			PmsService service = new PmsService();
			service.setProductId(product.getProductId());
			service.setProductName(product.getProductName());
			service.setServiceDiscount(1);
			service.setServiceName("service" + product.getProductId() + "-" + product.getProductName());
			service.setServiceOd(0);
			service.setServicePrice(servicePrice);
			service.setServiceRealPrice(servicePrice);
			service.setMcoms(Long.parseLong(product.getVideoLength()));
			pmsProductFacade.save(service);
			SessionInfo sessionInfo = getCurrentInfo(request);
			Log.error("add product ... ", sessionInfo);
		}
		// 加入文件转换队列
		fileConvertMQService.sendMessage(product.getProductId(), product.getVideoUrl());

	}

	@RequestMapping(value = "/product/update", method = RequestMethod.POST)
	public void update(final HttpServletRequest request, final HttpServletResponse response,
			@RequestParam final MultipartFile[] uploadFiles, final PmsProduct product) {

		final long productId = product.getProductId(); // product id
		//->2017-2-3 13:27:15 modify to dubbo begin
		//final Product originalProduct = proService.findProductById(productId);
		final PmsProduct originalProduct = pmsProductFacade.findProductById(productId);
		//->2017-2-3 13:27:15 modify to dubbo end

		// 获取未更改前的product对象,用于删除修改过的文件
		final List<String> pathList = new ArrayList<String>(); // 路径集合
		try {
			for (int i = 0; i < uploadFiles.length; i++) {
				final MultipartFile multipartFile = uploadFiles[i];
				if (!multipartFile.isEmpty()) {
					String path = fdfsService.upload(multipartFile);
					pathList.add(path);
				} else {
					// file字段 如果为空,说明 未上传新文件
					pathList.add("");
				}
			}
			product.setVideoUrl(pathList.get(0));
			product.setPicLDUrl(pathList.get(1));
			//->2017-2-3 13:27:15 modify to dubbo begin
			//proService.update(product);
			pmsProductFacade.update(product);
			//->2017-2-3 13:27:15 modify to dubbo end
			// 增加审核通过时，创建service数据
			List<Service> list = serService.loadService((int) product.getProductId());
			if (product.getFlag() == 1) {
				if (null == list || list.size() == 0) {
					PmsService service = new PmsService();
					service.setProductId(product.getProductId());
					service.setProductName(product.getProductName());
					service.setServiceDiscount(1);
					service.setServiceName("service" + product.getProductId() + "-" + product.getProductName());
					service.setServiceOd(0);
					service.setServicePrice(0d);
					service.setServiceRealPrice(0d);
					service.setMcoms(Long.parseLong(product.getVideoLength()));
					pmsProductFacade.save(service);
				}
			}
			if (originalProduct != null) {
				// 删除 原文件
				for (int i = 0; i < pathList.size(); i++) {
					final String newPath = pathList.get(i);
					if (newPath != null && !"".equals(newPath)) {
						// 发生更改，删除原文件
						String path = "";
						switch (i) {
						case 0: // videoFile
							path = originalProduct.getVideoUrl();
							break;
						case 1: // picLDFile
							path = originalProduct.getPicLDUrl();
							break;
						default:
							continue;
						}
						if (path != null && !"".equals(path)) {
							fdfsService.delete(path);
							// 如果视频更新的话，需要将新视频添加到转换队列
							if (i == 0)
								fileConvertMQService.sendMessage(originalProduct.getProductId(), pathList.get(0));
						}
					}
				}
			}
		} catch (Exception e) {
			SessionInfo sessionInfo = getCurrentInfo(request);
			Log.error("ProductController method:update() upload files error ...", sessionInfo);
			e.printStackTrace();
			throw new RuntimeException("Product update error ...", e);
		}
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("update product ... ", sessionInfo);

	}

	// add by wliming, 2016/02/24 18:53 begin
	// -> 增加信息模板的更新方法
	@RequestMapping(value = "/product/saveVideoDescription", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public long saveVideoDescription(@RequestBody final PmsProduct product, HttpServletRequest request) {
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("update product ... ", sessionInfo);
		pmsProductFacade.updateVideoDescription(product);
		return 1l;
	}
	// add by wliming, 2016/02/24 18:53 end

	// --------------------------------以下是前端展示内容 ----------------------------
	/**
	 * 首页 装载 更多作品页-手机端
	 */
	@RequestMapping(value = "/product/static/list", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public List<Product> data(@RequestBody final ProductView view) {

		final List<Product> list = proService.loadData(view);
		return list;
	}

	/**
	 * 首页 装载 更多作品页-PC端
	 */
	@RequestMapping(value = "/product/static/pc/list", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public List<Solr> load(final HttpServletRequest request, @RequestBody SolrView solrView) {

		// modify by wlc 2016-11-21 11:42:18
		// 修改为solr查询 begin
		// final List<Product> list = proService.loadProductByCommend();
		final SolrQuery query = new SolrQuery();
		query.set("qf", "productName^4 tags^3 teamName^2 pDescription^1");
		query.setQuery("*:*");
		query.setFields(
				"teamId,productId,productName,productType,itemName,teamName,orignalPrice,price,picLDUrl,length,pDescription,recommend,supportCount,tags");
		query.setStart(0);
		query.setRows(Integer.MAX_VALUE);
		if (null != solrView.getSort()) {
			query.setSort(solrView.getSort(), ORDER.desc);
		}
		final List<Solr> list = solrService.queryDocs(GlobalConstant.SOLR_PORTAL_URL, query);
		return list;
		// 修改为solr查询 end
	}

	@RequestMapping("/product/static/redirect")
	public ModelAndView redirect(final ModelMap model) {

		return new ModelAndView("/static/production", model);
	}

	/**
	 * 获取作品总数
	 * 
	 * @param view
	 *            分类标准
	 * @return 总数
	 */
	@RequestMapping("/product/static/pageSize")
	public long maxSize(@RequestBody final ProductView view) {

		final long size = proService.conditionSize(view);
		return size;
	}

	/**
	 * 作品页 数据
	 * 
	 * @param itemId
	 *            分类标准
	 * @param order
	 *            排序规则
	 */
	@RequestMapping(value = "/product/static/listWithCondition")
	public List<Product> listWithCondition(@RequestBody final ProductView view) {

		final List<Product> list = proService.listWithCondition(view);
		return list;
	}

	/**
	 * 查询 团队的作品信息
	 * 
	 * @param teamId
	 * @param productId
	 * @param model
	 */
	@RequestMapping(value = "/product/static/view/{teamId}/{productId}")
	public ModelAndView redirect(@PathVariable("teamId") final Integer teamId,
			@PathVariable("productId") final Integer productId, final ModelMap model) {
		model.addAttribute("teamId", teamId);
		model.addAttribute("productId", productId);
		Product product = proService.loadProduct(productId);
		model.addAttribute("product", product);
		return new ModelAndView("/static/team", model);
	}

	/**
	 * 装配相关视频
	 * 
	 * @param teamId
	 */
	@RequestMapping("/product/static/team/{teamId}")
	public List<Product> productInformationByTeam(@PathVariable("teamId") final Integer teamId) {

		final List<Product> list = proService.loadProductByTeam(teamId);
		return list;
	}

	/**
	 * 
	 * 
	 * @param teamId
	 */
	@RequestMapping("/product/static/order/team/{teamId}")
	public List<Product> productInformationByTeamOrder(@PathVariable("teamId") final Integer teamId) {

		final List<Product> list = proService.loadProductByTeamOrder(teamId);
		return list;
	}

	@RequestMapping("/product/static/information/{productId}")
	public PmsProduct information(@PathVariable("productId") final Integer productId) {
		final PmsProduct product = pmsProductFacade.loadProduct(productId);
		if (product.getTeamId() != null && !"".equals(product.getTeamId())) {
			final PmsTeam team = pmsTeamFacade.findTeamById(product.getTeamId());
			if (team != null) {
				product.setTeamDescription(team.getTeamDescription());
				product.setTeamName(team.getTeamName());
				product.setTeamPhotoUrl(team.getTeamPhotoUrl());
			}
		}
		return product;
	}

	/**
	 * 通过 供应商ID 获取已审核及审核中的视频
	 * 
	 * @param providerId
	 *            供应商唯一编码
	 * @return 已审核的产品列表
	 */
	@RequestMapping("/product/static/data/loadProducts/{providerId}")
	public List<PmsProduct> loadProductByProviderId(@PathVariable("providerId") final long teamId) {

		//final List<Product> list = proService.loadProductByProviderId(teamId);
		final List<PmsProduct> list = pmsProductFacade.loadProductByProviderId(teamId);
		return list;
	}

	/**
	 * 供应商根据 产品ID 删除视频
	 * 
	 * @param productId
	 *            视频唯一编号
	 */
	@RequestMapping("/product/static/data/deleteProduct/{productId}")
	public boolean deleteProductByProvider(@PathVariable("productId") final long productId,
			HttpServletRequest request) {

		long[] ids = new long[1];
		ids[0] = productId;
		//List<Product> list = proService.delete(ids); // 删除视频信息
		List<PmsProduct> list = pmsProductFacade.delete(ids); // 删除视频信息

		// 删除搜索索引
		solrService.deleteDoc(productId, SOLR_URL);
		solrService.deleteDoc(productId, SOLR_EMPLOYEE_URL);
		solrService.deleteDoc(productId, SOLR_PORTAL_URL);

		// serService.deleteByProduct(productId); // 删除服务信息

		// delete file on disk
		if (!list.isEmpty() && list.size() > 0) {

			for (final PmsProduct product : list) {
				// 删除 缩略图
				final String picLDUrl = product.getPicLDUrl();
				if (StringUtils.isNotBlank(picLDUrl))
					fdfsService.delete(picLDUrl);

				// 删除 高清图
				final String picHDUrl = product.getPicHDUrl();
				if (StringUtils.isNotBlank(picHDUrl))
					fdfsService.delete(picHDUrl);

				// 删除 视频
				final String videoUrl = product.getVideoUrl();
				if (StringUtils.isNotBlank(videoUrl))
					fdfsService.delete(videoUrl);

				// 待修改成分解富文本，删除图片
				final String description = product.getVideoDescription();
				List<String> imgList = JsoupUtil.getImgSrc(description);
				if (imgList != null && imgList.size() > 0) {
					for (String s : imgList) {
						if (StringUtils.isNotBlank(s))
							fdfsService.delete(s);
					}
				}
			}
		} else {
			throw new RuntimeException("Product ids is null");
		}
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("delete product ... ", sessionInfo);
		return true;
	}

	/**
	 * 根据 视频ID 获取视频
	 * 
	 * @param productId
	 *            视频唯一编号
	 */
	@RequestMapping("/product/static/data/{videoId}")
	public PmsProduct findProductById(@PathVariable("videoId") final long productId) {

		//final Product product = proService.findProductById(productId);
		final PmsProduct product = pmsProductFacade.findProductById(productId);
		return product;
	}

	/**
	 * 更新 视频基本信息，除了 推荐值等外
	 * 
	 * @return 服务ID
	 */
	@RequestMapping("/product/static/data/update/info")
	public boolean updateProductInfo(@RequestBody final PmsProduct product, HttpServletRequest request) {
		// 解码
		try {
			//Product oldProduct = proService.findProductById(product.getProductId());
			PmsProduct oldProduct = pmsProductFacade.findProductById(product.getProductId());
			oldProduct.setProductName(URLDecoder.decode(product.getProductName(), "UTF-8"));
			oldProduct.setCreationTime(product.getCreationTime());
			if (StringUtils.isNotEmpty(product.getTags())) {
				oldProduct.setTags(URLDecoder.decode(product.getTags(), "UTF-8"));
			}
			oldProduct.setFlag(product.getFlag());
			if (StringUtils.isNotBlank(product.getPicLDUrl())) {
				oldProduct.setPicLDUrl(product.getPicLDUrl());
				if (StringUtils.isNotBlank(oldProduct.getPicLDUrl())
						&& !product.getPicLDUrl().equals(oldProduct.getPicLDUrl())) {
					fdfsService.delete(oldProduct.getPicLDUrl());
				}
			}
			long l = pmsProductFacade.updateProductInfo(oldProduct); // 更新视频信息
			// ghost审核通过的自动创建service记录
			//List<Service> list = serService.loadService((int) product.getProductId());
			List<PmsService> list = pmsProductFacade.loadService((int) product.getProductId());
			if (product.getFlag() == 1) {
				if (null == list || list.size() == 0) {
					PmsService service = new PmsService();
					service.setProductId(product.getProductId());
					service.setProductName(product.getProductName());
					service.setServiceDiscount(1);
					service.setServiceName("service" + product.getProductId() + "-" + product.getProductName());
					service.setServiceOd(0);
					service.setServicePrice(0d);
					service.setServiceRealPrice(0d);
					service.setMcoms(Long.parseLong(product.getVideoLength()));
					//serService.save(service);
					pmsProductFacade.save(service);
				}
			}
			SessionInfo sessionInfo = getCurrentInfo(request);
			Log.error("update product ... ", sessionInfo);
			return l >= 0;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 保存 视频信息
	 * 
	 * @return 保存后视频ID
	 */
	@RequestMapping("/product/static/data/save/info")
	public long saveProductInfo(@RequestBody final PmsProduct product, HttpServletRequest request) {
		try {
			product.setProductName(URLDecoder.decode(product.getProductName(), "UTF-8"));
			//proService.save(product); // 保存视频信息
			long proId = pmsProductFacade.save(product); // 保存视频信息
			product.setProductId(proId);
			SessionInfo sessionInfo = getCurrentInfo(request);
			// 加入文件转换队列
			fileConvertMQService.sendMessage(product.getProductId(), product.getVideoUrl());
			Log.error("save product ... ", sessionInfo);
			return product.getProductId();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 更新文件路径
	 * 
	 * @param product
	 * 包含 视频唯一编号、缩略图、封面、视频路径
	 */
	/*@RequestMapping("/product/static/data/updateFilePath")
	public long updateFilePath(@RequestBody final Product product) {
		return proService.saveFileUrl(product);
	}*/

	/**
	 * 获取单个作品ID
	 * 
	 * @param teamId
	 *            供应商唯一编号
	 * @return 作品ID
	 */
	@RequestMapping("/product/static/data/loadSingleProduct/{teamId}")
	public long loadSingleProduct(@PathVariable("teamId") final long teamId) {

		final Product product = proService.loadSingleProduct(teamId);
		if (product != null)
			return product.getProductId();
		else
			return 0l;
	}

	/**
	 * 获取分销产品
	 * 
	 * @return 分销列表
	 */
	@RequestMapping("/product/static/data/salesproduct")
	public List<Product> salesProject(final HttpServletRequest request) {

		final List<Product> list = proService.loadSalesProduct();
		return list;
	}

	@RequestMapping(value = "/set/masterWork")
	public boolean setMasterWork(@RequestBody final PmsProduct product) {
		return pmsProductFacade.setMasterWork(product);
	}

	@RequestMapping(value = "/get/masterWork/{teamId}")
	public PmsProduct getMasterWork(@PathVariable("teamId") Long teamId, HttpServletRequest request) {
		if (teamId == null || teamId <= 0) {
			SessionInfo sessionInfo = getCurrentInfo(request);
			Log.error("productId is null ...", sessionInfo);
			return null;
		}
		//Product product = proService.getMasterWork(teamId);
		PmsProduct product = pmsProductFacade.getMasterWork(teamId);
		if (product == null) {
			List<PmsProduct> products = pmsProductFacade.loadProductByTeam(teamId);
			if (ValidateUtil.isValid(products)) {
				product = products.get(0);
			} else {
				SessionInfo sessionInfo = getCurrentInfo(request);
				Log.error("product is null ...", sessionInfo);
			}
		}
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("set masterWork ... ", sessionInfo);
		return product;
	}

	// 活动页面作品列表
	@RequestMapping(value = "/activity")
	public List<Product> loadActivityProducts() {
		return proService.loadActivityProducts();
	}

	/**
	 * 查找推荐的作品 分页
	 */
	@RequestMapping(value = "/product/recommend/list", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public DataGrid<PmsProduct> recommendList(final ProductView view, final PageParam param) {

		final long page = param.getPage();
		final long rows = param.getRows();
		param.setBegin((page - 1) * rows);
		param.setLimit(rows);
		final DataGrid<PmsProduct> dataGrid = pmsProductFacade.searchPageRecommendList(param);
		return dataGrid;
	}

	/**
	 * 查找推荐的作品 分页
	 */
	@RequestMapping(value = "/product/updateRecommend", method = RequestMethod.POST)
	public BaseMsg updateRecommend(final PmsProduct product) {
		final boolean b = pmsProductFacade.updateRecommend(product);
		if (b) {
			return new BaseMsg(1, "success");
		} else {
			return new BaseMsg(0, "修改失败");
		}
	}

	/**
	 * 修改作品可见性
	 */
	@RequestMapping(value = "/product/visibility")
	public boolean productVisibility(@RequestBody final PmsProduct product) {
		//return proService.updateProductVisibility(product);
		return pmsProductFacade.updateProductVisibility(product);
	}
}
