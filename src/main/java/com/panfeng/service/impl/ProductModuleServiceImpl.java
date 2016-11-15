package com.panfeng.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.panfeng.persist.ProductModuleMapper;
import com.panfeng.resource.model.ProductModule;
import com.panfeng.service.FDFSService;
import com.panfeng.service.ProductModuleService;

@Service
public class ProductModuleServiceImpl implements ProductModuleService{

	@Autowired
	private ProductModuleMapper pmMapper;
	@Autowired
	private FDFSService fdfsService;
	@Override
	public List<ProductModule> list() {
		List<ProductModule> list= pmMapper.list();
		//提供combotree的字段
		for(ProductModule p : list){
			p.setText(p.getModuleName());
		}
		return list;	
	}
	@Override
	public boolean save(ProductModule productModule,MultipartFile moduleImg) {
		//上传图片
		String fileId = fdfsService.upload(moduleImg);
		productModule.setPic(fileId);
		productModule.setSortIndex(0);
		return pmMapper.save(productModule)>0;
	}
	@Override
	public boolean update(ProductModule productModule,MultipartFile moduleImg) {
		String fileId = fdfsService.upload(moduleImg);
		productModule.setPic(fileId);
		productModule.setSortIndex(0);
		return pmMapper.update(productModule)>0;
	}
	@Override
	public long delete(long[] ids) {
		if(ids != null && ids.length > 0){
			for (final long id : ids) {
				pmMapper.delete(id);
			}
			return 1l;
		}
		return 0l;
	}
}
