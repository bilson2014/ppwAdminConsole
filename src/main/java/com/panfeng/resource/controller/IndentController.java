package com.panfeng.resource.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.paipianwang.pat.common.config.PublicConfig;
import com.paipianwang.pat.common.entity.DataGrid;
import com.paipianwang.pat.common.entity.PageParam;
import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.common.util.JsonUtil;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.facade.indent.entity.PmsIndent;
import com.paipianwang.pat.facade.indent.service.PmsIndentFacade;
import com.paipianwang.pat.facade.product.entity.PmsProduct;
import com.paipianwang.pat.facade.product.entity.PmsService;
import com.paipianwang.pat.facade.product.service.PmsProductFacade;
import com.paipianwang.pat.facade.product.service.PmsServiceFacade;
import com.panfeng.domain.BaseMsg;
import com.panfeng.domain.Result;
import com.panfeng.mq.service.SmsMQService;
import com.panfeng.resource.model.Indent;
import com.panfeng.resource.view.IndentView;
import com.panfeng.util.CsvWriter;
import com.panfeng.util.DateUtils;
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

	@Autowired
	private final SmsMQService smsMQService = null;

	@Autowired
	private PmsIndentFacade pmsIndentFacade = null;
	@Autowired
	private PmsProductFacade pmsProductFacade = null;
	@Autowired
	private PmsServiceFacade pmsServiceFacade = null;

	@RequestMapping("/indent-list")
	public ModelAndView view(final ModelMap model) {
		model.addAttribute("filmUrl", PublicConfig.FILM_URL);
		return new ModelAndView("indent-list", model);
	}

	@RequestMapping(value = "/indent/list", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public DataGrid<PmsIndent> list(final IndentView view, final PageParam param) {

		final long page = param.getPage();
		final long rows = param.getRows();
		param.setBegin((page - 1) * rows);
		param.setLimit(rows);

		final DataGrid<PmsIndent> dataGrid = pmsIndentFacade.listWithPagination(param, JsonUtil.objectToMap(view));
		return dataGrid;
	}

	@RequestMapping(value = "/indent/save", method = RequestMethod.POST)
	public long save(final PmsIndent pmsIndent, HttpServletRequest request) {

		final long ret = pmsIndentFacade.save(pmsIndent);
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("add new order ...", sessionInfo);
		return ret;
	}

	@RequestMapping(value = "/indent/update", method = RequestMethod.POST)
	public long update(final PmsIndent pmsIndent, HttpServletRequest request) {
		final long ret = pmsIndentFacade.update(pmsIndent);
		SessionInfo sessionInfo = getCurrentInfo(request);
		Log.error("update order ...", sessionInfo);
		return ret;
	}

	@RequestMapping(value = "/indent/delete", method = RequestMethod.POST)
	public long delete(final long[] ids, HttpServletRequest request) {
		if (ids.length > 0) {
			pmsIndentFacade.delIndentByIds(ids);
			SessionInfo sessionInfo = getCurrentInfo(request);
			Log.error("delete orders ...  ids:" + ids.toString(), sessionInfo);
		} else {
			SessionInfo sessionInfo = getCurrentInfo(request);
			Log.error("Indent Delete Error ...", sessionInfo);
			throw new RuntimeException("Indent Delete Error ...");
		}

		return 0l;
	}

	// 下单
	@RequestMapping(value = "/indent/order", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public Result order(@RequestBody final PmsIndent indent, HttpServletRequest request) {

		final Result result = new Result();
		String productName = null;
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
				final PmsProduct product = pmsProductFacade.findProductById(productId);
				productName = product.getProductName();
				final PmsService ser = pmsServiceFacade.getServiceById(serviceId);
				indent.setSecond(ser.getMcoms());
				indent.setIndentPrice(ser.getServiceRealPrice());
			}

			boolean res = pmsIndentFacade.saveOrder(indent);
			if (res) {
				result.setRet(true);
				String telephone = PublicConfig.PHONENUMBER_ORDER;
				if (indent.getSendToStaff()) {
					if (StringUtils.isBlank(productName)) {
						smsMQService.sendMessage("131844", telephone,
								new String[] { indent.getIndent_tele(), DateUtils.nowTime(), "【未指定具体影片】" });
					} else {
						smsMQService.sendMessage("131844", telephone,
								new String[] { indent.getIndent_tele(), DateUtils.nowTime(), "【" + productName + "】" });
					}
				}
				// 发送短信给用户下单成功
				if (indent.getSendToUser()) {
					smsMQService.sendMessage("131329", indent.getIndent_tele(), null);
				}
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

		final long count = pmsIndentFacade.checkStatus(0);
		return count;
	}

	/**
	 * 批量修改订单状态
	 */
	@RequestMapping(value = "/indent/modifyType", method = RequestMethod.POST)
	public boolean changeIndentsType(final Indent indent) {
		return pmsIndentFacade.changeIndentsType(indent.getIds(), indent.getIndentType());
	}

	@RequestMapping(value = "/indent/export", method = RequestMethod.POST)
	public void export(final IndentView view, final HttpServletResponse response, final HttpServletRequest request) {

		SessionInfo sessionInfo = getCurrentInfo(request);
		final List<PmsIndent> list = pmsIndentFacade.listWithCondition(JsonUtil.objectToMap(view));
		//处理订单状态、订单来源显示值
		if (ValidateUtil.isValid(list)) {
			for (PmsIndent indent : list) {
				switch (indent.getIndentType()) {
				case PmsIndent.ORDER_NEW:
					indent.setIndentTypeName("新订单");
					break;
				case PmsIndent.ORDER_HANDLING:
					indent.setIndentTypeName("处理中");
					break;
				case PmsIndent.ORDER_DONE:
					indent.setIndentTypeName("完成");
					break;
				case PmsIndent.ORDER_STOP:
					indent.setIndentTypeName("停滞");
					break;
				case PmsIndent.ORDER_AGAIN:
					indent.setIndentTypeName("再次沟通");
					break;
				case PmsIndent.ORDER_REAL:
					indent.setIndentTypeName("真实");
					break;
				case PmsIndent.ORDER_SHAM:
					indent.setIndentTypeName("虚假");
					break;
				case PmsIndent.ORDER_SUBMIT:
					indent.setIndentTypeName("提交");
					break;
				}

				if (indent.getIndentSource() != null) {
					switch (indent.getIndentSource()) {
					case PmsIndent.SOURCE_ONLINE_WEBSITE:
						indent.setIndentSourceName("线上-网站");
						break;
					case PmsIndent.SOURCE_ONLINE_ACTIVITY:
						indent.setIndentSourceName("线上-活动");
						break;
					case PmsIndent.SOURCE_ONLINE_NEW_MEDIA:
						indent.setIndentSourceName("线下-新媒体");
						break;
					case PmsIndent.SOURCE_OFFLINE_TELEMARKETING:
						indent.setIndentSourceName("线下-电销");
						break;
					case PmsIndent.SOURCE_OFFLINE_DIRECT_SELLING:
						indent.setIndentSourceName("线下-直销");
						break;
					case PmsIndent.SOURCE_OFFLINE_ACTIVITY:
						indent.setIndentSourceName("线下-活动");
						break;
					case PmsIndent.SOURCE_OFFLINE_CHANNEL:
						indent.setIndentSourceName("线下-渠道");
						break;
					case PmsIndent.SOURCE_REPEAT:
						indent.setIndentSourceName("复购");
						break;
					case PmsIndent.SOURCE_ONLINE_400:
						indent.setIndentSourceName("线上-400");
						break;
					case PmsIndent.SOURCE_ONLINE_BRIDGE:
						indent.setIndentSourceName("线上-商桥");
						break;
					}
				}
				indent.setIndentId(indent.getId());
			}
		}
		
		
		// 完成数据csv文件的封装
		String displayColNames = "订单名称,订单编号,下单时间,订单金额,订单状态,客户电话,订单备注,CRM备注,分销渠道,订单来源";
		String matchColNames = "indentName,indentId,orderDate,indentPrice,indentTypeName,indent_tele,indent_recomment,cSRecomment,salesmanUniqueId,indentSourceName";
		String fileName = "indent_report_";
		String content = CsvWriter.formatCsvData(JsonUtil.getValueListMap(list), displayColNames, matchColNames);
		try {
			Log.error("indent list export success ...", sessionInfo);
			CsvWriter.exportCsv(fileName, content, response);
		} catch (IOException e) {
			Log.error("indent list export error ...", sessionInfo);
		}
	}

	@RequestMapping(value = "/indent/updateCustomerService", method = RequestMethod.POST)
	public BaseMsg updateCustomerService(String employeeIds, Long customerService) {
		BaseMsg baseMsg = new BaseMsg();
		baseMsg.setCode(BaseMsg.ERROR);
		baseMsg.setErrorMsg("更新失败！");
		if (ValidateUtil.isValid(employeeIds)) {
			String[] split = employeeIds.split(",");
			if (split != null && split.length > 0) {
				for (int i = 0; i < split.length; i++) {
					String id = split[i];
					if (!"".equals(id) && !"".equals(id.trim())) {
						Long indentId = Long.valueOf(id);
						PmsIndent pi = new PmsIndent();
						pi.setIndentId(indentId);
						pi.setEmployeeId(customerService);
						pmsIndentFacade.updateCustomerService(pi);
					}
				}
				baseMsg.setCode(BaseMsg.NORMAL);
				baseMsg.setErrorMsg("更新成功！");
			}
		}
		return baseMsg;
	}

	/**
	 * 大炮改飞机新增方法！ 项目经理管家能看见的都是已经提交了的订单 {@code PmsIndent.ORDER_DONE}
	 * 
	 * @return
	 */
	@RequestMapping(value = "/list/require")
	public DataGrid<PmsIndent> getRequireList(PageParam param) {
		IndentView view = new IndentView();
		view.setIndentType(PmsIndent.ORDER_SUBMIT);
		long page = param.getPage();
		long rows = param.getRows();
		param.setBegin((page - 1) * rows);
		param.setLimit(rows);

		DataGrid<PmsIndent> dataGrid = pmsIndentFacade.listWithPagination(param, JsonUtil.objectToMap(view));

		return dataGrid;
	}
}