package com.panfeng.resource.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.paipianwang.pat.common.entity.ComboTreeModel;
import com.paipianwang.pat.common.entity.PmsResult;
import com.paipianwang.pat.workflow.entity.PmsQuotationType;
import com.paipianwang.pat.workflow.facade.PmsQuotationTypeFacade;

/**
 * 报价单
 */
@RestController
@RequestMapping("/portal")
public class QuotationController extends BaseController {

	@Autowired
	private PmsQuotationTypeFacade pmsQuotationTypeFacade;
	
	@RequestMapping("/quotationtype-list")
	public ModelAndView view(final ModelMap model){
		return new ModelAndView("/quotationtype-list", model);
	}
	
	@RequestMapping("/quotationtype/list")
	public List<PmsQuotationType> list(){
		return pmsQuotationTypeFacade.findAll();
	}
	
	@RequestMapping("/quotationtype/save")
	public PmsResult save(PmsQuotationType pmsQuotationType){
		long result=pmsQuotationTypeFacade.insert(pmsQuotationType);
		PmsResult pmsResult=new PmsResult();
		pmsResult.setResult(result>0?true:false);
		return pmsResult;
	}
	
	@RequestMapping("/quotationtype/update")
	public PmsResult update(PmsQuotationType pmsQuotationType){
		long result=pmsQuotationTypeFacade.update(pmsQuotationType);
		PmsResult pmsResult=new PmsResult();
		pmsResult.setResult(result>0?true:false);
		return pmsResult;
	}
	
	@RequestMapping("/quotationtype/delete")
	public PmsResult delete(final long[] ids){
		//校验
		
		//删除
		long result=pmsQuotationTypeFacade.deleteByIds(ids);
		PmsResult pmsResult=new PmsResult();
		pmsResult.setResult(result>0?true:false);
		return pmsResult;
	}
	/**
	 * 根据等级获取可选上级下拉框列表
	 * @param grade
	 * @return
	 */
	@RequestMapping("/quotationtype/selectlist/{grade}")
	public List<ComboTreeModel> selectList(@PathVariable("grade")Integer grade){
		List<ComboTreeModel> result=new ArrayList<ComboTreeModel>();
		if(grade>1){
			List<PmsQuotationType> types=pmsQuotationTypeFacade.findSelectionByGrade(grade);
			for(PmsQuotationType type:types){
				result.add(new ComboTreeModel(type.getTypeId()+"", type.getParentId()+"", type.getTypeName()));
			}
		}
		return result;
	}
	
}
