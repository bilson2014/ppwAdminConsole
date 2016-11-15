package com.panfeng.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.panfeng.resource.model.ProductModule;

public interface ProductModuleService {

	List<ProductModule> list();

	boolean save(ProductModule productModule, MultipartFile moduleImg);

	boolean update(ProductModule productModule,MultipartFile moduleImg);

	long delete(long[] ids);

}
