package com.panfeng.resource.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.paipianwang.pat.common.entity.ComboTreeModel;
import com.paipianwang.pat.common.entity.DataGrid;
import com.paipianwang.pat.common.entity.PageParam;
import com.paipianwang.pat.common.entity.PmsResult;
import com.paipianwang.pat.common.util.JsonUtil;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.facade.indent.entity.PmsIndent;
import com.paipianwang.pat.workflow.entity.PmsQuotationItem;
import com.paipianwang.pat.workflow.entity.PmsQuotationTemplate;
import com.paipianwang.pat.workflow.entity.PmsQuotationType;
import com.paipianwang.pat.workflow.facade.PmsQuotationTemplateFacade;
import com.paipianwang.pat.workflow.facade.PmsQuotationTypeFacade;
import com.panfeng.resource.view.IndentView;

/**
 * 报价单
 */
@RestController
@RequestMapping("/portal")
public class QuotationController extends BaseController {

	@Autowired
	private PmsQuotationTypeFacade pmsQuotationTypeFacade;
	@Autowired
	private PmsQuotationTemplateFacade pmsQuotationTemplateFacade;
	
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
		long result=0;;
		PmsResult pmsResult=new PmsResult();
		for(long id:ids){
			result=pmsQuotationTypeFacade.delete(id);
			if(result<=0){
				pmsResult.setResult(false);
			}
		}
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
	
	/**
	 * 根据id获取下级子节点
	 * 		即根据选项获取下级报价单类型下拉框值
	 * @param typeId
	 * @return
	 */
	@RequestMapping("/quotationtype/select")
	public List<ComboTreeModel> getPmsQuotationTypeChildren(Long typeId){
		List<ComboTreeModel> result=new ArrayList<ComboTreeModel>();
		List<PmsQuotationType> types= pmsQuotationTypeFacade.findByParent(typeId);
		for(PmsQuotationType type:types){
			result.add(new ComboTreeModel(type.getTypeId()+"", type.getParentId()+"", type.getTypeName()));
			if(ValidateUtil.isValid(type.getChildren())){
				for(PmsQuotationType child:type.getChildren()){
					result.add(new ComboTreeModel(child.getTypeId()+"", child.getParentId()+"", child.getTypeName()));
				}
			}
		}
		return result;
	}
	
	/**---------------template-----------------**/
	@RequestMapping("/quotationtemplate-list")
	public ModelAndView viewTemplate(final ModelMap model){
		return new ModelAndView("/quotationtemplate-list", model);
	}
	
	@RequestMapping("/quotationtemplate/list")
	public DataGrid<PmsQuotationTemplate> listTemplate(final PmsQuotationTemplate template,final PageParam param){
		// PmsQuotationTemplate 继承Pagination，实现排序
		final long page = param.getPage();
		final long rows = param.getRows();
		param.setBegin((page - 1) * rows);
		param.setLimit(rows);
		return pmsQuotationTemplateFacade.listWithPagination(param, JsonUtil.objectToMap(template));
	}

	@RequestMapping("/quotationtemplate/save")
	public PmsResult saveTemplate(@RequestBody PmsQuotationTemplate pmsQuotationTemplate){
		pmsQuotationTemplate.setType(PmsQuotationTemplate.TYPE_PRODUCT);
		long result=pmsQuotationTemplateFacade.insert(pmsQuotationTemplate);
		PmsResult pmsResult=new PmsResult();
		pmsResult.setResult(result>0?true:false);
		return pmsResult;
	}
	
	@RequestMapping("/quotationtemplate/update")
	public PmsResult updateTemplate(@RequestBody PmsQuotationTemplate pmsQuotationTemplate){
		long result=pmsQuotationTemplateFacade.update(pmsQuotationTemplate);
		PmsResult pmsResult=new PmsResult();
		pmsResult.setResult(result>0?true:false);
		return pmsResult;
	}
	
	@RequestMapping("/quotationtemplate/delete")
	public PmsResult deleteTemplate(final long[] ids){
		//校验
		
		//删除
		long result=0;;
		PmsResult pmsResult=new PmsResult();
		for(long id:ids){
			result=pmsQuotationTemplateFacade.delete(id);
			if(result<=0){
				pmsResult.setResult(false);
			}
		}
		return pmsResult;
	}
	@RequestMapping("/quotationtemplate/get")
	public List<PmsQuotationItem> listTemplateItem(final Long templateId){
		if(templateId==null){
			return new ArrayList<>();
		}
		PmsQuotationTemplate template=pmsQuotationTemplateFacade.getById(templateId);
		if(template==null){
			return new ArrayList<>();
		}
		return template.getItems();
	}
	
}
