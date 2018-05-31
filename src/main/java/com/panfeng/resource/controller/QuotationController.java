package com.panfeng.resource.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.paipianwang.pat.common.entity.ComboTreeModel;
import com.paipianwang.pat.common.entity.DataGrid;
import com.paipianwang.pat.common.entity.PageParam;
import com.paipianwang.pat.common.entity.PmsResult;
import com.paipianwang.pat.common.util.JsonUtil;
import com.paipianwang.pat.common.util.ValidateUtil;
import com.paipianwang.pat.common.web.file.FastDFSClient;
import com.paipianwang.pat.workflow.entity.PmsQuotationItem;
import com.paipianwang.pat.workflow.entity.PmsQuotationTemplate;
import com.paipianwang.pat.workflow.entity.PmsQuotationType;
import com.paipianwang.pat.workflow.enums.ProductionDeviceType;
import com.paipianwang.pat.workflow.enums.ProductionResource;
import com.paipianwang.pat.workflow.facade.PmsQuotationTemplateFacade;
import com.paipianwang.pat.workflow.facade.PmsQuotationTypeFacade;

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
	public PmsResult save(PmsQuotationType pmsQuotationType,@RequestParam final MultipartFile uploadFile){
		
		if (!uploadFile.isEmpty()) {
			String path = FastDFSClient.uploadFile(uploadFile);
			pmsQuotationType.setPhoto(path);
		}		
		
		long result=pmsQuotationTypeFacade.insert(pmsQuotationType);
		PmsResult pmsResult=new PmsResult();
		if(result>0) {
			pmsResult.setResult(true);
			pmsResult.setMsg("添加成功");
		}else {
			pmsResult.setResult(false);
			pmsResult.setMsg("添加失败");
		}
		pmsResult.setResult(result>0?true:false);
		return pmsResult;
	}
	
	@RequestMapping("/quotationtype/update")
	public PmsResult update(PmsQuotationType pmsQuotationType,@RequestParam final MultipartFile uploadFile){
		
		PmsQuotationType old=pmsQuotationTypeFacade.getById(pmsQuotationType.getTypeId());
		if(ValidateUtil.isValid(old.getPhoto()) && !ValidateUtil.isValid(pmsQuotationType.getPhoto())) {
			FastDFSClient.deleteFile(old.getPhoto());
		}
		
		if (!uploadFile.isEmpty()) {			
			String path = FastDFSClient.uploadFile(uploadFile);
			pmsQuotationType.setPhoto(path);
		}
		List<PmsQuotationTemplate> editTemplates=pmsQuotationTypeFacade.update(pmsQuotationType);
		PmsResult pmsResult=new PmsResult();
		if(editTemplates!=null) {
			pmsResult.setResult(true);
			
			String msg="";
			if(!editTemplates.isEmpty() && pmsQuotationType.getStatus().equals(0)) {
				
				for(PmsQuotationTemplate template:editTemplates) {
					if(template.getType()==PmsQuotationTemplate.TYPE_PRODUCT) {
						msg+=template.getTemplateName()+",";
					}
				}
				if(msg!="") {
					msg="该报价单项目已被禁用，但会影响到报价单模板（"+msg+"）。请到报价单模板管理中更新相关项目";
				}
			
			}
			
			pmsResult.setMsg("修改成功!"+msg);

			
		}else {
			pmsResult.setResult(false);
			pmsResult.setMsg("修改失败!");
		}
		return pmsResult;
	}
	
	@RequestMapping("/quotationtype/delete")
	public PmsResult delete(final long[] ids){	
		//删除
		long result=0;;
		PmsResult pmsResult=new PmsResult();
		for(long id:ids){
			//删除图片
			PmsQuotationType old=pmsQuotationTypeFacade.getById(id);			
			result=pmsQuotationTypeFacade.delete(id);
			
			if(ValidateUtil.isValid(old.getPhoto())) {
				FastDFSClient.deleteFile(old.getPhoto());
			}
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
			List<PmsQuotationType> types=null;
			if(grade==4) {
				types=pmsQuotationTypeFacade.findAll();
			}else {
				types=pmsQuotationTypeFacade.findSelectionByGrade(grade);
			}			
			
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
	/**
	 * 获取制片工具资源对应报价单类型
	 * 			配置类型及其所有下级节点
	 * @param typeId
	 * @return
	 */
	@RequestMapping("/quotationtype/production/select")
	public List<ComboTreeModel> listByProduction(String productionType,String subType){
		List<ComboTreeModel> result=new ArrayList<ComboTreeModel>();
		
		Long[] typeIds;
		
		if(ProductionResource.device.getKey().equals(productionType) && ValidateUtil.isValid(subType)) {
			typeIds=ProductionDeviceType.getEnum(Integer.parseInt(subType)).getQuotationType();
		}else {
			ProductionResource relation=ProductionResource.getEnum(productionType);
			typeIds=relation.getQuotationType();
		}
		
		for(Long typeId:typeIds) {
			PmsQuotationType self=pmsQuotationTypeFacade.getById(typeId);
			if(self!=null) {
				result.add(new ComboTreeModel(self.getTypeId()+"", self.getParentId()+"", self.getTypeName()));
				List<PmsQuotationType> types= pmsQuotationTypeFacade.findByParent(typeId);
				for(PmsQuotationType type:types){
					result.add(new ComboTreeModel(type.getTypeId()+"", type.getParentId()+"", type.getTypeName()));
					if(ValidateUtil.isValid(type.getChildren())){
						for(PmsQuotationType child:type.getChildren()){
							result.add(new ComboTreeModel(child.getTypeId()+"", child.getParentId()+"", child.getTypeName()));
						}
					}
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
