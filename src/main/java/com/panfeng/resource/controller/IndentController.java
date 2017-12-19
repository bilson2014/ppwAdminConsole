package com.panfeng.resource.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.alibaba.fastjson.JSONArray;
import com.paipianwang.pat.common.config.PublicConfig;
import com.paipianwang.pat.common.entity.DataGrid;
import com.paipianwang.pat.common.entity.PageParam;
import com.paipianwang.pat.common.entity.SessionInfo;
import com.paipianwang.pat.common.util.JsonUtil;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.facade.indent.entity.IndentSource;
import com.paipianwang.pat.facade.indent.entity.PmsIndent;
import com.paipianwang.pat.facade.indent.service.PmsIndentFacade;
import com.paipianwang.pat.facade.product.entity.PmsProduct;
import com.paipianwang.pat.facade.product.entity.PmsService;
import com.paipianwang.pat.facade.product.service.PmsProductFacade;
import com.paipianwang.pat.facade.product.service.PmsServiceFacade;
import com.paipianwang.pat.facade.right.entity.PmsEmployee;
import com.paipianwang.pat.facade.right.service.PmsEmployeeFacade;
import com.paipianwang.pat.facade.user.entity.Grade;
import com.paipianwang.pat.facade.user.service.PmsUserFacade;
import com.panfeng.domain.BaseMsg;
import com.panfeng.domain.Result;
import com.panfeng.mq.service.SmsMQService;
import com.panfeng.poi.GenerateExcel;
import com.panfeng.poi.IndentPoiAdapter;
import com.panfeng.poi.ProjectPoiAdapter;
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
	@Autowired
	private PmsEmployeeFacade pmsEmployeeFacad=null;
	@Autowired
	private PmsUserFacade pmsUserFacade=null;

	@RequestMapping("/indent-list")
	public ModelAndView view(final ModelMap model) {
		model.addAttribute("filmUrl", PublicConfig.FILM_URL);
		//处理数据字典
		List<Map<String,Object>> emlist=new ArrayList<Map<String,Object>>();
		for(IndentSource source:IndentSource.values()){
			Map<String,Object> map=new HashMap<>();
			map.put("value", source.getValue());
			map.put("text",source.getName());
			emlist.add(map);
		}
		Object json=JSONArray.toJSON(emlist);
		model.put("sourceCombobox", json);
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

		List<Map<String, Object>> datas=JsonUtil.getValueListMap(list);
		//数据处理
		editExportData(datas,list);

		OutputStream outputStream=null;
		try {
			 // 设置文件后缀  
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhh24mmss");  
	        String fn = URLEncoder.encode("indent_report_".concat(sdf.format(new Date()).toString() + ".xlsx"), "UTF-8");
	        // 设置响应  
	        response.setCharacterEncoding("UTF-8");  
//	        response.setContentType("text/csv; charset=UTF-8"); 
	        response.setContentType("application/octet-stream");
	        response.setHeader("Pragma", "public");  
	        response.setHeader("Cache-Control", "max-age=30");  
	        response.setHeader("Content-Disposition", "attachment; filename=" + fn);  
			
	        //文件导出
	        IndentPoiAdapter projectPoiAdapter = new IndentPoiAdapter();
			GenerateExcel ge = new GenerateExcel();
			projectPoiAdapter.setData(datas);
	        
			outputStream=response.getOutputStream();
			ge.generate(projectPoiAdapter, outputStream);
			outputStream.flush();
			Log.error("indent list export success ...", sessionInfo);
		} catch (IOException e) {
			e.printStackTrace();
			Log.error("indent list export success ...", sessionInfo);
		}finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 处理导出数据--显示值
	 * @param datas
	 */
	private void editExportData(List<Map<String, Object>> datas,List<PmsIndent> list) {
		// 数据处理
		List<PmsEmployee> employeeList = pmsEmployeeFacad.findEmployeeToSynergy();
//		List<PmsUser> userList = pmsUserFacade.all();
		for (int j=0;j<datas.size();j++) {
			Map<String, Object> data = datas.get(j);
			data.put("id",list.get(j).getId());
			
			Long employeeId = data.get("employeeId") == null ? null : Long.parseLong((String) data.get("employeeId"));
//			Long userId = data.get("userId") == null ? null : Long.parseLong((String) data.get("userId"));
			Long referrerId = data.get("referrerId") == null ? null : Long.parseLong((String) data.get("referrerId"));
			Long pMId = data.get("pMId") == null ? null : Long.parseLong((String) data.get("pMId"));

			//处理人员名称、推荐人名称、项目经理名称
			if (ValidateUtil.isValid(employeeList) && (employeeId != null || referrerId != null || pMId != null)) {
				for (PmsEmployee employee : employeeList) {
					if (employeeId != null && employee.getEmployeeId() == employeeId) {
						data.put("employeeRealName", employee.getEmployeeRealName());
					}
					if (referrerId != null && employee.getEmployeeId() == referrerId) {
						data.put("referrerRealName", employee.getEmployeeRealName());
					}
					if (pMId != null && employee.getEmployeeId() == pMId) {
						data.put("pMRealName", employee.getEmployeeRealName());
					}
				}
			}
			//客户联系人
//			if (ValidateUtil.isValid(userList) && userId != null) {
//				for (PmsUser user : userList) {
//					if (user.getUserId() == userId) {
//						data.put("userRealName", user.getRealName());
//						break;
//					}
//				}
//			}
			//职位
			Integer position = data.get("position") == null ? null : Integer.parseInt((String) data.get("position"));
			if (position != null) {
				for (int i = 0; i < Grade.position.length; i++) {
					if (Grade.position[i].getId() == position) {
						data.put("position", Grade.position[i].getText());
						break;
					}
				}
			}
			//订单状态显示值
			Integer indentType=data.get("indentType")==null?null:Integer.parseInt((String)data.get("indentType"));
			String indentTypeName="";
			switch (indentType) {
			case PmsIndent.ORDER_NEW:
				indentTypeName="新订单";
				break;
			case PmsIndent.ORDER_HANDLING:
				indentTypeName="处理中";
				break;
			case PmsIndent.ORDER_DONE:
				indentTypeName="完成";
				break;
			case PmsIndent.ORDER_STOP:
				indentTypeName="停滞";
				break;
			case PmsIndent.ORDER_AGAIN:
				indentTypeName="再次沟通";
				break;
			case PmsIndent.ORDER_REAL:
				indentTypeName="真实";
				break;
			case PmsIndent.ORDER_SHAM:
				indentTypeName="虚假";
				break;
			case PmsIndent.ORDER_SUBMIT:
				indentTypeName="提交";
				break;
			}
			data.put("indentTypeName",indentTypeName);
			//订单来源显示值
			Integer indentSource=data.get("indentSource")==null?null:Integer.parseInt((String)data.get("indentSource"));
			String indentSourceName="";
			if (indentSource != null) {
				for(IndentSource source:IndentSource.values()){
					if(indentSource.equals(source.getValue())){
						indentSourceName=source.getName();
						break;
					}
				}
			}
			data.put("indentSourceName",indentSourceName);
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