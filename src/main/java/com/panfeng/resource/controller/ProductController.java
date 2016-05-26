package com.panfeng.resource.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.panfeng.resource.model.Product;
import com.panfeng.resource.model.Service;
import com.panfeng.resource.model.Team;
import com.panfeng.resource.view.DataGrid;
import com.panfeng.resource.view.PageFilter;
import com.panfeng.resource.view.ProductView;
import com.panfeng.service.KindeditorService;
import com.panfeng.service.ProductService;
import com.panfeng.service.ServiceService;
import com.panfeng.service.SolrService;
import com.panfeng.service.TeamService;
import com.panfeng.util.DataUtil;
import com.panfeng.util.FileUtils;

/**
 * 产品管理类
 * 
 * @author GY
 */
@RestController
@RequestMapping("/portal")
public class ProductController extends BaseController {

	private static Logger logger = LoggerFactory.getLogger("error");

	@Autowired
	private final ProductService proService = null;

	@Autowired
	private final TeamService teamService = null;

	@Autowired
	private final ServiceService serService = null;
	
	@Autowired
	private KindeditorService kindService;
	
	@Autowired
	private SolrService solrService = null;

	private static String FILE_PROFIX = null; // 文件前缀

	private static String PRODUCT_VIDEO_PATH = null; // video文件路径

	private static String PRODUCT_IMAGE_PATH = null; // 产品图片路径

	private static String ALLOW_IMAGE_TYPE = null;

	private static String ALLOW_VIDEO_TYPE = null;
	
	private static String SOLR_URL = null;

