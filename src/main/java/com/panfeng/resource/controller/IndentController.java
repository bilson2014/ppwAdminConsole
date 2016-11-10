package com.panfeng.resource.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.panfeng.domain.Result;
import com.panfeng.domain.SessionInfo;
import com.panfeng.resource.model.Indent;
import com.panfeng.resource.model.Product;
import com.panfeng.resource.model.Service;
import com.panfeng.resource.view.DataGrid;
import com.panfeng.resource.view.IndentView;
import com.panfeng.resource.view.PageFilter;
import com.panfeng.service.IndentService;
import com.panfeng.service.ProductService;
import com.panfeng.service.ServiceService;
import com.panfeng.util.Log;

/**
 * 订单相关
 * 
 * @author Jack
 *
 */
@RestController
@RequestMapping("/portal")
public class IndentController extends BaseController {

	// private static final Logger logger = LoggerFactory.getLogger("error");

	@Autowired
	private final IndentService service = null;

	@Autowired
	private final ProductService productService = null;

	@Autowired
	private final ServiceService serService = null;

	@RequestMapping("/indent-list")
	public ModelAndView view(final ModelMap model) {

		return new ModelAndView("indent-list", model);
	}

	@RequestMapping(value = "/indent/list", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public DataGrid<Indent> list(final IndentView view, final PageFilter pf) {

		final long page = pf.getPage();
		final long rows = pf.getRows();
		view.setBegin((page - 1) * rows);
		view.setLimit(rows);

		final List<Indent> list = service.listWithPagination(view);
		final long total = service.maxSize(view);
		final DataGrid<Indent> dataGrid = new DataGrid<Indent>();
		dataGrid.setRows(list);
		dataGrid.setTotal(total);
		return dataGrid;
	}

	@RequestMapping(value = "/indent/save", method = RequestMethod.POST)
	public long save(final Indent indent,HttpServletRequest request) {

		final long ret = service.save(indent);
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("add new order ...", sessionInfo);
		return ret;
	}

	@RequestMapping(value = "/indent/update", method = RequestMethod.POST)
	public long update(final Indent indent,HttpServletRequest request) {

		final long ret = service.update(indent);
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("update order ...", sessionInfo);
		return ret;
	}

	@RequestMapping(value = "/indent/delete", method = RequestMethod.POST)
	public long delete(final long[] ids, HttpServletRequest request) {

		if (ids.length > 0) {
			service.delete(ids);
			SessionInfo sessionInfo = getCurrentInfo(request);
			Log.error("delete orders ...  ids:"+ids.toString(), sessionInfo);
		} else {
			SessionInfo sessionInfo = getCurrentInfo(request);
			Log.error("Indent Delete Error ...", sessionInfo);
			throw new RuntimeException("Indent Delete Error ...");
		}

		return 0l;
	}

	// 下单
	@RequestMapping(value = "/indent/order", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public Result order(@RequestBody final Indent indent, HttpServletRequest request) {

		final Result result = new Result();
		try {
			indent.setIndentName(URLDecoder.decode(indent.getIndentName(), "UTF-8"));
			if (indent.getIndent_recomment() != null && !"".equals(indent.getIndent_recomment())) {
				indent.setIndent_recomment(URLDecoder.decode(indent.getIndent_recomment(), "UTF-8"));
			}

			final long teamId = indent.getTeamId();
			final long productId = indent.getProductId();
			final long serviceId = indent.getServiceId();

			// 如果按产品下单，那么下单之后的订单信息需要与数据库进行对比
			if (teamId != -1 && productId != -1 && serviceId != -1) {
				// 产品下单
				final Product product = productService.findProductById(productId);
				indent.setProduct_name(product.getProductName());
				final Service ser = serService.getServiceById(serviceId);
				indent.setSecond(ser.getMcoms());
				indent.setIndentPrice(ser.getServiceRealPrice());
			}

			final long ret = service.order(indent);
			if (ret > 0) {
				result.setRet(true);
				result.setMessage(indent.getProduct_name());
			}
			return result;
		} catch (UnsupportedEncodingException e) {
			SessionInfo sessionInfo = getCurrentInfo(request);
			Log.error("Client Order Decoder Failure ...", sessionInfo);
			e.printStackTrace();
		}
		result.setRet(false);
		return result;
	}

	/**
	 * 检测 新订单个数
	 * 
	 * @return 返回 新订单 个数
	 */
	@RequestMapping("/indent/checkStatus/new")
	public long checkStatus(final HttpServletRequest request) {

		final long count = service.checkStatus(0);
		return count;
	}
	
	/**
	 * 成本计算器，保存订单后返回订单
	 */
	@RequestMapping(value = "/indent/cost/save", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public Indent calculateSave(@RequestBody final Indent indent,HttpServletRequest request) {
		long ret = 0l;
		SessionInfo sessionInfo = getCurrentInfo(request);
		if(indent.getIndentId()==0){
			 ret = service.save(indent);
			 Log.error("add new order ...", sessionInfo);
		}else{//更新操作
			ret = service.updateForCalculate(indent);
			Log.error("update order ...", sessionInfo);
		}
		return ret>0?indent:null;
	}
}