	public ProductController() {
		if (PRODUCT_VIDEO_PATH == null || "".equals(PRODUCT_VIDEO_PATH)) {
			final InputStream is = this.getClass().getClassLoader()
					.getResourceAsStream("jdbc.properties");
			try {
				Properties propertis = new Properties();
				propertis.load(is);
				FILE_PROFIX = propertis.getProperty("file.prefix");
				PRODUCT_VIDEO_PATH = propertis
						.getProperty("upload.server.product.video");
				PRODUCT_IMAGE_PATH = propertis
						.getProperty("upload.server.product.image");
				ALLOW_VIDEO_TYPE = propertis.getProperty("videoType");
				ALLOW_IMAGE_TYPE = propertis.getProperty("imageType");
				SOLR_URL = propertis.getProperty("solr.url");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@RequestMapping(value = "/product-list")
	public ModelAndView view(final ModelMap model) {

		return new ModelAndView("product-list", model);
	}
	
	@RequestMapping(value = "/product/sessionId",method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public Product getSessionId(final ModelMap map) {
		
		final String sessionId = DataUtil.getUuid();
		Product product = new Product();
		product.setSessionId(sessionId);
		return product; 
	}

	@RequestMapping(value = "/product/init", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public List<Team> init() {

		final List<Team> list = teamService.getAll();
		return list;
	}

	/**
	 * 分页检索
	 * 
	 * @param view
	 * product-条件视图
	 */
	@RequestMapping(value = "/product/list", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public DataGrid<Product> list(final ProductView view,final PageFilter pf) {

		final long page = pf.getPage();
		final long rows = pf.getRows();
		view.setBegin((page - 1) * rows);
		view.setLimit(rows);
		
		DataGrid<Product> dataGrid = new DataGrid<Product>();
		final List<Product> list = proService.listWithPagination(view);
		for (Product product : list) {
			String sid = product.getSessionId();
			if (sid == null || "".equals(sid)) {
				product.setSessionId(DataUtil.getUuid());
			}
		}
		dataGrid.setRows(list);
		final long total = proService.maxSize(view);
		dataGrid.setTotal(total);
		return dataGrid;
	}

	/**
	 * 删除
	 * 
	 * @param ids
	 * product id array
	 */
	@RequestMapping(value = "/product/delete", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public long delete(final long[] ids) {

		if (ids.length > 0) {
			final List<Product> list = proService.delete(ids);
			// delete file on disk
			if (!list.isEmpty() && list.size() > 0) {

				for (final Product product : list) {
					// 删除 缩略图
					final String picLDUrl = product.getPicLDUrl();
					FileUtils.deleteFile(picLDUrl);

					// 删除 高清图
					final String picHDUrl = product.getPicHDUrl();
					FileUtils.deleteFile(picHDUrl);

					// 删除 视频
					final String videoUrl = product.getVideoUrl();
					FileUtils.deleteFile(videoUrl);
					
					// 删除产品编辑页图片
					String sessionId = product.getSessionId();
					if (sessionId != null && !"".equals(sessionId))
						kindService.deleteImageDir(product.getSessionId());
					
					// 删除搜索索引
					solrService.deleteDoc(product.getProductId(), SOLR_URL);
				}
			} else {
				throw new RuntimeException("Product ids is null");
			}
		}
		return 0l;
	}

	@RequestMapping(value = "/product/save", method = RequestMethod.POST)
	public void save(final HttpServletRequest request,
			final HttpServletResponse response,
			@RequestParam final MultipartFile[] uploadFiles,
			final Product product) {
		response.setContentType("text/html;charset=UTF-8");

		// 保存 product
		final long productId = proService.save(product);

		// 视频文件全路径
		final String videoPath = FILE_PROFIX + PRODUCT_VIDEO_PATH;

		// 图片文件全路径
		final String imagePath = FILE_PROFIX + PRODUCT_IMAGE_PATH;

		try {
			File videoDir = new File(videoPath);
			File imageDir = new File(imagePath);
			if (!videoDir.exists())
				videoDir.mkdir();
			if (!imageDir.exists())
				imageDir.mkdir();

			// 路径接收
			final List<String> pathList = new ArrayList<String>();

			for (int i = 0; i < uploadFiles.length; i++) {
				final MultipartFile multipartFile = uploadFiles[i];
				if (!multipartFile.isEmpty()) {
					// 分组保存video、image
					final String multipartFileName = multipartFile
							.getOriginalFilename();
					final String extName = FileUtils.getExtName(
							multipartFileName, ".");
					final short fileType = FileUtils.divideIntoGroup(extName,
							ALLOW_IMAGE_TYPE, ALLOW_VIDEO_TYPE);
					final StringBuffer fileName = new StringBuffer();
					fileName.append("product" + productId);
					fileName.append("-");
					final Calendar calendar = new GregorianCalendar();
					fileName.append(calendar.get(Calendar.YEAR));
					fileName.append((calendar.get(Calendar.MONTH) + 1) < 10 ? "0"
							+ (calendar.get(Calendar.MONTH) + 1)
							: (calendar.get(Calendar.MONTH) + 1));
					fileName.append(calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0"
							+ calendar.get(Calendar.DAY_OF_MONTH)
							: calendar.get(Calendar.DAY_OF_MONTH));
					fileName.append(calendar.get(Calendar.HOUR_OF_DAY));
					fileName.append(calendar.get(Calendar.MINUTE));
					fileName.append(calendar.get(Calendar.SECOND));
					fileName.append(calendar.get(Calendar.MILLISECOND));
					fileName.append(i);
					fileName.append(".");
					fileName.append(extName);
					String path = "";
					switch (fileType) {
					case 0: // video
						path += PRODUCT_VIDEO_PATH + "/" + fileName.toString();
						break;
					case 1: // image
						path += PRODUCT_IMAGE_PATH + "/" + fileName.toString();
						break;
					case 2: // other
						throw new RuntimeException("file type error ...");
					}
					File destFile = new File(FILE_PROFIX + path);
					multipartFile.transferTo(destFile);
					pathList.add(path);
				} else {
					pathList.add("");
				}
			}
			product.setVideoUrl(pathList.get(0));
			product.setPicHDUrl(pathList.get(1));
			product.setPicLDUrl(pathList.get(2));
			// 保存路径
			proService.saveFileUrl(product);
		} catch (Exception e) {
			logger.error("ProductController method:save() -- save product error ...");
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/product/update", method = RequestMethod.POST)
	public void update(final HttpServletRequest request,
			final HttpServletResponse response,
			@RequestParam final MultipartFile[] uploadFiles,
			final Product product) {
		
		final long productId = product.getProductId(); // product id
		final Product originalProduct = proService.findProductById(productId);
		// 获取未更改前的product对象,用于删除修改过的文件
		final List<String> pathList = new ArrayList<String>(); // 路径集合

		try {
			// 视频文件全路径
			final String videoPath = FILE_PROFIX + PRODUCT_VIDEO_PATH;

			// 图片文件全路径
			final String imagePath = FILE_PROFIX + PRODUCT_IMAGE_PATH;

			File videoDir = new File(videoPath);
			File imageDir = new File(imagePath);
			if (!videoDir.exists())
				videoDir.mkdir();
			if (!imageDir.exists())
				imageDir.mkdir();

			for (int i = 0; i < uploadFiles.length; i++) {
				final MultipartFile multipartFile = uploadFiles[i];
				if (!multipartFile.isEmpty()) {
					// file字段 如果不为空,说明 更改了上传文件
					// 分组保存video、image
					final String multipartFileName = multipartFile
							.getOriginalFilename();
					final String extName = FileUtils.getExtName(
							multipartFileName, ".");
					final short fileType = FileUtils.divideIntoGroup(extName,
							ALLOW_IMAGE_TYPE, ALLOW_VIDEO_TYPE);
					final StringBuffer fileName = new StringBuffer();
					fileName.append("product" + productId);
					fileName.append("-");
					final Calendar calendar = new GregorianCalendar();
					fileName.append(calendar.get(Calendar.YEAR));
					fileName.append((calendar.get(Calendar.MONTH) + 1) < 10 ? "0"
							+ (calendar.get(Calendar.MONTH) + 1)
							: (calendar.get(Calendar.MONTH) + 1));
					fileName.append(calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0"
							+ calendar.get(Calendar.DAY_OF_MONTH)
							: calendar.get(Calendar.DAY_OF_MONTH));
					fileName.append(calendar.get(Calendar.HOUR_OF_DAY));
					fileName.append(calendar.get(Calendar.MINUTE));
					fileName.append(calendar.get(Calendar.SECOND));
					fileName.append(calendar.get(Calendar.MILLISECOND));
					fileName.append(i);
					fileName.append(".");
					fileName.append(extName);
					String path = "";
					switch (fileType) {
					case 0: // video
						path += PRODUCT_VIDEO_PATH + "/" + fileName.toString();
						break;
					case 1: // image
						path += PRODUCT_IMAGE_PATH + "/" + fileName.toString();
						break;
					case 2: // other
						throw new RuntimeException("file type error ...");
					}
					File destFile = new File(FILE_PROFIX + path);
					multipartFile.transferTo(destFile);
					pathList.add(path);
				} else {
					// file字段 如果为空,说明 未上传新文件
					pathList.add("");
				}
			}
			product.setVideoUrl(pathList.get(0));
			product.setPicHDUrl(pathList.get(1));
			product.setPicLDUrl(pathList.get(2));

			proService.update(product);

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
						case 1: // picHDFile
							path = originalProduct.getPicHDUrl();
							break;
						case 2: // picLDFile
							path = originalProduct.getPicLDUrl();
							break;
						default:
							continue;
						}
						if (path != null && !"".equals(path)) {
							FileUtils.deleteFile(FILE_PROFIX + path);
						}
					}
				}
			}

		} catch (IOException e) {
			logger.error("ProductController method:update() upload files error ...");
			e.printStackTrace();
			throw new RuntimeException("Product update error ...", e);
		}

	}
	
	// add by wliming, 2016/02/24 18:53 begin
	// -> 增加信息模板的更新方法
	@RequestMapping(value = "/product/saveVideoDescription", method = RequestMethod.POST,produces = "application/json; charset=UTF-8")
	public long saveVideoDescription(@RequestBody final Product product) {
		proService.updateVideoDescription(product);
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
	public List<Product> load(final HttpServletRequest request) {

		final List<Product> list = proService.loadProductByCommend();
		return list;
	}

	@RequestMapping("/product/static/redirect")
	public ModelAndView redirect(final ModelMap model) {

		return new ModelAndView("/static/production", model);
	}

	/**
	 * 获取作品总数
	 * 
	 * @param view 分类标准
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
	 * @param itemId 分类标准
	 * @param order 排序规则
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
			@PathVariable("productId") final Integer productId,
			final ModelMap model) {
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
	public List<Product> productInformationByTeam(
			@PathVariable("teamId") final Integer teamId) {

		final List<Product> list = proService.loadProductByTeam(teamId);
		return list;
	}

	@RequestMapping("/product/static/information/{productId}")
	public Product information(
			@PathVariable("productId") final Integer productId) {

		final Product product = proService.loadProduct(productId);
		return product;
	}

	/**
	 * 通过 供应商ID 获取已审核及审核中的视频
	 * 
	 * @param providerId 供应商唯一编码
	 * @return 已审核的产品列表
	 */
	@RequestMapping("/product/static/data/loadProducts/{providerId}")
	public List<Product> loadProductByProviderId(
			@PathVariable("providerId") final long teamId) {

		final List<Product> list = proService.loadProductByProviderId(teamId);
		return list;
	}

	/**
	 * 供应商根据 产品ID 删除视频
	 * 
	 * @param productId
	 * 视频唯一编号
	 */
	@RequestMapping("/product/static/data/deleteProduct/{productId}")
	public boolean deleteProductByProvider(
			@PathVariable("productId") final long productId) {

		long[] ids = new long[1];
		ids[0] = productId;
		List<Product> list = proService.delete(ids); // 删除视频信息
		
		// 删除搜索索引
		solrService.deleteDoc(productId, SOLR_URL);
		
		serService.deleteByProduct(productId); // 删除服务信息

		// delete file on disk
		if (!list.isEmpty() && list.size() > 0) {

			for (final Product product : list) {
				// 删除 缩略图
				final String picLDUrl = product.getPicLDUrl();
				FileUtils.deleteFile(FILE_PROFIX + picLDUrl);

				// 删除 高清图
				final String picHDUrl = product.getPicHDUrl();
				FileUtils.deleteFile(FILE_PROFIX + picHDUrl);

				// 删除 视频
				final String videoUrl = product.getVideoUrl();
				FileUtils.deleteFile(FILE_PROFIX + videoUrl);
			}
		} else {
			throw new RuntimeException("Product ids is null");
		}
		return true;
	}

	/**
	 * 根据 视频ID 获取视频
	 * 
	 * @param productId 视频唯一编号
	 */
	@RequestMapping("/product/static/data/{videoId}")
	public Product findProductById(@PathVariable("videoId") final long productId) {

		final Product product = proService.findProductById(productId);
		// 获取服务价格
		Service service = serService.loadSingleService(Integer.parseInt(String
				.valueOf(productId)));
		if (service != null) {
			product.setServicePrice(service.getServicePrice());
			product.setServiceId(service.getServiceId());
		}
		return product;
	}

	/**
	 * 更新 视频基本信息，除了 推荐值等外
	 * 
	 * @return 服务ID
	 */
	@RequestMapping("/product/static/data/update/info")
	public long updateProductInfo(@RequestBody final Product product) {

		// 解码
		try {
			product.setpDescription(URLDecoder.decode(
					product.getpDescription(), "UTF-8"));
			product.setProductName(URLDecoder.decode(product.getProductName(),
					"UTF-8"));
			product.setVideoLength(URLDecoder.decode(product.getVideoLength(),
					"UTF-8"));

			if (product.getTags() != null && !"".equals(product.getTags())) {
				product.setTags(URLDecoder.decode(product.getTags(), "UTF-8"));
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		proService.updateProductInfo(product); // 更新视频信息

		final Service service = new Service();
		if (product.getServiceId() != 0) { // 数据库中有此服务数据
			service.setServiceId(product.getServiceId());
			service.setMcoms(Long.parseLong(product.getVideoLength()));
			service.setServicePrice(product.getServicePrice());

			// 获取该服务打折信息
			Service originalService = serService.loadServiceById(product
					.getServiceId());
			final double discount = originalService.getServiceDiscount();
			final double realPrice = product.getServicePrice() * discount;
			BigDecimal bg = new BigDecimal(realPrice);
			final double roundRealPrice = bg.setScale(2,
					BigDecimal.ROUND_HALF_UP).doubleValue();
			service.setServiceRealPrice(roundRealPrice);

			serService.updatePriceAndMcoms(service); // 更新 服务信息
			return product.getServiceId();
		} else { // 数据库中无此服务数据
			final double servicePrice = product.getServicePrice(); // 保存服务信息
			service.setProductId(product.getProductId());
			service.setProductName(product.getProductName());
			service.setServiceDiscount(1);
			service.setServiceName("service" + product.getProductId() + "-"
					+ product.getProductName());
			service.setServiceOd(0);
			service.setServicePrice(servicePrice);
			service.setServiceRealPrice(servicePrice);
			service.setMcoms(Long.parseLong(product.getVideoLength()));
			serService.save(service);
			return service.getServiceId();
		}
	}

	/**
	 * 保存 视频信息
	 * 
	 * @return 保存后视频ID
	 */
	@RequestMapping("/product/static/data/save/info")
	public long saveProductInfo(@RequestBody final Product product) {
		try {
			// 解码
			product.setpDescription(URLDecoder.decode(
					product.getpDescription(), "UTF-8"));
			product.setProductName(URLDecoder.decode(product.getProductName(),
					"UTF-8"));
			product.setVideoLength(URLDecoder.decode(product.getVideoLength(),
					"UTF-8"));
			if (product.getTags() != null && !"".equals(product.getTags())) {
				product.setTags(URLDecoder.decode(product.getTags(), "UTF-8"));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		proService.save(product); // 保存视频信息

		final double servicePrice = product.getServicePrice(); // 保存服务信息
		Service service = new Service();
		service.setProductId(product.getProductId());
		service.setProductName(product.getProductName());
		service.setServiceDiscount(1);
		service.setServiceName("service" + product.getProductId() + "-"
				+ product.getProductName());
		service.setServiceOd(0);
		service.setServicePrice(servicePrice);
		service.setServiceRealPrice(servicePrice);
		service.setMcoms(Long.parseLong(product.getVideoLength()));
		serService.save(service);
		return product.getProductId();
	}

	/**
	 * 更新文件路径
	 * 
	 * @param product
	 * 包含 视频唯一编号、缩略图、封面、视频路径
	 */
	@RequestMapping("/product/static/data/updateFilePath")
	public long updateFilePath(@RequestBody final Product product) {

		return proService.saveFileUrl(product);
	}

	/**
	 * 获取单个作品ID
	 * 
	 * @param teamId 供应商唯一编号
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
	 * @return 分销列表
	 */
	@RequestMapping("/product/static/data/salesproduct")
	public List<Product> salesProject(final HttpServletRequest request){
		
		final List<Product> list = proService.loadSalesProduct();
		return list;
	}
}